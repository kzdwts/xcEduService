package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 菜单
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
@Mapper
public interface XcMenuMapper {

    /**
     * 根据用户id查询用户权限信息
     *
     * @param userid {@link String}
     * @return {@link List< XcMenu>}
     * @author Kang Yong
     * @date 2022/2/9
     */
    List<XcMenu> selectPermissionByUserId(String userid);
}
