package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学习媒资查询
 *
 * @author Kang Yong
 * @date 2022/1/29
 * @since 1.0.0
 */
@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    private LearningService learningService;

    /**
     * 查询学习媒资信息
     *
     * @param courseId    {@link String}
     * @param teachplanId {@link String}
     * @return {@link GetMediaResult}
     * @author Kang Yong
     * @date 2022/1/29
     */
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    @Override
    public GetMediaResult getmedia(@PathVariable("courseId") String courseId, @PathVariable("teachplanId") String teachplanId) {
        return this.learningService.getMedia(courseId, teachplanId);
    }
}
