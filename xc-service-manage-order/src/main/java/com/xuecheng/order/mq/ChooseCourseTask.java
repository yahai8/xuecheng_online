package com.xuecheng.order.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.plugin2.message.Message;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author: MuYaHai
 * Date: 2019/12/21, Time: 20:13
 */
@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService taskService;

    //监听添加完成后的消息
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE})
    public void receiveFinishChoosecourseTask(XcTask xcTask, Message message, Channel channel) {
        LOGGER.info("receiveChoosecourseTask...{}",xcTask.getId());
        //接收到消息id
        String id = xcTask.getId();
        //删除任务，添加历史任务
        taskService.finishTask(id);
    }


    //每隔一分钟扫描，并向mq发送消息
    @Scheduled(fixedDelay = 60000)
    public void sendChoosecourseTask() {
        //取出当前时间的前一分钟
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -1);
        Date time = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(time, 1000);

        //遍历任务列表
        for (XcTask xcTask : taskList) {
            String id = xcTask.getId();
            Integer version = xcTask.getVersion();
            if (taskService.getTask(id, version) > 0) {
                //发送消息
                taskService.publish(xcTask, xcTask.getMqExchange(), xcTask.getMqRoutingkey());
                LOGGER.info("send choose course task id:{}", xcTask.getId());
            }
        }
    }

}
