package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;

/**
 * cms配置管理接口定义
 */
public interface CmsConfigService {

    /**
     * 根据id查询配置信息
     *
     * @param id 主键id
     * @return
     */
    CmsConfig getConfigById(String id);
}
