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
}
