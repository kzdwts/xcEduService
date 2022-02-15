package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;

import java.util.Date;

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

    /**
     * 添加选课
     *
     * @param userId    {@link String} 用户id
     * @param courseId  {@link String} 课程id
     * @param valid     {@link String}
     * @param startTime {@link Date} 开始时间
     * @param endTime   {@link Date} 结束时间
     * @param xcTask    {@link XcTask} 任务
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2022/2/14
     */
    ResponseResult addcourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask);
}
