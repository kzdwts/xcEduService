package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Kang Yong
 */
@Mapper
public interface CourseMapper {

    CourseBase findCourseBaseById(String id);

    /**
     * 查询分页列表
     *
     * @return
     */
    Page<CourseBase> findCourseList();

    /**
     * 分页list
     *
     * @return
     */
    List<CourseBase> findPageList();
}
