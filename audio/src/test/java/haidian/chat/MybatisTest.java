package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.audio.MainApplication;
import haidian.audio.dao.db1.GroupRecordMapper;
import haidian.audio.dao.db1.GwCallMapper;
import haidian.audio.dao.db2.PersonMapper;
import haidian.audio.pojo.po.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {MainApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Resource
    GroupRecordMapper groupMapper;

    @Resource
    GwCallMapper gwCallMapper;

    @Resource
    PersonMapper personMapper;

    @Test
    public void persontest(){
        List<Person> ps = personMapper.getAll();
        Map<String,Object> m=new HashMap<>();
        for (Person p : ps) {
            String policeNum=p.getsPolicenum();
            if(policeNum!=null){
                m.put(p.getsPolicenum(),p);
            }
        }
        System.out.println(JSON.toJSONString(m));

    }


    @Test
    public void grouptest(){
        System.out.println(JSON.toJSONString(groupMapper.getGroupAudio("%800008%","2019-12-12","2019-12-13")));
    }

    @Test
    public void gtest(){
        System.out.println(JSON.toJSONString(gwCallMapper.getCallRecord("82588210","2019-12-12","2019-12-13")));
    }

}
