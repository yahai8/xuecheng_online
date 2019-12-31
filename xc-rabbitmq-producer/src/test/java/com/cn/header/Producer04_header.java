package com.cn.header;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author: MuYaHai
 * Date: 2019/11/27, Time: 15:48
 */
public class Producer04_header {
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_header_inform";

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
            HashMap<String, Object> header_mail = new HashMap<>();
            header_mail.put("inform_type", "email");
            HashMap<String, Object> header_sms = new HashMap<>();
            header_sms.put("inform_type", "sms");
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, header_sms);
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, header_mail);
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.HEADERS);

            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPICS_INFORM, "", header_mail);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_TOPICS_INFORM, "", header_sms);

            for (int i = 0; i < 10; i++) {
                String message = "MayDay mayDay 呼叫呼叫！";
                HashMap<String, Object> headers = new HashMap<>();
                headers.put("inform_type", "sms");
                AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                builder.headers(headers);
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "", builder.build(), message.getBytes());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
