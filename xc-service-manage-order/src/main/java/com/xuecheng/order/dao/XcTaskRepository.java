package com.xuecheng.order.dao;

import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 修改任务的更新时间
     *
     * @param taskId     {@link String} 任务id
     * @param updateTIme {@link Date} 更新时间
     * @return {@link int}
     * @author Kang Yong
     * @date 2022/2/13
     */
    @Modifying
    @Query("update XcTask xt set xt.updateTime=:updateTime where xt.id=:taskId")
    int updateTaskTime(@Param("taskId") String taskId, @Param("updateTime") Date updateTIme);
}
