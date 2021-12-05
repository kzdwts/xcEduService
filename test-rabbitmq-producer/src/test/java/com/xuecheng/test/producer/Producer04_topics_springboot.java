package com.xuecheng.test.producer;

import com.xuecheng.test.producer.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 生产者
 *
 * @author Kang Yong
 * @date 2021/11/30
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer04_topics_springboot {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendTopicEmailMsg() {
        String msg = "send a email message";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.email", msg);
        System.out.println("发送结束======email===");
    }

    @Test
    public void testSendTopicSmsMsg() {
        String msg = "send a sms message, like you";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.sms", msg);
        System.out.println("发送结束======sms===");
    }

}
