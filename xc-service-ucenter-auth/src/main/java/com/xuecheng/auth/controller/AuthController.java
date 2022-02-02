package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证控制层
 *
 * @author Kang Yong
 * @date 2022/2/2
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerApi {

    /**
     * 登录
     *
     * @param loginRequest {@link LoginRequest}
     * @return {@link LoginResult}
     * @author Kang Yong
     * @date 2022/2/2
     */
    @Override
    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }

    /**
     * 退出登录
     *
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2022/2/2
     */
    @Override
    public ResponseResult logout() {
        return null;
    }
}
