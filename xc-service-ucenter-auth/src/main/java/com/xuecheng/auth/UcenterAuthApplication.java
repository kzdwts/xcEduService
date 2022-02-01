package com.xuecheng.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 鉴权中心启动类
 *
 * @author Kang Yong
 * @date 2022/1/30
 * @since 1.0.0
 */

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.auth")//扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages = {"com.xuecheng.auth"})
@ComponentScan(basePackages = {"com.xuecheng.framework"})//扫描common下的所有类
public class UcenterAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcenterAuthApplication.class, args);
    }

}
