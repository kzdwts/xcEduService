package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 课程dao测试
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TeachplanMapperTest extends TestCase {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Test
    public void testTeachplanSelect() {
        TeachplanNode teachplanNode = this.teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode.toString());
    }

}