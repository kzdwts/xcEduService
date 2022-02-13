package com.xuecheng.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 订单工程启动类
 *
 * @author Kang Yong
 * @date 2022/2/12
 * @since 1.0.0
 */
@EnableScheduling // 开启任务调度
@SpringBootApplication
@EntityScan(value = {"com.xuecheng.framework.domain.order", "com.xuecheng.framework.domain.task"})//扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages = {"com.xuecheng.order"})
@ComponentScan(basePackages = {"com.xuecheng.framework"})//扫描common下的所有类
public class ManageOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageOrderApplication.class, args);
    }

}
