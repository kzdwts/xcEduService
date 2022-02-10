package com.xuecheng.manage_course.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.config.CoursePublishProperties;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 课程管理
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;

    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CoursePubRepository coursePubRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Autowired
    private CoursePublishProperties coursePublishProperties;

    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = this.teachplanMapper.selectList(courseId);
        return teachplanNode;
    }

    @Override
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        // 参数非空判断
        if (teachplan == null
                || StringUtils.isBlank(teachplan.getCourseid())
                || StringUtils.isBlank(teachplan.getPname())
        ) {
            return new ResponseResult(CommonCode.INVALIDATE_PARAM);
        }

        // 课程id
        String courseid = teachplan.getCourseid();
        // 获取父节点
        String parentId = teachplan.getParentid();
        if (StringUtils.isBlank(parentId)) {
            parentId = this.getTeahplanRoot(courseid, "0");
            if (parentId == null) {
                // 课程id非法
                return new ResponseResult(CommonCode.INVALIDATE_PARAM);
            }
        }

        // 查询父节点
        Optional<Teachplan> optionalTeachplan = this.teachplanRepository.findById(parentId);
        Teachplan parentNode = optionalTeachplan.get();
        // 保存课程计划
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentId);
        teachplanNew.setCourseid(courseid);
        teachplanNew.setStatus("0"); // 未发布
        if (parentNode.getGrade().equals("1")) {
            // 一共只有三级
            teachplanNew.setGrade("2");
        } else {
            teachplanNew.setGrade("3");
        }
        this.teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public QueryResponseResult findCourseList(String companyId, Integer pageNum, Integer pageSize, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (pageNum <= 0) pageNum = 0;
        if (pageSize <= 0) pageSize = 20;

        // 条件
        courseListRequest.setCompanyId(companyId);

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询
        Page<CourseInfo> courseInfoPage = this.courseMapper.findCoursePageList(courseListRequest);
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<>();
        courseInfoQueryResult.setList(courseInfoPage.getResult());
        courseInfoQueryResult.setTotal(courseInfoPage.getTotal());

        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseInfoQueryResult);
    }

    @Override
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        // 课程状态设置为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    @Override
    public CourseBase getCoursebaseById(String courseId) {
        Optional<CourseBase> optional = this.courseBaseRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public ResponseResult updateCoursebase(String id, CourseBase courseBase) {
        // 查询出来
        CourseBase coursebaseExists = this.getCoursebaseById(id);
        if (coursebaseExists == null) {
            ExceptionCast.cast(CommonCode.INVALIDATE_PARAM);
        }

        // 修改课程信息
        coursebaseExists.setName(courseBase.getName());
        coursebaseExists.setMt(courseBase.getMt());
        coursebaseExists.setSt(courseBase.getSt());
        coursebaseExists.setGrade(courseBase.getGrade());
        coursebaseExists.setStudymodel(courseBase.getStudymodel());
        coursebaseExists.setUsers(courseBase.getUsers());
        coursebaseExists.setDescription(courseBase.getDescription());
        CourseBase save = this.courseBaseRepository.save(coursebaseExists);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 新增图片
     *
     * @param courseId {@link String} 课程id
     * @param pic      {@link String} 图片
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2021/12/17
     */
    @Override
    public ResponseResult saveCoursePic(String courseId, String pic) {
        // 查询
        Optional<CoursePic> optionalCoursePic = this.coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (optionalCoursePic.isPresent()) {
            coursePic = optionalCoursePic.get();
        }

        // 封装参数
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePic.setCreateTime(new Date());
        this.coursePicRepository.save(coursePic);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程图片信息
     *
     * @param courseId {@link String}
     * @return {@link CoursePic}
     * @author Kang Yong
     * @date 2021/12/17
     */
    @Override
    public CoursePic findCoursePic(String courseId) {
        // 直接查询
        Optional<CoursePic> optionalCoursePic = this.coursePicRepository.findById(courseId);
        if (optionalCoursePic.isPresent()) {
            return optionalCoursePic.get();
        }
        return null;
    }

    /**
     * 删除课程图片信息
     *
     * @param courseId {@link String}
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2021/12/17
     */
    @Transactional
    @Override
    public ResponseResult deleteCoursePic(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 课程发布
     *
     * @param courseId 课程id
     * @return
     */
    @Transactional
    @Override
    public CoursePublishResult publish(String courseId) {
        // 查询课程信息
        CourseBase courseBase = this.findCourseBaseById(courseId);

        // 发布课程详情页面
        CmsPostPageResult cmsPostPageResult = this.publishPage(courseId);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }

        // 更新课程状态
        CourseBase updatedCourseBase = this.saveCoursePubState(courseId);

        // 课程索引
        CoursePub coursePub = this.createCoursePub(courseId);
        // 保存
        this.saveCoursePub(courseId, coursePub);

        // 页面url
        String pageUrl = cmsPostPageResult.getPageUrl();

        // 保存媒资文件发布信息
        this.saveTeachplanMediaPub(courseId);

        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    /**
     * 保存课程计划媒资信息
     *
     * @param courseId
     */
    private void saveTeachplanMediaPub(String courseId) {
        // 查询课程媒资信息
        List<TeachplanMedia> teachplanMediaList = this.teachplanMediaRepository.findByCourseId(courseId);
        // 将课程计划媒资信息存储待索引表
        this.teachplanMediaPubRepository.deleteByCourseId(courseId);

        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
            teachplanMediaPubList.add(teachplanMediaPub);
        }

        this.teachplanMediaPubRepository.saveAll(teachplanMediaPubList);
    }

    /**
     * 保存媒资信息
     *
     * @param teachplanMedia
     * @return
     */
    @Override
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        // 参数判断
        if (teachplanMedia == null) {
            ExceptionCast.cast(CommonCode.INVALIDATE_PARAM);
        }

        // 课程计划
        String teachplanId = teachplanMedia.getTeachplanId();

        // 查询课程计划
        Optional<Teachplan> teachplanOptional = this.teachplanRepository.findById(teachplanId);
        if (!teachplanOptional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_URLISNULL);
        }
        Teachplan teachplan = teachplanOptional.get();
        // 自允许为子节点课程选择视频
        String grade = teachplan.getGrade();
        if (org.springframework.util.StringUtils.isEmpty(grade) || !grade.equals("3")) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = this.teachplanMediaRepository.findById(teachplanId);
        if (teachplanMediaOptional.isPresent()) {
            one = teachplanMediaOptional.get();
        } else {
            one = new TeachplanMedia();
        }

        // 保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        this.teachplanMediaRepository.save(one);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存课程
     *
     * @param courseId
     * @param coursePub
     * @return
     */
    private CoursePub saveCoursePub(String courseId, CoursePub coursePub) {
        CoursePub coursePubNew = null;
        Optional<CoursePub> coursePubOptional = this.coursePubRepository.findById(courseId);
        if (coursePubOptional.isPresent()) {
            coursePubNew = coursePubOptional.get();
        } else {
            coursePubNew = new CoursePub();
        }

        // 属性复制
        BeanUtils.copyProperties(coursePub, coursePubNew);
        // 时间戳
        coursePubNew.setTimestamp(new Date());
        // 发布时间
        coursePubNew.setPubTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        coursePubNew.setId(courseId);

        // 保存
        this.coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }

    /**
     * 创建coursePub对象
     *
     * @param courseId
     * @return
     */
    private CoursePub createCoursePub(String courseId) {
        CoursePub coursePub = new CoursePub();
        // 查询课程基础信息course_base
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        // 查询课程图片信息
        Optional<CoursePic> coursePicOptional = this.coursePicRepository.findById(courseId);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }

        // 查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = this.courseMarketRepository.findById(courseId);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        // 查询课程计划信息
        TeachplanNode teachplanNode = this.teachplanMapper.selectList(courseId);
        if (!ObjectUtils.isEmpty(teachplanNode)) {
            // 不为空
            String jsonString = JSON.toJSONString(teachplanNode);
            coursePub.setTeachplan(jsonString);
        }

        return coursePub;
    }

    /**
     * 更新课程发布状态
     *
     * @param courseId 课程id
     * @return
     */
    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        CourseBase save = this.courseBaseRepository.save(courseBase);
        return save;
    }

    /**
     * 调用一键发布，进行页面发布
     *
     * @param courseId
     * @return
     */
    private CmsPostPageResult publishPage(String courseId) {
        // 查询详情
        CourseBase courseBase = this.findCourseBaseById(courseId);
        // 发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setSiteId(coursePublishProperties.getSiteId());
        cmsPage.setTemplateId(coursePublishProperties.getTemplateId());
        cmsPage.setPageWebPath(coursePublishProperties.getPageWebPath());
        cmsPage.setPagePhysicalPath(coursePublishProperties.getPagePhysicalPath());
        cmsPage.setDataUrl(coursePublishProperties.getDataUrlPre() + courseBase.getId());

        // 发布页面
        CmsPostPageResult cmsPostPageResult = this.cmsPageClient.postPageQuick(cmsPage);
        return cmsPostPageResult;
    }

    /**
     * 查询coursebase信息
     *
     * @param courseId
     * @return
     */
    private CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            return courseBaseOptional.get();
        }
        return null;
    }

    /**
     * 获取课程计划父节点id
     *
     * @param courseid 课程id
     * @param parentId 父节点
     * @return
     */
    private String getTeahplanRoot(String courseid, String parentId) {
        // 查询课程信息
        Optional<CourseBase> optionalCourseBase = this.courseBaseRepository.findById(courseid);
        if (!optionalCourseBase.isPresent()) {
            return null;
        }
        CourseBase courseBase = optionalCourseBase.get();

        // 查询
        List<Teachplan> teachplanList = this.teachplanRepository.findByCourseidAndParentid(courseid, parentId);
        if (CollectionUtils.isEmpty(teachplanList)) {
            // 新增
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseid);
            teachplanRoot.setParentid("0");
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setGrade("1"); // 一级
            teachplanRoot.setStatus("0"); // 未发布
            // 保存
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }

        // 不为空，直接返回id
        return teachplanList.get(0).getId();
    }
}
