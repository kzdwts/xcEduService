package com.xuecheng.manage.cms.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage.cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * cms 站点 控制层
 */
@RestController
@RequestMapping("/cms/site")
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    private CmsSiteService cmsSiteService;

    /**
     * 获取站点列表
     *
     * @return
     */
    @GetMapping("/listAll")
    public List<CmsSite> findList() {
        return cmsSiteService.findList();
    }


}
