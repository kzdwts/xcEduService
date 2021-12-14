package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * 课程营销，业务实现层
 *
 * @author Kang Yong
 * @date 2021/12/14
 * @since 1.0.0
 */
@Service
public class CourseMarketServiceImpl implements CourseMarketService {

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    /**
     * 根据课程id获取课程营销信息
     *
     * @param courseId
     * @return
     */
    @Override
    public CourseMarket getCourseMarketById(String courseId) {
        // 查询
        Optional<CourseMarket> marketOptional = this.courseMarketRepository.findById(courseId);
        if (marketOptional.isPresent()) {
            return marketOptional.get();
        }
        return null;
    }

    /**
     * 更新课程营销信息
     *
     * @param courseId     课程id
     * @param courseMarket 课程营销信息
     * @return
     */
    @Transactional
    @Override
    public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket) {
        // 先查询有没有
        CourseMarket existsCourseMarket = this.getCourseMarketById(courseId);
        if (ObjectUtils.isEmpty(existsCourseMarket)) {
            // 新增
            existsCourseMarket = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, existsCourseMarket);
            // 课程id
            existsCourseMarket.setId(courseId);
            this.courseMarketRepository.save(existsCourseMarket);
        } else {
            // 更新
            existsCourseMarket.setCharge(courseMarket.getCharge());
            existsCourseMarket.setStartTime(courseMarket.getStartTime());
            existsCourseMarket.setEndTime(courseMarket.getEndTime());
            existsCourseMarket.setPrice(courseMarket.getPrice());
            existsCourseMarket.setPrice_old(courseMarket.getPrice_old());
            existsCourseMarket.setQq(courseMarket.getQq());
            existsCourseMarket.setValid(courseMarket.getValid());
            this.courseMarketRepository.save(existsCourseMarket);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
