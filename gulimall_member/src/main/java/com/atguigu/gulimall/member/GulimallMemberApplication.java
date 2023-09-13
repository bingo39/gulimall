package com.atguigu.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


//@ComponentScan(basePackages = "com.atguigu.gulimall.member.feign")
@EnableFeignClients(basePackages = "com.atguigu.gulimall.member.feign")     //开启远程调用Feign
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }


    /**
     * 备注：
     * ① @ComponentScan:指明componen扫描路径，但@FeignClient就会扫描不到了
     */

}
