package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseMarketControllerApi;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * TODO desc
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@RestController
@RequestMapping("/course")
public class CourseMarketController implements CourseMarketControllerApi {

    /**
     * 根据课程id获取课程营销信息
     *
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursemarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return null;
    }

    /**
     * 更新课程营销信息
     *
     * @param id
     * @param courseMarket
     * @return
     */
    @Override
    @PostMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
        return null;
    }
}
