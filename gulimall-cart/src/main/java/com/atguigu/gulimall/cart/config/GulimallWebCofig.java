package com.atguigu.gulimall.cart.config;

import com.atguigu.gulimall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring中添加拦截器，不需要Component注释放入容器内，但要使用@Configuration配置mvc的传输
 */
@Configuration
public class GulimallWebCofig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //需要拦截的请求的url
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
