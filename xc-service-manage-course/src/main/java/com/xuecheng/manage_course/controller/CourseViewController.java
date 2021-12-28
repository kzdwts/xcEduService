package com.xuecheng.manage_course.controller;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.api.course.CourseViewControllerApi;
import com.xuecheng.framework.domain.portalview.ViewCourse;
import com.xuecheng.manage_course.service.CourseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程视图
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
@RestController
@RequestMapping("/course")
public class CourseViewController implements CourseViewControllerApi {

    @Autowired
    private CourseViewService courseViewService;

    /**
     * 课程视图
     *
     * @param courseId 课程id
     * @return
     */
    @Override
    @GetMapping("/courseview/{id}")
    public ViewCourse courseView(@PathVariable("id") String courseId) {
        return this.courseViewService.courseView(courseId);
    }
}
