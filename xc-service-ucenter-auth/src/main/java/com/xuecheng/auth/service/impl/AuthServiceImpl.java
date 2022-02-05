package com.xuecheng.auth.service.impl;

import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
     * 用户登录
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
        return null;
    }

}
