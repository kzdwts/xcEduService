package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
public interface XcTeacherRepository extends JpaRepository<XcTeacher, String> {
}
