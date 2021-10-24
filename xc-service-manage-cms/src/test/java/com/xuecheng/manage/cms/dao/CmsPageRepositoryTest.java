package com.xuecheng.manage.cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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