package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 所属企业
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser, String> {

    /**
     * 根据用户id查询所属企业id
     *
     * @param userId {@link String}
     * @return {@link XcCompanyUser}
     * @author Kang Yong
     * @date 2022/2/7
     */
    XcCompanyUser findByUserId(String userId);

}
