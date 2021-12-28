package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 课程预览
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
@Api(value = "课程预览接口", description = "课程预览信息接口，提供数据模型的管理、查询")
public interface CoursePublishControllerApi {

    @ApiOperation("预览课程")
    CoursePublishResult preview(String id);
}
