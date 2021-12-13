package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * 课程管理
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
public interface CourseService {

    /**
     * 根据课程id，查询课程计划
     *
     * @param courseId {@link String}
     * @return {@link TeachplanNode}
     * @author Kang Yong
     * @date 2021/12/9
     */
    TeachplanNode findTeachplanList(String courseId);

    /**
     * 新增课程计划
     *
     * @param teachplan {@link Teachplan}
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2021/12/9
     */
    ResponseResult addTeachplan(Teachplan teachplan);

    /**
     * 查询课程列表
     *
     * @param pageNum           当前页
     * @param pageSize          每页条数
     * @param courseListRequest 查询条件
     * @return
     */
    QueryResponseResult findCourseList(Integer pageNum, Integer pageSize, CourseListRequest courseListRequest);

    /**
     * 添加课程基础信息
     *
     * @param courseBase
     * @return
     */
    AddCourseResult addCourseBase(CourseBase courseBase);

    /**
     * 获取课程基础信息
     *
     * @param courseId
     * @return
     */
    CourseBase getCoursebaseById(String courseId);

    /**
     * 更新课程信息
     *
     * @param id         课程id
     * @param courseBase 课程信息
     * @return
     */
    ResponseResult updateCoursebase(String id, CourseBase courseBase);
}
