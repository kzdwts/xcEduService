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
public class Producer02_publish {

    // 对列
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

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
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            // 声明队列
            // 1、队列名称
            // 2、是否持久化
            // 3、是否独占此队列
            // 4、队列不用是否自动删除
            // 5、参数
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            // 交换机和队列绑定
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");

            // 发送消息
            for (int i = 0; i < 5; i++) {
                String msg = "inform to user " + i;
                channel.basicPublish(EXCHANGE_FANOUT_INFORM, "", null, msg.getBytes(StandardCharsets.UTF_8));
                System.out.println("send mq message " + msg);
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
