package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TeachplanRepository jpa
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
}
