package com.atguigu.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1.整合mybatis-plus
 * 1） 导入依赖
 * <dependency>
 * <groupId>com.baomidou</groupId>
 * <artifactId>mybatis-plus-boot-starter</artifactId>
 * <version>3.5.1</version>
 * </dependency>
 * <p>
 * 2） 配置
 * ① 配置数据源
 * 3).导入数据库的驱动
 * 4).在application.yml配置数据源
 * ② 配置Mybatis-plus
 * 1.@MapperScan,告诉mybatis-plus的Mapper文件在哪里
 * 2.告诉mybatis-plus,sql映射文件位置
 * <p>
 * 2.逻辑删除：
 * 2.1 application.yml加入配置（如果默认值和mp默认的一样，该配置可无）
 * 2.2 逻辑删除组件Bean（3.1以上无须）
 * 2.3 加上逻辑删除注解 @TableLogic
 * <p>
 * <p>
 * 3.JSR303 java体验规定第303条：做前端校验
 * eg:@NotNull:标注就不能为空
 * --- 在javax.validation.constraints这个包里面有很多这些标注注解
 * 3.1 给Bean添加校验注解：
 * 3.2 给需要的字段或者属性添加校验（eg：entity中添加@NotNull）
 * 3.3 需要的位置添加@Valid（eg:controller中@RequestBody,给请求体中的数据做校验）
 * 3.4 给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 * <p>
 * 4.统一异常处理
 * <p>
 * 5.分组校验
 * 5.1 给entity的属性注解标注什么情况需要进行校验
 * 5.2 在Controller上添加标注
 * 默认没有指定分组的校验注解@NotBlank，在分组@Validated({AddGrop.class})校验情况下不生效，只会在@Validated生效
 * <p>
 * 6.自定义校验
 * 6.1 编写一个自定义的校验注解
 * 6.2 编写一个自定义的校验器
 * 6.3 关联自定义的校验器和自定义的
 *
 * @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
 * @Retention(RetentionPolicy.RUNTIME)
 * @Documented // 指定校验器，不然无法关联到ListValueConstraintValidator.java
 * @Constraint(validatedBy = {ListValueConstraintValidator.class})  【备注：可以指定多个不同的校验器，适配不同类型的校验】
 * <p>
 * 7. 模板引擎
 * 1）thymeleaf-satrter:关闭缓存
 * 2）静态资源都放在static文件夹下就可以按照路劲直接访问
 * 3）页面放在templates下，直接访问springboot，访问项目的时候，默认会找index
 * 4)页面修改不重启服务器实时更新
 * 4.1）引入dev-tools
 * 4.2)修改完页面 controller build product(shift + f9快捷键)
 */

@EnableCaching  //开启SpringCache缓存功能
@EnableDiscoveryClient  //开启服务注册发现客户端
@MapperScan("com.atguigu.gulimall.product.dao")
@EnableFeignClients(basePackages = "com.atguigu.gulimall.product.Feign")
@SpringBootApplication()
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
