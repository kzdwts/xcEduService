package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 菜单
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
public interface XcMenuRepository extends JpaRepository<XcMenu, String> {
}
