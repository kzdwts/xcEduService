package com.xuecheng.manage.cms.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * cmspage测试
 *
 * @author Kang Yong
 * @date 2021/11/28
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageServiceTest extends TestCase {

    @Autowired
    private CmsPageService cmsPageService;

    /**
     * 生成静态页面测试
     */
    @Test
    public void testGetPageHtml() {
        String pageId = "6193c45d91fdaa3188ae5e86";
        String pageHtml = this.cmsPageService.getPageHtml(pageId);
        System.out.println(pageHtml);
    }
}