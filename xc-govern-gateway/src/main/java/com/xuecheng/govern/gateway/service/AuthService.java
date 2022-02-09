package com.xuecheng.govern.gateway.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户认证
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 查询身份令牌
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String}
     * @author Kang Yong
     * @date 2022/2/9
     */
    String getTokenFromCookie(HttpServletRequest request);
}
