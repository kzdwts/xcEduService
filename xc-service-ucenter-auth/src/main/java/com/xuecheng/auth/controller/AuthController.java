package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

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

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    private Integer cookieMaxAge;

    @Value("${auth.tokenValiditySeconds}")
    private Integer tokenValiditySeconds;

    @Autowired
    private AuthService authService;

    /**
     * 登录
     *
     * @param loginRequest {@link LoginRequest}
     * @return {@link LoginResult}
     * @author Kang Yong
     * @date 2022/2/2
     */
    @PostMapping("/userlogin")
    @Override
    public LoginResult login(LoginRequest loginRequest) {
        // 非空校验，这些应该放到注解里面的，这里先这么写
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }

        // 校验密码是否输入
        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }

        AuthToken authToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword(), clientId, clientSecret);
        // 将令牌写入cookie
        // 访问token
        String access_token = authToken.getAccess_token();
        // 将令牌存储到cookie
        this.saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS, access_token);
    }

    /**
     * 将令牌保存到token
     *
     * @param token {@link String}
     * @author Kang Yong
     * @date 2022/2/5
     */
    private void saveCookie(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // 添加cookie认证令牌，最后一个参数设置为false，标识允许浏览器获取
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    /**
     * 退出登录
     *
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2022/2/2
     */
    @PostMapping("/userlogout")
    @Override
    public ResponseResult logout() {
        return null;
    }
}
