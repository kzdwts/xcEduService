package com.xuecheng.manage.cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    /**
     * 页面查询
     *
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    @Override
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        // 查询结果集
//        QueryResult<CmsPage> queryResult = new QueryResult<>();
//        List<CmsPage> list = new ArrayList<>();
//        CmsPage cmsPage = new CmsPage();
//        cmsPage.setPageName("测试页面");
//        list.add(cmsPage);
//        queryResult.setList(list);
//        queryResult.setTotal(1);
//        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return cmsPageService.findList(page, size, queryPageRequest);
    }

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    @PostMapping("/add")
    @Override
    public CmsPageResult add(@RequestBody @Valid CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    /**
     * 查询页面详情
     *
     * @param pageId 页面id
     * @return
     */
    @GetMapping("/findById/{pageId}")
    @Override
    public CmsPage findById(@PathVariable("pageId") String pageId) {
        return cmsPageService.findById(pageId);
    }

    /**
     * 更新
     *
     * @param pageId  页面id
     * @param cmsPage 页面信息
     * @return
     */
    @PutMapping("/edit/{pageId}")
    @Override
    public CmsPageResult edit(@PathVariable("pageId") String pageId, @RequestBody CmsPage cmsPage) {
        return cmsPageService.edit(pageId, cmsPage);
    }

    /**
     * 删除页面
     *
     * @param pageId
     * @return
     */
    @DeleteMapping("/del/{pageId}")
    @Override
    public ResponseResult delete(@PathVariable("pageId") String pageId) {
        return cmsPageService.delete(pageId);
    }


    public String getPageHtml(String pageId) {
        // 获取页面
        return cmsPageService.getPageHtml(pageId);
    }

}
