package com.service_haidian;

import com.alibaba.fastjson.JSON;
import com.service_haidian.dao.JjjlMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class ServiceHaidianApplicationTests {

    @Autowired
    JjjlMapper jjjlMapper;

    @Test
    public void contextLoads() {
        long t1=System.currentTimeMillis();
        System.out.println("返回结果为 ： "+JSON.toJSONString(jjjlMapper.queryJjjlByDh("15831878379")));
        long t2=System.currentTimeMillis();
        System.out.println("用时： "+ new Double((t2-t1)/1000)+"秒");
    }

}
