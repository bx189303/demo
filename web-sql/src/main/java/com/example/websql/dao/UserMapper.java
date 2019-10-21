package com.example.websql.dao;

import com.example.websql.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User getUserById1(int id);

    User getUserById2(int id);

}
