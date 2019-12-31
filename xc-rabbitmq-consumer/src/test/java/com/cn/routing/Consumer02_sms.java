package com.cn.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: MuYaHai
 * Date: 2019/11/27, Time: 14:57
 */
public class Consumer02_sms {
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM ="exchange_routing_inform";

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
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_SMS);
            //监听
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                /**
                 * 消费者消费此消息调用
                 * @param consumerTag 消费者的标签channel.basicConsumer指定
                 * @param envelope 消息包内容，可以从中获取消息id，routingkey，消息重传标志
                 * @param properties
                 * @param body 消息体正文内容
                 * @throws IOException
                 */
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("来自银行的消息："+new String(body,"utf-8"));
                }
            };
            channel.basicConsume(QUEUE_INFORM_SMS, true, consumer);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
