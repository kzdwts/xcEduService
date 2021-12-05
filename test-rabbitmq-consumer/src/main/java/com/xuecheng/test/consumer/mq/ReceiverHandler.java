package com.xuecheng.test.consumer.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.xuecheng.test.consumer.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.crypto.spec.PSource;

/**
 * 接受mq消息
 *
 * @author Kang Yong
 * @date 2021/12/5
 * @since 1.0.0
 */
@Component
public class ReceiverHandler {

    /**
     * 接受mq邮件消息
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void receiverEmail(String msg, Message message, Channel channel) {
        System.out.println("======接收到消息：" + msg);
    }

    /**
     * 接受mq短信消息
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receiverSms(String msg, Message message, Channel channel) {
        System.out.println("======接收到消息：" + msg);
    }


}
