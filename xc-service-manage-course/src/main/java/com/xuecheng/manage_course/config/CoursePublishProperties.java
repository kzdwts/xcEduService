package com.xuecheng.manage_course.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 页面发布配置项
 *
 * @author Kang Yong
 * @date 2021/12/27
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "course-publish")
@RefreshScope
@Data
public class CoursePublishProperties {

    private String siteId;
    private String templateId;
    private String previewUrl;
    private String pageWebPath;
    private String pagePhysicalPath;
    private String dataUrlPre;
}
