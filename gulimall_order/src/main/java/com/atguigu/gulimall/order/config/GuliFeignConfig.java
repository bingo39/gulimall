package com.atguigu.gulimall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class GuliFeignConfig {
    //提前给feign添加一个请求拦截器
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //获取到order服务刚发送过来的请求【主要是为了获取cookie】(之前请求)
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                if(requestAttributes!=null){
                    System.out.println("RequestInterceptor线程...."+Thread.currentThread().getId());
                    if(requestAttributes !=null){
                        //老请求
                        HttpServletRequest request = requestAttributes.getRequest();
                        // 同步请求头数据 cookie（新请求给老请求同步了cookie）
                        requestTemplate.header("Cookie",request.getHeader("Cookie"));
                        //给拦截器设置请求头
                        System.out.println("feign远程之前先进行RequestInterceptor.apply");
                    }
                }
            }
        };
    }
}
