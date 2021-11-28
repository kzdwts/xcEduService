package com.xuecheng.manage.cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage.cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 页面预览 controller
 *
 * @author Kang Yong
 * @date 2021/11/28
 * @since 1.0.0
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CmsPageService cmsPageService;

    /**
     * 页面预览
     *
     * @param pageId
     */
    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) {
        // 查询页面数据
        String pageHtml = cmsPageService.getPageHtml(pageId);
        // 为空就不处理了
        if (StringUtils.isNotEmpty(pageHtml)) {
            try {
                ServletOutputStream outputStream = this.response.getOutputStream();
                outputStream.write(pageHtml.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
