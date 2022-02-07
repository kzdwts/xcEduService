package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

/**
 * 用户管理 接口定义
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据用户账号查询用户信息
     *
     * @param username {@link String}
     * @return {@link XcUser}
     * @author Kang Yong
     * @date 2022/2/7
     */
    XcUser findXcUserByUsername(String username);

    /**
     * 根据账号查询用户的信息，返回用户扩展信息
     *
     * @param username {@link String}
     * @return {@link XcUserExt}
     * @author Kang Yong
     * @date 2022/2/7
     */
    XcUserExt getUserExt(String username);
}
