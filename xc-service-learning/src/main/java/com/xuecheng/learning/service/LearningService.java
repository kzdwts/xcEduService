package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;

/**
 * 学习服务业务处理
 *
 * @author Kang Yong
 * @date 2022/1/28
 * @since 1.0.0
 */
public interface LearningService {

    /**
     * 根据课程id和教学计划id搜索课程媒资信息
     *
     * @param courseId    {@link String}
     * @param teachplanId {@link String}
     * @return {@link GetMediaResult}
     * @author Kang Yong
     * @date 2022/1/28
     */
    GetMediaResult getMedia(String courseId, String teachplanId);
}
