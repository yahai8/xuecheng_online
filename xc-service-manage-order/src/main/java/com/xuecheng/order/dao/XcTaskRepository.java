package com.xuecheng.order.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @author: MuYaHai
 * Date: 2019/12/21, Time: 20:06
 */
public interface XcTaskRepository extends JpaRepository<XcTask, String> {

    //取出指定时间之前的任务
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

/*    //更新任务处理时间
    @Modifying
    @Query("update XcTask set XcTask .updateTime= :updateTime where XcTask .id= :id")
    public int updateTaskTime(@Param("updateTime") Date updateTime, @Param("id") String id);*/

/*    //更新版本
    @Modifying
    @Query("update XcTask set XcTask.version = :version+1 where XcTask.version = :version and XcTask.id = :id")
    public int updateTaskVersion(@Param(value = "version") int version, @Param(value = "id") String id);*/

    @Modifying
    @Query("update XcTask t set t.version = :version+1  where t.id = :id and t.version = :version")
    public int updateTaskVersion(@Param(value = "id") String id, @Param(value = "version") int version);
}
