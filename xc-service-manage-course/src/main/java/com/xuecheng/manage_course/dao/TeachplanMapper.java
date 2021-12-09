package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程计划mapper
 *
 * @author Kang Yong
 * @date 2021/12/9
 * @since 1.0.0
 */
@Mapper
public interface TeachplanMapper {

    /**
     * 查询课程计划
     *
     * @param courseId {@link String}
     * @return {@link TeachplanNode}
     * @author Kang Yong
     * @date 2021/12/9
     */
    TeachplanNode selectList(String courseId);

}
