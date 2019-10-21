package com.example.redis;

import com.example.redis.util.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil r;

    @Test
    public void contextLoads() {
//        redisTemplate.opsForValue().set("test","测试set");
//        System.out.println(redisTemplate.opsForValue().get("test"));
        r.set("test","测试插入");
        System.out.println(r.get("test"));
    }

}
