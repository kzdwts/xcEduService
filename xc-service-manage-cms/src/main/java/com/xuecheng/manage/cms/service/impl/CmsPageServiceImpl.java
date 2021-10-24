package com.xuecheng.manage.cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage.cms.dao.CmsPageRepository;
import com.xuecheng.manage.cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CmsPageServiceImpl implements CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 页面查询方法
     *
     * @param page             页码， 从1开始计数
     * @param size
     * @param queryPageRequest 查询条件
     * @return
     */
    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        // 参数处理
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size < 0) {
            size = 10;
        }

        // 分页参数
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> cmsPagePage = cmsPageRepository.findAll(pageable);

        // 封装返回结果
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPagePage.getContent()); // 内容
        queryResult.setTotal(cmsPagePage.getTotalElements()); // 总数量

        // 返回结果
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
