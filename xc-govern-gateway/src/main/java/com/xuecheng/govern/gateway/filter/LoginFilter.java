package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 网关拦截器
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
@Slf4j
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;

    @Override
    public String filterType() {
        log.info("=== filterType正在执行===");
        // 四种类型：pre、routing、post、error
        return "pre";
    }

    @Override
    public int filterOrder() {
        log.info("=== filterOrder正在执行===");
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        log.info("=== shouldFilter正在执行===");
        // 该过滤器需要执行，
        return true;

        // 不执行过滤器，设置false，run方法就不执行了
//        return false;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("=== run正在执行===");
        // 上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 请求对象
        HttpServletRequest request = requestContext.getRequest();
        // 查询身份令牌
        String access_token = this.authService.getTokenFromCookie(request);
        if (access_token == null) {
            // 拒绝访问
            log.error("======获取cookie token 异常");
            this.accessDenied();
            return null;
        }

        // 校验令牌是否过期
        Long expire = this.authService.getExpire(access_token);
        if (expire <= 0) {
            // 拒绝访问
            log.error("======token已过期");
            this.accessDenied();
            return null;
        }

        // 查询jwt令牌
        String jwt = this.authService.getJwtFromHeader(request);
        if (jwt == null) {
            // 拒绝访问
            log.error("======获取header token异常");
            this.accessDenied();
            return null;
        }
        return null;
    }

    /**
     * 拒绝访问
     */
    private void accessDenied() {
        // 上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 拒绝访问
        requestContext.setSendZuulResponse(false);

        // 设置响应内容
        ResponseResult unauthorization = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonStr = JSON.toJSONString(unauthorization);
        requestContext.setResponseBody(jsonStr);
        requestContext.getResponse().setContentType("application/json;charset=UTF-8");
        // 状态码
        requestContext.setResponseStatusCode(200);
    }
}
