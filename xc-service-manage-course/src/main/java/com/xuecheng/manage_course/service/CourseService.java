package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
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
}
