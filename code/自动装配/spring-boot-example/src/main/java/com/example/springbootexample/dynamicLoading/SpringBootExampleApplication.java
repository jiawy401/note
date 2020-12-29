package com.example.springbootexample.dynamicLoading;

import com.example.springbootexample.dynamicLoading1.JWSqlSessionTemplate;
import com.example.springbootexample.dynamicLoading2.JWRedisTemplate;
import com.example.springbootexample.dynamicLoadConfig.EnableConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;


//@ComponentScan("com.*.*")//扫码application之外的目录
//@MapperScan("com.example.springbootexample.dao.mapper")
//@RestController
@SpringBootApplication
@EnableConfiguration
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
