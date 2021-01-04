package com.jiawy.springbootthread;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.jiawy.springbootthread.persistence")
@SpringBootApplication
public class SpringBootThreadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootThreadApplication.class, args);
    }

}
