package com.xuecheng.framework.domain.portalview;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.report.ReportCourse;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by admin on 2018/2/27.
 */
@Data
@ToString
@Document(collection = "view_course")
public class ViewCourse implements Serializable {

    @Id
    private String id;

    /**
     * 基础信息
     */
    private CourseBase courseBase;

    /**
     * 课程营销
     */
    private CourseMarket courseMarket;

    /**
     * 课程图片
     */
    private CoursePic coursePic;

    /**
     * 教学计划
     */
    private TeachplanNode teachplan;
    //课程统计信息
    private ReportCourse reportCourse;

}
