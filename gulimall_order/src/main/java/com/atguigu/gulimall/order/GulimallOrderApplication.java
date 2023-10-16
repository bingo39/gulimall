package com.atguigu.gulimall.order;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用RabbitMQ
 * 1.引入amqp场景；RabbitAutoConfiguration就会自动生效
 * 2. 给容器中自动配置
 *      RabbitTemplate、AmqpAdmin、CachingConnectionFactory、RabbitMessagingTemplate
 *      所有属性都是 spring.rabbitmq
 *      @ConfigurationProperties(prefix = "spring.rabbitmq") 【类路径：org.springframework.boot.autoconfigure.amqp.RabbitProperties.class】
 * 3.配置文件配置rabbitmq的host、virtual-host、port等信息
 * 4. 注解开启：@EnableRabbit
 * 5. 监听消息：使用@RabbitListener:必须有@EnableRabbit
 */
@SpringBootApplication(exclude = GlobalTransactionAutoConfiguration.class)
@EnableRabbit
@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableFeignClients
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
