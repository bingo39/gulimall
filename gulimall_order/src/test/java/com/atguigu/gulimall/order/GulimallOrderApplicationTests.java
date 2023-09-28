package com.atguigu.gulimall.order;


import com.atguigu.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class GulimallOrderApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 测试RabbitMQ
     * 1.如何创建Exchange、Queue、Binging
     *      1) 使用AmqpAdmin进行创建
     * 2.如何收发消息
     */

    /**
     * 创建交换机
     */
    @Test
    public void createExchange(){
        //创建交换机
        DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        System.out.println(directExchange);
        log.info("Exchange[{}]创建成功","hello-java-exchange");
    }

    /**
     * 创建队列
     */
    @Test
    public void creatQueue(){
        // public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("queue[{}]创建成功","hello-java-queue");
    }

    /**
     * 创建绑定
     * 将exchange指定的交换机和destination目的地进行绑定，使用routingKey作为指定的路由键
     */
    @Test
    public void creatBinging(){
        //public Binding(String destination, Binding.DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments)
        Binding binding = new Binding("hello-java-queue",Binding.DestinationType.QUEUE,"hello-java-exchange","hello.java",null);
        amqpAdmin.declareBinding(binding);
        log.info("binging[{}]创建成功","hello-java-binging");
    }

    /**
     * 设置消息收发
     */
    @Test
    public void sendMessageTest(){
        //1.发送消息【实体类--订单退货】
        OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
        orderReturnReasonEntity.setId(1L);
        orderReturnReasonEntity.setCreateTime(new Date());
        orderReturnReasonEntity.setName("测试消息发送&消息序列化");
            //发送消息，如果发送的消息是一个对象，就必须使用序列化机制，将对象写出去，对象必须实现serializable
        rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",orderReturnReasonEntity,new CorrelationData(UUID.randomUUID().toString()));
        log.info("消息发送完成{}",orderReturnReasonEntity);
    }


}
