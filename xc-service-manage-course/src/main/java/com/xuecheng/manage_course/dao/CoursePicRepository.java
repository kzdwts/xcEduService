package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 课程图片管理
 *
 * @author Kang Yong
 * @date 2021/12/17
 * @since 1.0.0
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {

    /**
     * 根据课程id删除课程图片信息
     *
     * @param courseId {@link String}
     * @return {@link long}
     * @author Kang Yong
     * @date 2021/12/17
     */
    long deleteByCourseid(String courseId);

}
