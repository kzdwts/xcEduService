package com.xuecheng.govern.gateway.service.impl;

import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.govern.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        // 读取cookie
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)) {
            return null;
        }
        return access_token;
    }

    /**
     * 从header中查询jwt令牌
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String}
     * @author Kang Yong
     * @date 2022/2/9
     */
    @Override
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            // 拒绝访问
            return null;
        }

        if (!authorization.startsWith("Bearer ")) {
            // 拒绝访问
            return null;
        }
        return authorization;
    }

    /**
     * 查询令牌的有效期
     * 由于令牌存储时采用String序列化策略，所以这里用StringRedisTemplate来查询，使用RedisTemplate无法完成查询
     *
     * @param access_token {@link String}
     * @return {@link Long}
     * @author Kang Yong
     * @date 2022/2/9
     */
    @Override
    public Long getExpire(String access_token) {
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
    }

}
