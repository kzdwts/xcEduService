package com.xuecheng.order.service.impl;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import com.xuecheng.order.service.XcTaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    /**
     * 发送mq消息
     *
     * @param xcTask     {@link XcTask} 任务内容
     * @param ex         {@link String} 交换机
     * @param routingKey {@link String}
     * @author Kang Yong
     * @date 2022/2/13
     */
    @Override
    public void publish(XcTask xcTask, String ex, String routingKey) {
        Optional<XcTask> xcTaskOptional = this.xcTaskRepository.findById(xcTask.getId());
        if (xcTaskOptional.isPresent()) {
            // 发送消息
            this.rabbitTemplate.convertAndSend(ex, routingKey, xcTask);

            // 更新时间
            XcTask one = xcTaskOptional.get();
            one.setUpdateTime(new Date());
            this.xcTaskRepository.save(one);
        }
    }

    /**
     * 获取任务（乐观锁：更新成功即获取到任务）
     *
     * @param taskId  {@link String} 任务id
     * @param version {@link Integer} 版本号
     * @return {@link int}
     * @author Kang Yong
     * @date 2022/2/14
     */
    @Transactional
    @Override
    public int getTask(String taskId, Integer version) {
        return this.xcTaskRepository.updateTaskVersion(taskId, version);
    }

    /**
     * 完成任务后将任务删除
     *
     * @param taskId {@link String}
     * @author Kang Yong
     * @date 2022/2/15
     */
    @Transactional
    @Override
    public void finishTask(String taskId) {
        Optional<XcTask> taskOptional = this.xcTaskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            XcTask xcTask = taskOptional.get();
            xcTask.setDeleteTime(new Date());

            // 历史任务
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, xcTaskHis);

            // 保存任务记录
            this.xcTaskHisRepository.save(xcTaskHis);
            // 删除任务
            this.xcTaskRepository.delete(xcTask);
        }


    }

}
