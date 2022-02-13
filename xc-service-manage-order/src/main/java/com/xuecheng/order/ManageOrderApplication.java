package com.xuecheng.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class ManageOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageOrderApplication.class, args);
    }

}
