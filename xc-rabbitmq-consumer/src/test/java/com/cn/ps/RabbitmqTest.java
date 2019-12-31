package com.cn.ps;

import com.rabbitmq.client.*;

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
            //定义消息方法
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                /**
                 * 消费者接收消息调用此方法
                 * @param consumerTag 消费者的标签，在basicConsumer指定
                 * @param envelope 消息包内容，可以从中获取消息id，消息routingkey，交换机，消息重传标志（收到消息后是否需要重新发送）
                 * @param properties
                 * @param body
                 * @throws IOException
                 */
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(new String(body,"utf-8"));
                }
            };
            /**
             * 监听队列
             * param1：队列名称
             * param2：是否自动回复，设置为true表示自动向mq发送接收到了，mq接收到回复会自动删除消息，设置为fasle则需要手动回复
             * param3：消费消息的方法，消费者接收到消息后调用此方法
             */
            channel.basicConsume(QUEUE, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
