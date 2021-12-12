package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TeachplanRepository jpa
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {

    /**
     * 根据课程id和父节点id查询查询出节点列表
     *
     * @param courseId
     * @param parentId
     * @return
     */
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
