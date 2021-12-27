package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
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

    @ApiOperation("查询课程列表")
    QueryResponseResult<CourseInfo> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程基础信息")
    AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程基础信息")
    CourseBase getCourseBaseById(String courseId);

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("获取课程图片信息")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片信息")
    ResponseResult deleteCoursePic(String courseId);

    // TODO 课程预览

    @ApiOperation("课程发布")
    CoursePublishResult publish(String id);

}
