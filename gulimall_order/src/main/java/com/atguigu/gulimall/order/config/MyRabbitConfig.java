package com.atguigu.gulimall.order.config;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig{

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 消息转换器
     * --RabbitTemplate发送消息需要的序列化机制
     * 将消息构造器转换为json形式放入容器中，RabbitAutoConfiguration创建时候就会得到该bean
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制RabbitTemplate
     *  1.服务收到消息就回调
     *      ① spring.rabbitmq.publisher-confirms=true
     *      ② 设置确认消息回调的ConfirmCallback
     *  2.消息正确抵达队列进行回调
     *      ① spring.rabbitmq.publisher-return=true
     *          spirng.rabbitmq.template.mandatory=true
     *      ② 设置确认回调ReturnCallback
     *  3.消费端确认（保证每个消息都被消费，broker才会移除消息）
     *
     */
    @PostConstruct      //MyRabbitConfig对象创建完成以后【即构造器调用以后】，执行这个方法
    public void initRabbitTemplate(){
        //设置消息确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){

            /**
             *1.只要消息抵达brock,ack就会变为true
             * @param correlationData 当前消息的唯一关联数据（这个是消息的唯一id）
             * @param ack 消息是否成功收到
             * @param cause     失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData,boolean ack,String cause){
                System.out.println("confirm......"+correlationData+"ack..."+ack+"cause...."+cause);
            }
        });

        //设置消息抵达队列的确认回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递给指定的队列，就触发这个失败回调
             * @param message 投递失败的消息详细信息
             * @param replyCode 恢复的状态码
             * @param replyText 回复的文本内容
             * @param exchange 当时这个消息发给哪个交换机
             * @param routingKey 当时这个消息用哪个路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("fail message==>"+message+"replyCode==>"+replyCode+"replyText==>"+replyText+"exchange==>"+exchange+"routingKey==>"+routingKey);
            }
        });
    }
}
