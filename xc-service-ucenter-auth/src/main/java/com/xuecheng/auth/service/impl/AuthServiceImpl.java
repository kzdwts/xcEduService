package com.xuecheng.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 认证服务业务实现层
 *
 * @author Kang Yong
 * @date 2022/2/4
 * @since 1.0.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${auth.tokenValiditySeconds}")
    private Integer tokenValiditySeconds;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录（认证方法）
     *
     * @param username     {@link String} 用户账号
     * @param password     {@link String} 密码
     * @param clientId     {@link String} 客户端id
     * @param clientSecret {@link String} 客户端秘钥
     * @return {@link AuthToken}
     * @author Kang Yong
     * @date 2022/2/5
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        // 申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if (authToken == null) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }

        // 将token存到redis
        String access_token = authToken.getAccess_token();
        String content = JSON.toJSONString(authToken);
        boolean saveTokenResult = this.saveToken(access_token, content, tokenValiditySeconds);
        if (!saveTokenResult) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }
        return authToken;
    }

    /**
     * 保存token到redis缓存
     *
     * @param access_token
     * @param content
     * @param tokenValiditySeconds
     * @return
     */
    private boolean saveToken(String access_token, String content, Integer tokenValiditySeconds) {
        // 令牌名称
        String name = "user_token:" + access_token;
        // 保存令牌到redis
        stringRedisTemplate.boundValueOps(name).set(content, tokenValiditySeconds, TimeUnit.SECONDS);
        // 获取过期时间
        Long expire = stringRedisTemplate.getExpire(name);
        return expire > 0;
    }

    /**
     * 申请令牌（认证方法）
     *
     * @param username     {@link String}
     * @param password     {@link String}
     * @param clientId     {@link String}
     * @param clientSecret {@link String}
     * @return {@link AuthToken}
     * @author Kang Yong
     * @date 2022/2/6
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        // 选中认证服务的地址
        return null;
    }

}
