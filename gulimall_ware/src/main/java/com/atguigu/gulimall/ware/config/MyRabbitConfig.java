package com.atguigu.gulimall.ware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列中库存锁&解锁微服务的配置
 */
@Configuration
public class MyRabbitConfig {

    /**
     * 消息转换器
     * --RabbitTemplate发送消息需要的序列化机制
     * 将消息构造器转换为json形式放入容器中，RabbitAutoConfiguration创建时候就会得到该bean
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange stockEventExchange(){
        return new TopicExchange("stock-event-exchange",true,false);
    }

    @Bean
    public Queue stockReleaseStockQueue(){
        return new Queue("stock.release.stock.queue",true,false,false);
    }

    /**
     * 延迟队列
     */
    @Bean
    public Queue stockDelayQueue(){
        /**
         * 需要配置的参数：
         * x-dead-letter-exchange:stock-event-exchange
         * x-dead-letter-routing-key: stock.release
         * x-message-ttl: 120000
         */
        Map<String,Object> arg = new HashMap<>();
        arg.put("x-dead-letter-exchange","stock-event-exchange");
        arg.put("x-dead-letter-routing-key","stock.release");
        arg.put("x-message-ttl",120000);
        return new Queue("stock.delay.queue",true,false,false,arg);
    }

    /**
     * 交换机与延迟队列的绑定关系
     */
    @Bean
    public Binding stockLockedBinding(){
       return new Binding("stock.delay.queue",Binding.DestinationType.QUEUE,"stock-event-exchange","stock.locked",null);
    }

    /**
     *库存解锁服务 & 交换机绑定队列关系
     */
    @Bean
    public Binding stockReleaseBinding(){
        return new Binding("stock.release.stock.queue",Binding.DestinationType.QUEUE,"stock-event-exchange","stock.release.#",null);
    }

}
