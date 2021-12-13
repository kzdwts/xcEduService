package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;

/**
 * TODO desc
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
public interface CategoryService {

    /**
     * 查询课程分类列表
     *
     * @return
     */
    CategoryNode findList();
}
