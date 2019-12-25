package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.audio.MainApplication;
import haidian.audio.dao.GroupRecordMapper;
import haidian.audio.dao.GwCallMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = {MainApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Resource
    GroupRecordMapper groupMapper;

    @Resource
    GwCallMapper gwCallMapper;


    @Test
    public void grouptest(){
        System.out.println(JSON.toJSONString(groupMapper.getGroupAudio("%800008%","2019-12-12","2019-12-13")));
    }

    @Test
    public void gtest(){
        System.out.println(JSON.toJSONString(gwCallMapper.getCallRecord("82588210","2019-12-12","2019-12-13")));
    }

}
