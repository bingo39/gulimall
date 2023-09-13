
package io.renren.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    //不需要，后面自己写跨域问题
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOriginPatterns("*")
//            .allowCredentials(true)
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .maxAge(3600);
//    }


    /**
     * 备注：
     *   allowedOriginPatterns("*")方法有报错。原来springboot2.6.x版本，
     *   现在改成2.2.x,可以换成.allowedOrigins("*")
     */
}
