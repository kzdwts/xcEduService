package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 *
 * @author Kang Yong
 * @date 2021/11/30
 * @since 1.0.0
 */
public class Producer03_topics {

    // 对列
    public static final String ROUTING_KEY_EMAIL = "routing_key_email";
    public static final String ROUTING_KEY_SMS = "routing_key_sms";
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    public static void main(String[] args) {
        // 通过工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.100.132");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("kangyong");
        connectionFactory.setPassword("kangyong");

        // 设置一个雪泥机，一个mq服务可以设置多个虚拟机，每个虚拟机相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        // 建立新连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            /*
            声明交换机
            1、交换机名称
            2、交换机类型，fanout、topic、direct、headers
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            // 声明队列
            // 1、队列名称
            // 2、是否持久化
            // 3、是否独占此队列
            // 4、队列不用是否自动删除
            // 5、参数
            channel.queueDeclare(ROUTING_KEY_EMAIL, true, false, false, null);
            channel.queueDeclare(ROUTING_KEY_SMS, true, false, false, null);

            // 交换机和队列绑定
            channel.queueBind(ROUTING_KEY_EMAIL, EXCHANGE_TOPICS_INFORM, "");
            channel.queueBind(ROUTING_KEY_SMS, EXCHANGE_TOPICS_INFORM, "");

            // 发送邮件消息
            for (int i = 0; i < 5; i++) {
                String msg = "email info to user " + i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.email", null, msg.getBytes(StandardCharsets.UTF_8));
                System.out.println("send mq email message: " + msg);
            }

            // 发送短信消息
            for (int i = 0; i < 5; i++) {
                String msg = "sms info to user " + i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms", null, msg.getBytes(StandardCharsets.UTF_8));
                System.out.println("send mq sms message: " + msg);
            }

            // 发送邮件、短信消息
            for (int i = 0; i < 5; i++) {
                String msg = "email and sms info to user " + i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms.email", null, msg.getBytes(StandardCharsets.UTF_8));
                System.out.println("send mq email、sms message: " + msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
