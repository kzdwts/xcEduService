package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
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

    /**
     * 分页查询课程列表
     *
     * @param courseListRequest
     * @return
     */
    Page<CourseInfo> findCoursePaageList(CourseListRequest courseListRequest);
}
