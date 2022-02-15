package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.LearningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 选课任务
 *
 * @author Kang Yong
 * @date 2022/2/15
 * @since 1.0.0
 */
@Slf4j
@Component
public class ChooseCourseTask {

    @Autowired
    private LearningService learningService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 接收选课任务
     *
     * @param xcTask  {@link XcTask} 任务内容
     * @param message {@link Message} 消息
     * @param channel {@link Channel} 渠道
     * @author Kang Yong
     * @date 2022/2/15
     */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE})
    public void receiveChooseCourseTask(XcTask xcTask, Message message, Channel channel) {
        log.info("接收到添加选课任务， taskId:{}", xcTask.getId());
        // 接收到的消息id
        String taskId = xcTask.getId();
        // 添加选课
        try {
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            if (map.get("startTime") != null) {
                startTime = sdf.parse((String) map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                endTime = sdf.parse((String) map.get("endTime"));
            }

            // 添加选课
            ResponseResult addcourse = this.learningService.addcourse(userId, courseId, valid, startTime, endTime, xcTask);
            if (addcourse.isSuccess()) {
                // 选课成功发送响应消息
                this.rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE, RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE, xcTask);

                log.info("成功发送选课成功消息，taskId:{}", taskId);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
