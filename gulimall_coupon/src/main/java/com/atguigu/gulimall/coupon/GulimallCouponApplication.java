package com.atguigu.gulimall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1.想要远程调用别的服务
 * 1.）引入open-feign
 * 2.)编写一个接口，告诉springcloud这个接口需要调用的远程服务
 * ①声明接口的每一个方法都是调用哪个远程服务的那个请求
 * 3.开启feign远程调用功能
 * <p>
 * 4.如何使用Nacos作为配置中心统一管理配置
 * 1.）引入依赖
 * <!--服务注册/发现-->
 * <dependency>
 * <groupId>com.alibaba.cloud</groupId>
 * <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
 * </dependency>
 * <p>
 * 2.) 创建一个bootstrap.properties
 * (这是springboot为nacos的配置中心做的配置)
 * spring.cloud.nacos.config.server-addr=127.0.0.1:8848
 * spring.application.name=gulimall-coupons
 * <p>
 * 3.)需要给配置中心默认添加一个叫  数据集（Data Id）xxx.properties.
 * 默认规则：应用名.properties
 * 给 应用名.properties 添加任何配置
 * 动态获取配置
 *
 * @RefreshScope:动态获取并刷新配置
 * @Value("${配置项的名}"):获取到配置 规则：如果配置中心和当前应用的配置文件中都配置了相同的项，优先选择配置中心的
 * <p>
 * <p>
 * 5. nacos细节点
 * 1.）命名空间：配置隔离
 * 默认：public（保留空间）；默认新增的所有配置都在public空间
 * 1.开发，测试，生产：利用命名空间来做环境隔离 【基于环境隔离】
 * 注意：在bootstrap.properties里 指明上需要的命名空间下的配置（默认是使用public命名空间的）
 * 2.每一个微服务之间互相隔离配置，每一个微服务都创建自己的命名空间，只加载自己命名空间下的所有配置  【基于微服务】
 * 2.）配置集
 * 概念：一组相关或不相干的配置项的集合称为配置集（properties文件中所有的配置）
 * <p>
 * 3.）配置集ID：类似于配置文件名
 * 也即是nacos配置列表中的DataID
 * 4.）配置分组
 * 也即是nacos配置列表中的GROP下
 * 可以把同个GROP下的配置文件归纳到一组微服务中，换句话说，也就是配置文件于微服务间多对多的关系
 * <p>
 * 6. 同时加载多个配置集
 * 1.）微服务任何配置信息，任何配置文件都可以放在配置中心中
 * 2.）只需要在bootstrap.properties说明加载配置中心中哪个配置文件即可
 * 3.）获取配置文件的注解：
 * @Value,@ConfigurationPropertries..... 以前SpringBoot任何方式从配置文件中获取配置值，都能使用
 * 配置中心有的优先使用配置中心的（相当于配置中心的配置文件搬家到项目的配置文件中）
 */


@RefreshScope       //动态获取并刷新配置
@EnableDiscoveryClient  //开启远程注册nacos
@SpringBootApplication
public class GulimallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class, args);
    }

}
