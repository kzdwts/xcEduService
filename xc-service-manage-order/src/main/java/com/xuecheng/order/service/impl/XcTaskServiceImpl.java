package com.xuecheng.order.service.impl;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.dao.XcTaskRepository;
import com.xuecheng.order.service.XcTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 学成任务 业务实现层
 *
 * @author Kang Yong
 * @date 2022/2/13
 * @since 1.0.0
 */
@Service
public class XcTaskServiceImpl implements XcTaskService {

    @Autowired
    private XcTaskRepository xcTaskRepository;

    /**
     * 查询任务列表
     *
     * @param updateTime {@link Date}
     * @param size       {@link int}
     * @return {@link List <  XcTask >}
     * @author Kang Yong
     * @date 2022/2/13
     */
    @Override
    public List<XcTask> findXcTaskList(Date updateTime, int size) {
        // 分页参数设置
        Pageable pageable = new PageRequest(0, size);
        // 查询
        Page<XcTask> xcTaskPageInfo = this.xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        List<XcTask> xcTaskList = xcTaskPageInfo.getContent();
        return xcTaskList;
    }

}
