package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.portalview.ViewCourse;

/**
 * 课程视图service
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
public interface CourseViewService {

    /**
     * 查询课程视图数据
     *
     * @param courseId 课程id
     * @return
     */
    ViewCourse courseView(String courseId);
}
