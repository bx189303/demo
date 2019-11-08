package com.example.security;

import com.example.security.dao.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

    @Autowired(required = false)
    SysUserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(userMapper.selectById(1));
    }

}
