package haidian.chat;

import haidian.chatSip.ChatSipApplication;
import haidian.chatSip.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = {ChatSipApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RedisTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisUtil r;

    @Test
    public void t(){
        System.out.println(r.hget("personMapOfIdNum","0004"));
    }

}
