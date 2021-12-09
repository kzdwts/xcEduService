package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 课程管理控制层
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
@Api(value = "课程管理接口", description = "课程管理接口，提供数据模型的管理、查询")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("新增课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);
}
