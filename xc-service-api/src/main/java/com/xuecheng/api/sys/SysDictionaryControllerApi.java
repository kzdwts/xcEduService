package com.xuecheng.api.sys;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 字典
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@Api(value = "字典管理接口", description = "字典管理接口，提供数据模型的管理、查询")
public interface SysDictionaryControllerApi {

    @ApiOperation(value = "数据字典查询")
    SysDictionary getByType(String type);
}
