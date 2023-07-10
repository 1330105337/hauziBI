package com.yupi.springbootinit.biMq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/*
生产者代码
 */
@Component
public class MyMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public  void sendMessage(String exchange,String routingKey,String message) {
        //将信息还有路由key发送到交换机
        rabbitTemplate.convertAndSend(exchange,routingKey,message);

    }


}
