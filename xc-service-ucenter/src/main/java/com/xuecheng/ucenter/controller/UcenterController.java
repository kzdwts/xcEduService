package com.xuecheng.ucenter.controller;

import com.xuecheng.api.ucenter.UcenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理 控制层
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {

    @Autowired
    private UserService userService;

    /**
     * 根据账号查询用户信息
     *
     * @param username {@link String}
     * @return {@link XcUserExt}
     * @author Kang Yong
     * @date 2022/2/7
     */
    @Override
    @GetMapping("/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username) {
        XcUserExt userExt = this.userService.getUserExt(username);
        return userExt;
    }
}
