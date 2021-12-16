package com.xuecheng.file_system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kang Yong
 * @date 2021/12/14
 * @since 1.0.0
 */
//@ConfigurationProperties(prefix = "fastdfs") 加载配置文件有多种方式，这种方式需要在启动类加注解
// 第二种加载配置文件，不用再启动类加注解，是把这个类加载到spring容器

@Data
@Component
@ConfigurationProperties(prefix = "xuecheng.fastdfs")
public class FastDFSProperties {

    /**
     * http连接超时时间
     */
    private Integer connectTimeoutInSeconds;

    /**
     * tracker与storage网络通信超时时间
     */
    private Integer networkTimeoutInSeconds;

    /**
     * 字符编码
     */
    private String charset;

    /**
     * tracker服务器地址，多个地址中间使用英文逗号隔开
     */
    private String trackerServers;


}
