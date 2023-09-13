package com.atguigu.gulimall.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
// 备注：包选择都选择reactive，也就是响应式
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 网关配置跨域
 * Filter允许请求通过，并且在响应的时候再赋给response具体的响应头
 */
@Configuration
public class GulimallCORSConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        // spring本身就提供了CorsWebFilter类

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration CORSConfiguration = new CorsConfiguration();
        // 所有跟跨域有关的配置，都是写在configuration里面

        // 1.配置跨域
        CORSConfiguration.addAllowedHeader("*");    // 请求头
        CORSConfiguration.addAllowedMethod("*");    // 请求方法
        CORSConfiguration.addAllowedOrigin("*");    // 请求源
        CORSConfiguration.setAllowCredentials(true);    // 允许cookie跨域


        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", CORSConfiguration);
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
