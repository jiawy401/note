package com.jiawy.springbootthread.persistence.controller;

import com.jiawy.springbootthread.persistence.model.User;
import com.jiawy.springbootthread.persistence.service.SmsClient;
import com.jiawy.springbootthread.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    SmsClient smsClient;

    /**
     * 规范 Mapping里不能是动词
     */
    @PostMapping("/user")
    public String add(@RequestBody User user){

        long start = System.currentTimeMillis();
        userService.add(user);
        long end = System.currentTimeMillis();
        return "success:" +(end -start);
    }

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @PostMapping("/sms/user")
    public String register(User user){

        long start = System.currentTimeMillis();
        userService.add(user);

        //异步化
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                smsClient.sendSms("18598905443");
            }
        });
//        new Thread(()->{
//            smsClient.sendSms("18598905443");
//        }).start();


        long end = System.currentTimeMillis();
        return "success:" +(end -start);
    }
}
