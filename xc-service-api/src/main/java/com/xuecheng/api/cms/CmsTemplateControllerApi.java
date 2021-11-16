package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "cms模板管理接口", description = "cms模板管理接口，提供模板的增删改查")
public interface CmsTemplateControllerApi {

    /**
     * 获取模板列表
     *
     * @return
     */
    @ApiOperation("查询模板列表")
    List<CmsTemplate> findList();
}
