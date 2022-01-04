package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * coursePub
 *
 * @author Kang Yong
 * @date 2022/1/4
 * @since 1.0.0
 */
public interface CoursePubRepository extends JpaRepository<CoursePub, String> {
}
