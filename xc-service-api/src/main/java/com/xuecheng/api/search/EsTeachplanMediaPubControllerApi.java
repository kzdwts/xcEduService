package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 课程计划 媒资管理查询
 *
 * @author Kang Yong
 * @date 2022/1/27
 * @since 1.0.0
 */
@Api(value = "课程计划媒资信息搜索", description = "课程计划媒资信息搜索", tags = {"课程计划媒资信息搜索"})
public interface EsTeachplanMediaPubControllerApi {

    @ApiOperation("根据课程计划id搜索媒资信息")
    TeachplanMediaPub getmedia(String teachplanId);
}
