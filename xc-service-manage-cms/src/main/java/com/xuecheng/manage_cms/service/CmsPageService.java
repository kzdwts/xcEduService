package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface CmsPageService {

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据id查询详情
     *
     * @param pageId
     * @return
     */
    CmsPage findById(String pageId);

    /**
     * 添加页面，如果已经存在则更新
     *
     * @param cmsPage
     * @return
     */
    CmsPageResult save(CmsPage cmsPage);

    /**
     * 更新页面
     *
     * @param pageId
     * @param cmsPage
     * @return
     */
    CmsPageResult edit(String pageId, CmsPage cmsPage);

    /**
     * 删除页面
     *
     * @param pageId
     * @return
     */
    ResponseResult delete(String pageId);

    /**
     * 页面静态化
     *
     * @param pageId 页面id
     * @return
     */
    String getPageHtml(String pageId);

    /**
     * 发布页面
     *
     * @param pageId
     * @return
     */
    ResponseResult postPage(String pageId);

    /**
     * 一键发布
     *
     * @param cmsPage
     * @return
     */
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
