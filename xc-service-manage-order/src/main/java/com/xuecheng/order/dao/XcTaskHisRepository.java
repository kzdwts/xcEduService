package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 历史任务
 *
 * @author Kang Yong
 * @date 2022/2/14
 * @since 1.0.0
 */
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis, String> {
}
