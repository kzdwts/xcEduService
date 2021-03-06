package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 课程管理 控制层
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    /**
     * 课程计划查询
     *
     * @param courseId {@link String} 课程id
     * @return {@link TeachplanNode}
     * @author Kang Yong
     * @date 2021/12/9
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    /**
     * 新增课程计划
     *
     * @param teachplan {@link Teachplan}
     * @return {@link TeachplanNode}
     * @author Kang Yong
     * @date 2021/12/9
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /**
     * 查询课程列表
     *
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("/coursebase/list/{page}/{size}")
    @Override
    public QueryResponseResult<CourseInfo> findCourseList(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size,
            CourseListRequest courseListRequest, HttpServletRequest request) {
        // 调用工具类取出用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        // 获取用户归属公司
        String companyId = userJwt.getCompanyId();
        return courseService.findCourseList(companyId, page, size, courseListRequest);
    }

    /**
     * 添加课程基础信息
     *
     * @param courseBase
     * @return
     */
    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    /**
     * 获取课程基础信息
     *
     * @param courseId
     * @return
     */
    @PreAuthorize("hasAuthority('course_get_baseinfo')")
    @GetMapping("/coursebase/get/{courseId}")
    @Override
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) {
        return courseService.getCoursebaseById(courseId);
    }

    /**
     * 更新课程基础信息
     *
     * @param id
     * @param courseBase
     * @return
     */
    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {
        return courseService.updateCoursebase(id, courseBase);
    }

    /**
     * 添加课程图片
     *
     * @param courseId {@link String} 课程id
     * @param pic      {@link String} 图片
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2021/12/17
     */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.saveCoursePic(courseId, pic);
    }

    /**
     * 获取课程图片信息
     *
     * @param courseId {@link String} 课程id
     * @return {@link CoursePic}
     * @author Kang Yong
     * @date 2021/12/17
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    /**
     * 删除课程图片信息
     *
     * @param courseId {@link String} 课程ID
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2021/12/17
     */
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    /**
     * 课程发布
     *
     * @param id
     * @return
     */
    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {
        return courseService.publish(id);
    }

    /**
     * 保存媒资信息
     *
     * @param teachplanMedia
     * @return
     */
    @PostMapping("/savemedia")
    @Override
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }

}
