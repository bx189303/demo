package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.chat.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;

@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RedisTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisUtil r;

    @Test
    public void listsettest(){
        r.lSet("list","a");
        r.lSet("list","b");
        r.lSet("list","c");
        r.lSet("list","z");
        List<Object> objects = r.lGet("G2", 0, r.lGetListSize("G2"));
        System.out.println(JSON.toJSONString(objects));
    }

    @Test
    public void listupdatetest(){
        long t1= System.currentTimeMillis();
        List<Object> list = r.lGet("list", 0, r.lGetListSize("list"));
        for (int i = 0; i < list.size() ; i++) {
            String v= (String) list.get(i);
            if (v.equals("z")){
                r.lUpdateIndex("list",i,"替换");
            }
        }
        long t2= System.currentTimeMillis();
        System.out.println(JSON.toJSONString(list));
        System.out.println(new Double(t2-t1)/1000);
    }

    @Test
    public void setTest() {
        r.set("testchat","测试插入2");
        System.out.println(r.get("test"));
    }

    @Test
    void haskeytest(){
        System.out.println(r.hasKey("testabc"));
        System.out.println(r.hasKey("testchat"));
    }

    @Test
    void getkeytest(){
        long t1= System.currentTimeMillis();
//        Set<String> keys=r.keys("chat");
//        for (String key : keys) {
//            System.out.println(key);
//        }
        Set<String> keys = redisTemplate.keys("null");
        System.out.println(JSON.toJSONString(keys));
        long t2= System.currentTimeMillis();
        System.out.println("第一次时间："+ new Double(t2-t1)/1000);
        keys = redisTemplate.keys("*25*");
        System.out.println(JSON.toJSONString(keys));
        long t3=System.currentTimeMillis();
        System.out.println("双*时间："+ new Double(t3-t2)/1000);
//        keys = redisTemplate.keys("chat");
//        System.out.println(JSON.toJSONString(keys));
        for (String key : keys) {
            r.get(key);
        }
        long t4=System.currentTimeMillis();
        System.out.println("无*时间："+ new Double(t4-t3)/1000);
    }

    @Test
    public void keystest(){
        long t1= System.currentTimeMillis();
        Set<String> keys = r.keys(25+"*");
        Set<String> keys2 = r.keys("*"+25);
        keys.addAll(keys2);
        long t2= System.currentTimeMillis();
        System.out.println("合并前后*时间："+ new Double(t2-t1)/1000);
    }

    @Test
    void settest(){
        for (int i = 0; i < 10000; i++) {
            String key=UUID.randomUUID()+"";
            r.set(key,"test");
        }

    }

}
