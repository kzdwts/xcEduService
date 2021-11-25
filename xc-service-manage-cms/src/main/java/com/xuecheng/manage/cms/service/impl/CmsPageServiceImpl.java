package com.xuecheng.manage.cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomerException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.cms.dao.CmsPageRepository;
import com.xuecheng.manage.cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Optional;

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
        if (ObjectUtils.isEmpty(queryPageRequest)) {
            // 保证后边的逻辑不会包空指针异常
            queryPageRequest = new QueryPageRequest();
        }
        // 自定义条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

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
        Page<CmsPage> cmsPagePage = cmsPageRepository.findAll(example, pageable);

        // 封装返回结果
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPagePage.getContent()); // 内容
        queryResult.setTotal(cmsPagePage.getTotalElements()); // 总数量

        // 返回结果
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        // 先查询是否已经有了,页面名称、站点、页面path
        CmsPage existCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        // 如果没有就新增
        if (!ObjectUtils.isEmpty(existCmsPage)) {
            // 页面名称已存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        // 新增
        cmsPage.setPageId(null);
        cmsPage.setPageCreateTime(new Date());
        cmsPageRepository.save(cmsPage);
        // 返回成功
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    @Override
    public CmsPage findById(String pageId) {
        Optional<CmsPage> opCmsPage = cmsPageRepository.findById(pageId);
        if (opCmsPage.isPresent()) {
            CmsPage cmsPage = opCmsPage.get();
            return cmsPage;
        }
        return null;
    }

    @Override
    public CmsPageResult edit(String pageId, CmsPage cmsPage) {
        // 先查询
        CmsPage existsCmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(existsCmsPage)) {
            // 如果为空，直接返回错误信息
            return new CmsPageResult(CommonCode.FAIL, null);
        }

        // 补全参数
        existsCmsPage.setTemplateId(cmsPage.getTemplateId());
        existsCmsPage.setSiteId(cmsPage.getSiteId());
        existsCmsPage.setPageAliase(cmsPage.getPageAliase());
        existsCmsPage.setPageName(cmsPage.getPageName());
        existsCmsPage.setPageWebPath(cmsPage.getPageWebPath());
        existsCmsPage.setPageParameter(cmsPage.getPageParameter());
        existsCmsPage.setDataUrl(cmsPage.getDataUrl());

        // 更新
        CmsPage savedCmsPage = cmsPageRepository.save(cmsPage);
        if (ObjectUtils.isEmpty(savedCmsPage)) {
            // 如果为空，直接返回错误信息
//            return new CmsPageResult(CommonCode.FAIL, null);
//            throw new CustomerException(CommonCode.FAIL);
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return new CmsPageResult(CommonCode.SUCCESS, savedCmsPage);
    }

    @Override
    public ResponseResult delete(String pageId) {
        // 先查询
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
//            return new ResponseResult(CommonCode.FAIL);
            ExceptionCast.cast(CommonCode.FAIL);
        }

        // 删除
        cmsPageRepository.delete(cmsPage);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
