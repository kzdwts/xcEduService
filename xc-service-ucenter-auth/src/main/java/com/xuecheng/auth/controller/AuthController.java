package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户认证控制层
 *
 * @author Kang Yong
 * @date 2022/2/2
 * @since 1.0.0
 */
@Slf4j
@RestController
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
        log.info("===登录成功");
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
        // 取出身份令牌
        String uid = this.getTokenFormCookie();
        // 删除redis中的token
        this.authService.delToken(uid);
        // 清除cookie
        this.clearCookie(uid);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 清除cookie
     * @param token
     */
    private void clearCookie(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
        log.info("===清除cookie成功");
    }

    /**
     * 查询userJwt令牌
     *
     * @return {@link JwtResult}
     * @author Kang Yong
     * @date 2022/2/7
     */
    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt() {
        // 获取cookie中的令牌
        String uid = this.getTokenFormCookie();
        if (uid == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }

        // 用身份令牌从redis查询jwt信息
        AuthToken authToken = this.authService.getUserToken(uid);
        if (authToken == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        return new JwtResult(CommonCode.SUCCESS, authToken.getJwt_token());
    }

    /**
     * 从cookie中读取访问令牌
     *
     * @return
     */
    private String getTokenFormCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if (map != null && map.get("uid") != null) {
            String uid = map.get("uid");
            return uid;
        }
        return null;
    }
}
