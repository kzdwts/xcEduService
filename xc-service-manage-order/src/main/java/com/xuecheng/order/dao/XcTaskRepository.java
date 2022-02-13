package com.xuecheng.order.dao;

import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * 任务dao
 *
 * @author Kang Yong
 * @date 2022/2/13
 * @since 1.0.0
 */
public interface XcTaskRepository extends JpaRepository<XcTask, String> {

    /**
     * 查询更新时间之前的数据（分页）
     *
     * @param pageable   {@link Pageable} 分页条件
     * @param updateTime {@link Date} 更新时间
     * @return {@link PageInfo< List< XcTask>>}
     * @author Kang Yong
     * @date 2022/2/13
     */
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);
}
