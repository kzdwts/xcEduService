package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePublishControllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 课程预览
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
@RequestMapping("/course")
public class CoursePublishController implements CoursePublishControllerApi {

    @Autowired
    private CourseService courseService;

    @Override
    public CoursePublishResult preview(String id) {
        return null;
    }

    /**
     * 课程发布
     *
     * @param id
     * @return
     */
    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable String id) {
        return courseService.publish(id);
    }
}
