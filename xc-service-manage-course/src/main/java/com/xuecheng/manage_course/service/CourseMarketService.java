package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * 课程营销
 *
 * @author Kang Yong
 * @date 2021/12/14
 * @since 1.0.0
 */
public interface CourseMarketService {

    /**
     * 根据课程id获取课程营销信息
     *
     * @param courseId
     * @return
     */
    CourseMarket getCourseMarketById(String courseId);

    /**
     * 更新课程营销信息
     *
     * @param courseId     课程id
     * @param courseMarket 课程营销信息
     * @return
     */
    ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket);
}
