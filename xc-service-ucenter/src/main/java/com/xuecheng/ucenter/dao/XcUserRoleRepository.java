package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户角色
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
public interface XcUserRoleRepository extends JpaRepository<XcUserRole, String> {
}
