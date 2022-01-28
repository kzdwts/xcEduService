package com.xuecheng.learning.client;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 搜索服务 调用
 *
 * @author Kang Yong
 * @date 2022/1/28
 * @since 1.0.0
 */
@FeignClient(value = "xc-service-search")
public interface CourseSearchClient {

    @GetMapping(value = "/getmedia/{teachplanId}")
    TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
