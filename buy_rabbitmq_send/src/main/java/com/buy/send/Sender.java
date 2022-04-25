package com.buy.send;

import com.buy.send.config.OrderTtlQueueConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String msg){
        //发送消息到过期队列中
        amqpTemplate.convertAndSend(OrderTtlQueueConfig.TOPIC_EXCHANGE ,"order.ttl" , msg);
    }

}
