package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;

import java.util.List;

public interface CmsSiteService {

    /**
     * 查询列表
     *
     * @return
     */
    List<CmsSite> findList();

}
