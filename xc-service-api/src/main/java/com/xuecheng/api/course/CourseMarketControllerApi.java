package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 课程营销信息管理
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@Api(value = "课程营销信息接口", description = "课程营销信息接口，提供数据模型的管理、查询")
public interface CourseMarketControllerApi {

    @ApiOperation("获取课程营销信息")
    CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);
}
