package com.xuecheng.manage_cms;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 20:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqTest {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void test() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageId", "收到请回答！");
        String msg = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,"5a751fab6abb5044e0d19ea1",msg);
    }

}
