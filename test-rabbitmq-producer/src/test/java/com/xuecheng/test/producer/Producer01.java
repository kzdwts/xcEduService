package com.xuecheng.test.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 *
 * @author Kang Yong
 * @date 2021/11/30
 * @since 1.0.0
 */
public class Producer01 {

    // 对列
    public static final String QUEUE = "helloworld";

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

            /**
             *声明对列
             * 1、queue 对列名称
             * 2、durable 是否辞旧华，如果持久化，mq重启后对列还在
             * 3、exclusive 是否独占链接，对列只允许在改链接中访问，如果connection链接关闭则自动删除
             * 4、autoDelete 自动删除，对列不再使用时是否自动删除此对列
             * 5、arguments 参数，可以设置一个对列的扩展参数，比如：可设置活动时间
             */
            channel.queueDeclare(QUEUE, true, false, true, null);
            /**
             *发送消息
             * 1、exchange， 交换机，如果不指定将使用mq默认交换机
             * 2、routingKey，路由key，交换机根据路由key来将消息转发到指定的对列如果使用默认交换机，设置为对列的名称
             * 3、props 消息的属性
             * 4、body，消息内容
             */
            String msg = "hello world 黑马程序员,kangyong";
            channel.basicPublish("", QUEUE, null, msg.getBytes());
            System.out.println("send mq message " + msg);
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
