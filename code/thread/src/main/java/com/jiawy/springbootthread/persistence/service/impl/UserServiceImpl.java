package com.jiawy.springbootthread.persistence.service.impl;

import com.jiawy.springbootthread.persistence.Mapper.UserMapper;
import com.jiawy.springbootthread.persistence.model.User;
import com.jiawy.springbootthread.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public void add(User user) {
        userMapper.insert(user);
    }
}
