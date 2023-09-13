package com.atguigu.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyRedissonConfig {

    /**
     * 所有对Redssion的使用都是通过RedissonClient对象
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        //本机默认连接地址：127.0.0.1:6379
//        RedissonClient redisson = Redisson.create();
        Config config = new Config();
        // redisson集群模式
//        config.useClusterServers().addNodeAddress("127.0.0.1:7004", "127.0.0.1:7001");
        // redisson单实例模式
        //可以使用”redis://“来启动SSL连接
        config.useSingleServer().setAddress("redis://192.168.31.106:6379");
        //根据config创建出RedissonClient示例
        return Redisson.create(config);
    }

}
