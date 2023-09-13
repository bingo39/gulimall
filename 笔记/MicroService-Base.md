微服务基础
==========

# feign-远程调用

概念:服务发送到注册/配置中心(nacos)，需要调用还需要远程调用的组件(feign)

<font color=red>注意：</font></br>
feign不支持调用的服务名称带有下划线"_"

## 快速入门open-feign

> 项目中的`gulimall-coupon`就是远程调用服务。</br> -- 当各个微服务注册到注册中心后，也还是需要服务来调用才可以使得各个微服务间互相调用

1. 引入open-feign
   ```xml
           <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
   ```
2. 编写一个接口，告诉springcloud这个接口需要调用的远程服务
    + 声明接口的每一个方法都是调用哪个远程服务的那个请求

举例说明：
`gulimall_member`中创建一个远程服务`菜单列表（member/list）`调用接口

```java
/**
 * 远程接口，让Coupon各个服务能被调用
 * 声明式远程调用，也就是查找gulimall_coupon这个服务
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
```

3. 主程序中开启feign远程调用功能

主要是用到@EnableFeignClients注解

```java
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

```

