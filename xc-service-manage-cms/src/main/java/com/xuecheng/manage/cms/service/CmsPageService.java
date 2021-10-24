package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.springframework.web.bind.annotation.PathVariable;

public interface CmsPageService {

    /**
     * 分页查询
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
