package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

import java.util.Map;

/**
 * 搜索 业务实现层
 *
 * @author Kang Yong
 * @date 2022/1/6
 * @since 1.0.0
 */
public interface EsCourseService {

    /**
     * 课程搜索
     *
     * @param page              第几页
     * @param size              每页多少条数据
     * @param courseSearchParam 搜索条件
     * @return
     */
    QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);

    /**
     * 根据课程id查询课程信息
     *
     * @param id
     * @return
     */
    Map<String, CoursePub> getall(String id);

    /**
     * 根据课程计划查询媒资信息
     *
     * @param teachplanIds {@link String[]}
     * @return {@link QueryResponseResult< TeachplanMediaPub>}
     * @author Kang Yong
     * @date 2022/1/27
     */
    QueryResponseResult<TeachplanMediaPub> getmedia(String[] teachplanIds);
}
