package com.example.websql;

import com.alibaba.fastjson.JSON;
import com.example.websql.bean.Emp;
import com.example.websql.bean.User;
import com.example.websql.dao.EmpMapper;
import com.example.websql.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmpUserTest {

    @Autowired(required = false)
    EmpMapper empMapper;

    @Autowired(required = false)
    UserMapper userMapper;

    @Test
    public void EUTest(){
        EmpTest();
//        UserTest();
    }

    public void UserTest(){
        User u=null;
        u=userMapper.getUserById1(2);
//        u=userMapper.getUserById2(5);
        System.out.println(JSON.toJSONString(u));
    }

    public void EmpTest(){
        Emp e=null;
        e = empMapper.getEmpById1(1);
//        e = empMapper.getEmpById2(3);
        System.out.println(JSON.toJSONString(e));
    }


}
