package com.xuecheng.auth.service;

import com.xuecheng.framework.domain.ucenter.ext.AuthToken;

/**
 * 认证服务业务接口定义
 *
 * @author Kang Yong
 * @date 2022/2/4
 * @since 1.0.0
 */
public interface AuthService {

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
    AuthToken login(String username, String password, String clientId, String clientSecret);
}
