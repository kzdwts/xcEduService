package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心启动类
 *
 * @author Kang Yong
 * @date 2021/12/17
 * @since 1.0.0
 */
@EnableEurekaServer // 标识次工程是一个EurekaServer
@SpringBootApplication
public class GovernCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernCenterApplication.class, args);
    }

}
