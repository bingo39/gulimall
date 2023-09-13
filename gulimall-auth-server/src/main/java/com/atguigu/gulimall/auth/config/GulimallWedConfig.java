package com.atguigu.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GulimallWedConfig implements WebMvcConfigurer {

    // addViewControllers方法：自动映射请求到页面

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        /**
         * 相当于：
         *     @GetMapping("/login.html")
         *     public String loginPage(){
         *         return "login";
         *     }
         */
        registry.addViewController("/login.html").setViewName("login");

        /**
         * 相当于：
         * @GetMapping("/register.html")
         *     public String registerPage(){
         *         return "register";
         *     }
         */
        registry.addViewController("/register.html").setViewName("register");
    }

}
