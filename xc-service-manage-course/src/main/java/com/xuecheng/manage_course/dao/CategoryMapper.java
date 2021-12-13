package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程分类
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper {

    /**
     * 查询课程分类列表
     *
     * @return
     */
    CategoryNode selectList();
}
