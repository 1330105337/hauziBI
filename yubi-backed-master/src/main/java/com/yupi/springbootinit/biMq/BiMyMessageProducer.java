package com.yupi.springbootinit.biMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/*
生产者代码
 */
@Component
@Slf4j
public class BiMyMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    private static final  String IP="8.130.131.26";

    public  void sendMessage(String message) {
        try {
            log.info("程序执行了.....");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(IP);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(BiMqConstant.BI_EXCHANGE_NAME,"direct");
            //创建队列，随机分配一个队列名称
            channel.queueDeclare(BiMqConstant.BI_QUEUE_NAME,true,false,false,null);
            channel.queueBind(BiMqConstant.BI_QUEUE_NAME,BiMqConstant.BI_EXCHANGE_NAME,BiMqConstant.BT_MYROUTINGKEY);
        }catch (Exception e){

        }
        //将信息还有路由key发送到交换机
        rabbitTemplate.convertAndSend(BiMqConstant.BI_EXCHANGE_NAME,BiMqConstant.BT_MYROUTINGKEY,message);

    }


}
