package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.course.CourseBase;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 课程管理dao
 *
 * @author Kang Yong
 * @date 2021/12/12
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseMapperTest extends TestCase {

    @Autowired
    private CourseMapper courseMapper;

    @Test
    public void testFindCourseList() {
        PageHelper.startPage(1, 9);
        Page<CourseBase> page = this.courseMapper.findCourseList();
        List<CourseBase> courseBaseList = page.getResult();
        System.out.println(courseBaseList.size());
    }

    @Test
    public void testFindPageList() {
        PageHelper.startPage(2, 5);
        List<CourseBase> pageList = this.courseMapper.findPageList();
        PageInfo<CourseBase> pageInfo = new PageInfo<>(pageList);
        List<CourseBase> list = pageInfo.getList();
        System.out.println(list.size());
    }
}