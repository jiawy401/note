package com.example.springbootexample;

import com.example.springbootexample.dynamicLoading2.JWRedisTemplate;
import com.example.springbootexample.dynamicLoadConfig.EnableConfiguration;
import com.example.springbootexample.dynamicLoading1.JWSqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


//@ComponentScan("com.*.*")//扫码application之外的目录
@MapperScan("com.example.springbootexample.dao.mapper")
@RestController
@SpringBootApplication
public class SpringBootExampleApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootExampleApplication.class, args);
        System.out.println(configurableApplicationContext.getBean(JWRedisTemplate.class));
        System.out.println(configurableApplicationContext.getBean(JWSqlSessionTemplate.class));
    }

    @GetMapping
    public String  test(){
        return "-------------";
    }

}
