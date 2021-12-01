package com.xuecheng.test.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 入门程序 消费者
 *
 * @author Kang Yong
 * @date 2021/11/30
 * @since 1.0.0
 */
public class Consumer01_publish_sms {

    // 对列
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";


    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.100.132");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("kangyong");
        connectionFactory.setPassword("kangyong");

        // 设置一个雪泥机，一个mq服务可以设置多个虚拟机，每个虚拟机相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        // 建立连接
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
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
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            // 交换机和队列绑定
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");

            // 实现消费方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                /**
                 * 当接收到消息后，此方法将被调用
                 * @param consumerTag 消费者标签，用来标识消费者的
                 * @param envelope 信封，通过envelope
                 * @param properties 消息属性
                 * @param body 消息内容
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    String msg = new String(body, "utf-8");
                    System.out.println("接收到消息" + msg);
                }
            };

            /**
             *监听对列
             * 1、Queue 对列名称
             * 2、autoAck 自动回复，当消费者接收到消息后要告诉mq消息已接收，如果将此参数设置为true表示会自动回复mq，如果设置为false，表示要编程实现回复
             * 3、callback， 消费方法，当消费者接收到消息要执行的方法
             */
            channel.basicConsume(QUEUE_INFORM_SMS, true, defaultConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
