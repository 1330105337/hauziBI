//package com.yupi.springbootinit.biMq;
//
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
///**
// * 创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
// */
//@Slf4j
//public class BiMqInitMain {
//    private static final  String IP="8.130.131.26";
//
//    public static void main(String[] args) {
//        try {
//            log.info("程序执行了.....");
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost(IP);
//            Connection connection = factory.newConnection();
//            Channel channel = connection.createChannel();
//            //声明交换机
//            channel.exchangeDeclare(BiMqConstant.BI_EXCHANGE_NAME,"direct");
//            //创建队列，随机分配一个队列名称
//            channel.queueDeclare(BiMqConstant.BI_QUEUE_NAME,true,false,false,null);
//            channel.queueBind(BiMqConstant.BI_QUEUE_NAME,BiMqConstant.BI_EXCHANGE_NAME,BiMqConstant.BT_MYROUTINGKEY);
//        }catch (Exception e){
//
//        }
//    }
//}
