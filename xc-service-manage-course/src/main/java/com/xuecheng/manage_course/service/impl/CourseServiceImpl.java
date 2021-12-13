package com.xuecheng.manage_course.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import com.xuecheng.manage_course.service.CourseService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private TeachplanMapper teachplanMapper;

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
    public QueryResponseResult findCourseList(Integer pageNum, Integer pageSize, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (pageNum <= 0) pageNum = 0;
        if (pageSize <= 0) pageSize = 20;

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询
        Page<CourseInfo> courseInfoPage = this.courseMapper.findCoursePaageList(courseListRequest);
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
