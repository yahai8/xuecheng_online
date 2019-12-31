package com.xuecheng.rabbitmq.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author: MuYaHai
 * Date: 2019/11/27, Time: 20:23
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_topice_springboot {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        for (int i = 0; i < 5; i++) {
            String message = "收到请回答！";
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.sm.email", message);
        }
    }
}
