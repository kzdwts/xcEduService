package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

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
}
