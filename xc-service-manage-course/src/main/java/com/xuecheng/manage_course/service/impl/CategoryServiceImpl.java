package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 课程分类业务实现层
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryNode findList() {
        return categoryMapper.selectList();
    }
}
