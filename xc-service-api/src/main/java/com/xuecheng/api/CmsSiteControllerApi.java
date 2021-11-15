package com.xuecheng.api;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface CmsSiteControllerApi {

    /**
     * 获取站点列表
     *
     * @return
     */
    @GetMapping("/listAll")
    List<CmsSite> findList();
}
