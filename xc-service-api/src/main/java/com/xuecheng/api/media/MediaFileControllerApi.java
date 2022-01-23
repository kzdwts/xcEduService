package com.xuecheng.api.media;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 媒资文件管理 Controller Api
 *
 * @author Kang Yong
 * @date 2022/1/23
 * @since 1.0.0
 */
@Api(value = "媒资文件管理", description = "媒资文件管理接口", tags = {"媒资文件管理接口"})
public interface MediaFileControllerApi {

    @ApiOperation("查询文件列表")
    QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

}
