package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 课程分类
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceTest extends TestCase {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testFindList() {
        CategoryNode node = this.categoryService.findList();
        System.out.println(node.getName());
    }
}