package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户管理 持久层
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
public interface XcUserRepository extends JpaRepository<XcUser, String> {

    /**
     * 根据username查询用户信息
     *
     * @param username {@link String}
     * @return {@link XcUser}
     * @author Kang Yong
     * @date 2022/2/7
     */
    XcUser findXcUserByUsername(String username);
}
