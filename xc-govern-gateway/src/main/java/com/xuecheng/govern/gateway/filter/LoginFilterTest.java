package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器测试
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
@Slf4j
@Component
public class LoginFilterTest extends ZuulFilter {

    @Override
    public String filterType() {
        log.info("===filterType");
        return "pre";
    }

    @Override
    public int filterOrder() {
        log.info("===filterOrder");
        // int值来定义过滤器的执行顺序，数值越小优先级越高
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        log.info("===shouldFilter");
        // 该过滤器需要执行
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("===run");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        HttpServletRequest request = requestContext.getRequest();

        // 取出头部信息Authorization
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            requestContext.setSendZuulResponse(false); // 拒绝访问
            requestContext.setResponseStatusCode(200); // 设置响应状态码

            ResponseResult unauthorization = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String jsonStr = JSON.toJSONString(unauthorization);
            requestContext.setResponseBody(jsonStr);
            requestContext.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }
        return null;
    }

}
