package com.example.springbootexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;

public class RedisController {


//    @Autowired  //在这里能够实现注入的前提是？ IOC存在实例（自动装配）
//    private RedisTemplate<String , String > redisTemplate;
//
//    @GetMapping("/say")
//    public String say(){
//        return   redisTemplate.opsForValue().get("name");
//    }
}
