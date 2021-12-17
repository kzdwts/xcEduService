package com.xuecheng.file_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @author Kang Yong
 * @date 2021/12/16
 * @since 1.0.0
 */
@SpringBootApplication  // 扫描所在包及子包的bean，注入到ioc中
@EntityScan("com.xuecheng.framework.domain.filesystem") // 扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.xuecheng.framework"}) // 扫描framework中通用类
@ComponentScan(basePackages = {"com.xuecheng.file_system"}) // 扫描本项目下的所有类
public class BaseFileSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseFileSystemApplication.class, args);
    }

}
