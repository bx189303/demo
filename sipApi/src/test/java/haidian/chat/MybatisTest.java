package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.sipApi.MainApplication;
import haidian.sipApi.dao.db1.GroupRecordMapper;
import haidian.sipApi.dao.db1.GwCallMapper;
import haidian.sipApi.dao.db1.GwRecordMapper;
import haidian.sipApi.dao.db1.SipUserMapper;
import haidian.sipApi.dao.db2.PersonMapper;
import haidian.sipApi.pojo.po.Person;
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

    @Resource
    GwRecordMapper gwRecordMapper;

    @Resource
    SipUserMapper proMapper;

    @Test
    public void protest(){

//        proMapper.addSipUser(null,"00001abc","2");
        String res="";
        String id="00001e3123";
        String type="2";
        Map<String ,Object> m=new HashMap<>();
        m.put("res",res);
        m.put("id",id);
        m.put("type",type);
//        proMapper.addSipUser(res,"001012abcdfe","12");
//        System.out.println(res);
//        proMapper.delSipUser(m);
        System.out.println(m.get("res"));
    }

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
