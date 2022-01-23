package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 搜索
 *
 * @author Kang Yong
 * @date 2022/1/6
 * @since 1.0.0
 */
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    /**
     * 课程搜索
     *
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    @Override
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return esCourseService.list(page, size, courseSearchParam);
    }

    /**
     * 根据课程id查询课程信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getall/{id}")
    @Override
    public Map<String, CoursePub> getall(String id) {
        return esCourseService.getall(id);
    }
}
