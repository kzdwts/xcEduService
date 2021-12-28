package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePublishControllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
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
    @Override
    public CoursePublishResult preview(String id) {
        return null;
    }
}
