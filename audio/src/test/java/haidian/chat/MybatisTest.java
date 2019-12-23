package haidian.chat;

import haidian.audio.MainApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MainApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {


    @Test
    public void gtest(){
//        System.out.println(JSON.toJSONString(groupMapper.selectByPrimaryKey("0001")));
//        System.out.println(JSON.toJSONString(groupMapper.getByUserId("0001")));
//        System.out.println(JSON.toJSONString(groupMapper.getUserByGroupId("0001g")));
    }

}
