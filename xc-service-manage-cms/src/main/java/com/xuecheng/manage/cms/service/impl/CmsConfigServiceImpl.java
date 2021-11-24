package com.xuecheng.manage.cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage.cms.dao.CmsConfigRepository;
import com.xuecheng.manage.cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsConfigServiceImpl implements CmsConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Override
    public CmsConfig getConfigById(String id) {
        // 从mongo查询
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }
}
