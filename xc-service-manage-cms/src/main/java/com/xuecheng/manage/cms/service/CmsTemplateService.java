package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;

import java.util.List;

/**
 * cms模板接口层
 */
public interface CmsTemplateService {

    /**
     * 查询模板列表
     *
     * @return
     */
    List<CmsTemplate> findList();
}
