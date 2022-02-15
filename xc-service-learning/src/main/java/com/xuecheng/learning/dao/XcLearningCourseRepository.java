package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户选课
 *
 * @author Kang Yong
 * @date 2022/2/14
 * @since 1.0.0
 */
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse, String> {

    /**
     * 根据用户id和课程id查询选课记录
     *
     * @param userId   {@link String} 用户id
     * @param courseId {@link String} 课程id
     * @return {@link XcLearningCourse}
     * @author Kang Yong
     * @date 2022/2/14
     */
    XcLearningCourse findByUserIdAndCourseId(String userId, String courseId);
}
