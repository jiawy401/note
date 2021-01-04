package com.jiawy.springbootthread.persistence.Mapper;


import com.jiawy.springbootthread.persistence.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insert(User user);
}
