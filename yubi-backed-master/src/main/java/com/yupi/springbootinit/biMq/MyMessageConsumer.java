package com.yupi.springbootinit.biMq;


import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;



/**
 * 消费者代码
 */
@Component
@Slf4j
public class MyMessageConsumer {
    //指定程序的队列和监听机制
    @RabbitListener(queues = {"bi_queue"},ackMode = "MANUAL")
    @SneakyThrows
    public  void  receiveMessage(String message, Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){

        log.info("receviceMessage message={}",message);
        channel.basicAck(deliveryTag,false);
    }
}
