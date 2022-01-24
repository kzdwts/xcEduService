package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 媒资信息
 *
 * @author Kang Yong
 * @date 2022/1/23
 * @since 1.0.0
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {

    /**
     * 从teachplanmedia 查询课程计划媒资信息
     *
     * @param courseId
     * @return
     */
    List<TeachplanMedia> findByCourseId(String courseId);
}
