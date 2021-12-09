package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 课程管理
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = this.teachplanMapper.selectList(courseId);
        return teachplanNode;
    }
}
