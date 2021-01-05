package com.jiawy.springbootthread.persistence.Mapper;


import com.jiawy.springbootthread.persistence.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void insert(@Param("user") User user);
}
