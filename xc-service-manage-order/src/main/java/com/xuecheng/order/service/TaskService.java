package com.xuecheng.order.service;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: MuYaHai
 * Date: 2019/12/21, Time: 20:09
 */
@Service
public class TaskService {
    @Autowired
    XcTaskRepository xcTaskRepository;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public List<XcTask> findTaskList(Date update, int n) {
        //设置分页参数，取出前n条
        PageRequest pageRequest = new PageRequest(0, n);
        Page<XcTask> page = xcTaskRepository.findByUpdateTimeBefore(pageRequest, update);
        List<XcTask> result = page.getResult();
        return result;
    }


    /**
     * 发送消息
     * @param xcTask
     * @param ex 交换机
     * @param routingKey 路由key
     */
    @Transactional
    public void publish(XcTask xcTask, String ex, String routingKey) {
        //查询任务
        Optional<XcTask> optionalXcTask = xcTaskRepository.findById(xcTask.getId());
        if (optionalXcTask.isPresent()) {
            XcTask one = optionalXcTask.get();
            rabbitTemplate.convertAndSend(ex, routingKey, one);
            //更新任务时间为当前时间
            one.setUpdateTime(new Date());
            xcTaskRepository.save(one);
        }
    }


    //查询任务，是否需要操作，通过版本号来添加
    @Transactional
    public int getTask(String taskId, int version) {
        return xcTaskRepository.updateTaskVersion(taskId,version);
    }

    //删除任务
    @Transactional
    public void finishTask(String taskId) {
        Optional<XcTask> optional = xcTaskRepository.findById(taskId);
        //如果存在就删除任务，添加历史任务
        if (optional.isPresent()) {
            XcTask xcTask = optional.get();
            xcTask.setDeleteTime(new Date());
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);
        }
    }
}
