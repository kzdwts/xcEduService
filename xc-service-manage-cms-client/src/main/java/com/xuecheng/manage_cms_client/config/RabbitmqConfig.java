package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author Kang Yong
 * @date 2021/12/6
 * @since 1.0.0
 */
@Configuration
public class RabbitmqConfig {

    // 队列bean的名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    // 交换机的名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    // 队列的名称
    @Value("${xuecheng.mq.queue}")
    public String queue_cms_postpage_name;

    // routingKey 即站点id
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;

    // 交换机
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE() {
        // durable 标识持久化交换机
        return ExchangeBuilder.topicExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    // 声明队列 QUEUE_CMS_POSTPAGE
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue QUEUE_CMS_POSTPAGE() {
        return new Queue(queue_cms_postpage_name);
    }

    // QUEUE_CMS_POSTPAGE 绑定交换机
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue,
                                              @Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
