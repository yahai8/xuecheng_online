package com.cn.ps;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: MuYaHai
 * Date: 2019/11/27, Time: 14:25
 */
public class Consumer01_sms {
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "inform_queue_sms";
    private static final String EXCHANGE_FANOUT_INFORM="exchange_fanout_inform";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setPort(5672);
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            /**
             * param1:队列名称
             * param2:持久化
             * param3:是否独占本次链接
             * param4:是否在不使用时自动删除
             * param5:其他参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            //绑定队列
            /**
             * param1:队列名称
             * param2:交换机名称
             * param3:路由key
             */
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
            //监听队列
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("特别行动小队发来消息："+new String(body,"utf-8"));
                }
            };
            channel.basicConsume(QUEUE_INFORM_EMAIL, true, consumer);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
