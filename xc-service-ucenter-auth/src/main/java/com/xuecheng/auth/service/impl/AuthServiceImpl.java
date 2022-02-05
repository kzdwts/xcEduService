package com.xuecheng.auth.service.impl;

import com.xuecheng.auth.service.AuthService;
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

    // 认证方法


}
