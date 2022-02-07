package com.xuecheng.ucenter.service.impl;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import com.xuecheng.ucenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户管理
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private XcUserRepository userRepository;

    @Autowired
    private XcCompanyUserRepository companyUserRepository;

    /**
     * 根据用户账号查询用户信息
     *
     * @param username {@link String}
     * @return {@link XcUser}
     * @author Kang Yong
     * @date 2022/2/7
     */
    @Override
    public XcUser findXcUserByUsername(String username) {
        return this.userRepository.findXcUserByUsername(username);
    }

    /**
     * 根据账号查询用户的信息，返回用户扩展信息
     *
     * @param username {@link String}
     * @return {@link XcUserExt}
     * @author Kang Yong
     * @date 2022/2/7
     */
    @Override
    public XcUserExt getUserExt(String username) {
        XcUser xcUser = this.findXcUserByUsername(username);
        if (xcUser == null) {
            return null;
        }

        // 封装扩展信息
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);

        // 查询用户归属公司信息
        XcCompanyUser xcCompanyUser = this.companyUserRepository.findByUserId(xcUser.getId());
        if (xcCompanyUser != null) {
            xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
        }
        return xcUserExt;
    }
}
