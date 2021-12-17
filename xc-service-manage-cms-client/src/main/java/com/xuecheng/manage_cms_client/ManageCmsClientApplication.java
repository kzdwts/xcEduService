package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Kang Yong
 * @date 2021/12/6
 * @since 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan(basePackages = {"com.xuecheng.framework"})
@ComponentScan(basePackages = {"com.xuecheng.manage_cms_client"})
public class ManageCmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class, args);
    }
}
