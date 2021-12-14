package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Kang Yong
 * @date 2021/12/14
 * @since 1.0.0
 */
public interface CourseMarketRepository extends JpaRepository<CourseMarket, String> {
}
