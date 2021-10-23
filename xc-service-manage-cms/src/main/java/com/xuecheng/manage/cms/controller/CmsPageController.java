package com.xuecheng.manage.cms.controller;

import com.xuecheng.api.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

public class CmsPageController implements CmsPageControllerApi {


    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        return null;
    }
}
