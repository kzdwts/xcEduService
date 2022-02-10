package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器
 *
 * @author Kang Yong
 * @date 2022/2/10
 * @since 1.0.0
 */
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("===common feign拦截器执行===START===");

        try {
            // 使用RequestContextHolder工具获取request相关变量
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                // 取出request
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        if (name.equals("authorization")) {
                            requestTemplate.header(name, values);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("===common feign拦截器执行===异常：{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
