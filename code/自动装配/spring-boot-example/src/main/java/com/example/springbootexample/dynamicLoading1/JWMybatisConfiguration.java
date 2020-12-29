package com.example.springbootexample.dynamicLoading1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWMybatisConfiguration {

    @Bean
    public JWSqlSessionTemplate jwSqlSessionTemplate(){
        return new JWSqlSessionTemplate();
    }
}
