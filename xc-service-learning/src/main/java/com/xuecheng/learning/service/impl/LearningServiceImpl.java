package com.xuecheng.learning.service.impl;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import com.xuecheng.learning.service.LearningService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 学习服务业务实现层
 *
 * @author Kang Yong
 * @date 2022/1/28
 * @since 1.0.0
 */
@Service
public class LearningServiceImpl implements LearningService {

    @Autowired
    private CourseSearchClient courseSearchClient;

    /**
     * 根据课程id和教学计划id搜索课程媒资信息
     *
     * @param courseId    {@link String}
     * @param teachplanId {@link String}
     * @return {@link GetMediaResult}
     * @author Kang Yong
     * @date 2022/1/28
     */
    @Override
    public GetMediaResult getMedia(String courseId, String teachplanId) {
        // 校验学生的学习权限，是否资费等

        // 查询课程媒资信息
        TeachplanMediaPub teachplanMediaPub = this.courseSearchClient.getmedia(teachplanId);
        if (ObjectUtils.isEmpty(teachplanMediaPub) || StringUtils.isBlank(teachplanMediaPub.getMediaUrl())) {
            // 获取视频播放地址出错
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        return new GetMediaResult(CommonCode.SUCCESS, teachplanMediaPub.getMediaUrl());
    }
}