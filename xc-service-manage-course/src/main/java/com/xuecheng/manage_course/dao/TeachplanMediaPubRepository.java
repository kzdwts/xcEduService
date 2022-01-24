package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 课程媒资发布信息
 *
 * @author Kang Yong
 * @date 2022/1/24
 * @since 1.0.0
 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub, String> {

    /**
     * 根据课程id删除课程计划媒资信息
     *
     * @param courseId
     * @return
     */
    long deleteByCourseId(String courseId);

}
