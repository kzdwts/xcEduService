package com.xuecheng.manage_media.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 教学计划
 *
 * @author Kang Yong
 * @date 2022/1/23
 * @since 1.0.0
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
}
