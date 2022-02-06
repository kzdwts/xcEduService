package com.xuecheng.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        if (null == serviceInstance) {
            log.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }

        // 获取令牌的url
        String path = serviceInstance.getUri().toString() + "/auth/oauth/token";
        // 定义body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        // 授权方式
        formData.add("grant_type", "password");
        // 账号 密码
        formData.add("username", username);
        formData.add("password", password);
        // 定义头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", httpbasic(clientId, clientSecret));
        // 指定RestTemplate 当遇到400或401响应的时候也不要抛出异常，也要正常返回
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        Map map = null;
        try {
            ResponseEntity<Map> mapResponseEntity = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(formData, header), Map.class);
            map = mapResponseEntity.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error("request oauth_token_password error:{}", e.getMessage());
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }

        if (map == null
                || map.get("access_token") == null
                || map.get("refresh_token") == null
                || map.get("jti") == null) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }

        AuthToken authToken = new AuthToken();
        String jwt_token = (String) map.get("access_token");
        String refresh_token = (String) map.get("refresh_token");
        String access_token = (String) map.get("jti");
        authToken.setJwt_token(jwt_token);
        authToken.setRefresh_token(refresh_token);
        authToken.setAccess_token(access_token);

        return authToken;
    }

    /**
     * 获取httpbasic认证串
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret) {
        String str = clientId + ":" + clientSecret;
        // 进行base64编码
        byte[] encode = Base64.encode(str.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encode);
    }

}
