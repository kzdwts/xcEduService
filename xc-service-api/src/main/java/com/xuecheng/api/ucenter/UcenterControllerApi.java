package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户中心管理
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {

    @ApiOperation("根据账号查询用户信息")
    XcUserExt getUserext(String username);
}
