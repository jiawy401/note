package com.example.springbootexample.example1;

import com.example.springbootexample.dao.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {


    @Bean
    public User user(){
        return new User();
    }
}
