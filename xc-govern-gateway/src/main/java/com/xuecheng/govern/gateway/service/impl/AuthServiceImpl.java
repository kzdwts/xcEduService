package com.xuecheng.govern.gateway.service.impl;

import com.xuecheng.govern.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户认证
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询身份令牌
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String}
     * @author Kang Yong
     * @date 2022/2/9
     */
    @Override
    public String getTokenFromCookie(HttpServletRequest request) {
        return null;
    }
}
