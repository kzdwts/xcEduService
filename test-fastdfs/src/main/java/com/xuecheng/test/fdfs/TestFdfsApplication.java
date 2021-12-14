package com.xuecheng.test.fdfs;

import com.xuecheng.test.fdfs.config.FastDFSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 启动类
 *
 * @author Kang Yong
 * @date 2021/12/14
 * @since 1.0.0
 */
@SpringBootApplication
//@EnableConfigurationProperties({FastDFSProperties.class})
public class TestFdfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestFdfsApplication.class, args);
    }

}
