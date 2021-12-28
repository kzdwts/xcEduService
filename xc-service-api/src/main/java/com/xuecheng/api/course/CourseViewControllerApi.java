package com.xuecheng.api.course;

import com.xuecheng.framework.domain.portalview.ViewCourse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 课程视图控制层
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
@Api(value = "课程视图接口", description = "课程视图信息接口，提供数据模型的管理、查询")
public interface CourseViewControllerApi {

    @ApiOperation("课程视图查询")
    ViewCourse courseView(String id);

}
