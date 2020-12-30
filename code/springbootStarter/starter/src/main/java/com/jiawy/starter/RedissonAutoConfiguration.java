package com.jiawy.starter;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(Redisson.class) //条件装配，classpath下存在Redisson才会加载本类
@EnableConfigurationProperties(RedissonProperties.class)
@Configuration
public class RedissonAutoConfiguration {


    @Bean
    RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Config config = new Config();
        String prefix = "redis://";
        if(redissonProperties.isSsl()){
            prefix = "redis://";
        }

        config.useSingleServer()
                .setAddress(redissonProperties.getHost() + ":" + redissonProperties.getPort())
                .setIdleConnectionTimeout(redissonProperties.getTimeout());

        return Redisson.create(config);
    }
}
