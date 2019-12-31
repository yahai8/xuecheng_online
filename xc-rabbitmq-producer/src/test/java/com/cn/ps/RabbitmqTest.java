package com.cn.ps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: MuYaHai
 * Date: 2019/11/27, Time: 10:47
 */
public class RabbitmqTest {
    public static final String QUEUE = "hello world";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");

            //创建rabbitMQ与tcp的连接
            connection = factory.newConnection();
            //创建信道
            channel = connection.createChannel();
            /**
             * 声明队列
             * param1:队列名称
             * param2:是否持久化，durable
             * param3:是否独占本次连接
             * param4:队列没有使用时自动删除
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false,null);
            //定义消息
            String message = "呼叫基地，收到请回答！";
            /**
             * 发送消息
             * param1:交换机名称
             * param2:routing key ,消息的路由key，是用于交换机将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4:消息体
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
