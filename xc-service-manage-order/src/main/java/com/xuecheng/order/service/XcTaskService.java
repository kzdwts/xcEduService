package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;

import java.util.Date;
import java.util.List;

/**
 * 任务 业务层
 *
 * @author Kang Yong
 * @date 2022/2/13
 * @since 1.0.0
 */
public interface XcTaskService {

    /**
     * 查询任务列表
     *
     * @param updateTime {@link Date}
     * @param size       {@link int}
     * @return {@link List< XcTask>}
     * @author Kang Yong
     * @date 2022/2/13
     */
    List<XcTask> findXcTaskList(Date updateTime, int size);

    /**
     * 发送mq消息
     *
     * @param xcTask     {@link XcTask} 任务内容
     * @param ex         {@link String} 交换机
     * @param routingKey {@link String}
     * @author Kang Yong
     * @date 2022/2/13
     */
    void publish(XcTask xcTask, String ex, String routingKey);

    /**
     * 获取任务（乐观锁：更新成功即获取到任务）
     *
     * @param taskId  {@link String} 任务id
     * @param version {@link Integer} 版本号
     * @return {@link int}
     * @author Kang Yong
     * @date 2022/2/14
     */
    int getTask(String taskId, Integer version);
}
