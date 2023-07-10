package com.yupi.springbootinit.biMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
 */
public class MqInitMain {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("8.130.131.26");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //声明交换机
            String EXCHANGE_NAME="bi_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            //创建队列，随机分配一个队列名称
            String queueName="bi_queue";
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName,EXCHANGE_NAME,"bi_routingKey");
        }catch (Exception e){

        }
    }
}
