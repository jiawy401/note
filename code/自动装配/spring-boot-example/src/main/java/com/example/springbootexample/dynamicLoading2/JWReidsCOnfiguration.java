package com.example.springbootexample.dynamicLoading2;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWReidsCOnfiguration {


    @Bean
    public JWRedisTemplate redisTemplate(){
        return new JWRedisTemplate();
    }
}
