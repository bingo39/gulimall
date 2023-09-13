# spring注解

## controller层

+ @RequestParam

-- 概念：

接收url请求的携带参数 获取URL中携带的请求参数的值既URL中“?”后携带的参数,传递参数的格式是:key=value
例如：`https://localhost/requestParam/test?key1=value1&key2=value2..`

| 属性 | 值   | 默认值 |
| ---- | ---- | ------ |
| value | (param) | |
|required | (Boolean) |true|

+ @PathVariable

用于获取URL中路径的参数值,参数名由RequestMapping注解请求路径时指定,常用于restful风格的api中,传递参数格式:直接在url后添加需要传递的值即可
例如：`https://localhost/pathVariable/test/value1/value2...`

+ @RestController
  @RestController是Spring MVC框架中的一个注解，它结合了@Controller和@ResponseBody两个注解的功能，用于标记一个类或者方法，
  表示该类或方法用于处理HTTP请求，并将响应的结果直接返回给客户端，而不需要进行视图渲染。

## config层

+ @ConfigurationProperties注解 @ConfigurationProperties是springboot提供读取配置文件的一个注解

    + 作用： 1.常用于配置读写分离的场景

  --- 通过抽取出配置类中的属性，使得配置文件可以操作这些属性

```java
@ConfigurationProperties(prefix = "gulimall.thread")       //绑定容器配置
@Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
```

配置文件：

```properties
## 自定义配置属性
gulimall.thread.core-size=20
gulimall.thread.max-size=200
gulimall.thread.keep-alive-time=10
```

配置类：

```java
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool){
        return  new ThreadPoolExecutor(pool.getCoreSize(),pool.getMaxSize(),pool.getKeepAliveTime(), TimeUnit.SECONDS,new LinkedBlockingDeque<>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

    }
}
```

