package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.portalview.ViewCourse;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.service.CourseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 课程视图
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
@Service
public class CourseViewServiceImpl implements CourseViewService {

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private TeachplanMapper teachplanMapper;

    /**
     * 查询课程视图数据
     *
     * @param courseId 课程id
     * @return
     */
    @Override
    public ViewCourse courseView(String courseId) {
        ViewCourse viewCourse = new ViewCourse();

        // 1、课程基本信息
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            viewCourse.setCourseBase(courseBase);
        }

        // 2、课程营销信息
        Optional<CourseMarket> courseMarketOptional = this.courseMarketRepository.findById(courseId);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            viewCourse.setCourseMarket(courseMarket);
        }

        // 3、课程图片信息
        Optional<CoursePic> coursePicOptional = this.coursePicRepository.findById(courseId);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            viewCourse.setCoursePic(coursePic);
        }

        // 4、课程计划信息
        TeachplanNode teachplanNode = this.teachplanMapper.selectList(courseId);
        viewCourse.setTeachplan(teachplanNode);

        return viewCourse;
    }
}
