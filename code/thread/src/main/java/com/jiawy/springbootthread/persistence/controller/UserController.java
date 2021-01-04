package com.jiawy.springbootthread.persistence.controller;

import com.jiawy.springbootthread.persistence.model.User;
import com.jiawy.springbootthread.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    /**
     * 规范 Mapping里不能是动词
     */
    @PostMapping("/user")
    public String add(User user){

        long start = System.currentTimeMillis();
        userService.add(user);
        long end = System.currentTimeMillis();
        return "success:" +(end -start);
    }
}
