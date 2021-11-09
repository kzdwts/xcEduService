package com.xuecheng.manage.cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 测试查询全部
     */
    @Test
    public void testSelect() {
        List<CmsPage> list = cmsPageRepository.findAll();
        list.forEach(System.out::println);
    }

    /**
     * 测试查询全部
     */
    @Test
    public void testFindAllByExample() {
        // 分页参数
        int page = 0; // 从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // 自定义查询条件
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");
        cmsPage.setPageAliase("轮播");
        // 查询构造器（类似mp的Wrapper）
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all);
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testFindPage() {
        // 分页参数
        int page = 0; // 从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all.getTotalPages());
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate() {
        // 先查询
        Optional<CmsPage> optional = cmsPageRepository.findById("5af942190e661827d8e2f5e3");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            System.out.println(cmsPage);
            // 设置参数
            cmsPage.setPageName("preview_4028e58161bd3b380161bd3bcd2f0000.html"); // preview_4028e58161bd3b380161bd3bcd2f0000.html
            // 更新
            CmsPage saveResult = cmsPageRepository.save(cmsPage);
            System.out.println(saveResult);
        }
    }

    /**
     * 自定义方法
     */
    @Test
    public void testFindByPageName() {

        CmsPage cmsPage = cmsPageRepository.findByPageName("preview_4028e58161bd3b380161bd3bcd2f0000.html");
        System.out.println(cmsPage);

    }

}