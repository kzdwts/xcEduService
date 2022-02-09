package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
public interface XcPermissionRepository extends JpaRepository<XcPermission, String> {
}
