package com.xuecheng.manage.cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage.cms.dao.CmsSiteRepository;
import com.xuecheng.manage.cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsSiteServiceImpl implements CmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Override
    public List<CmsSite> findList() {
        return cmsSiteRepository.findAll();
    }
}
