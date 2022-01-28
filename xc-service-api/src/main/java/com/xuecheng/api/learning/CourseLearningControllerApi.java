package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 学习工程api
 *
 * @author Kang Yong
 * @date 2022/1/28
 * @since 1.0.0
 */
@Api(value = "录播课程学习管理", description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    @ApiOperation("获取课程学习地址")
    GetMediaResult getmedia(String courseId, String teachplanId);

}
