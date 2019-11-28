package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.chat.dao.GroupMapper;
import haidian.chat.dao.GroupUserMapper;
import haidian.chat.dao.PersonMapper;
import haidian.chat.pojo.GroupUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Autowired(required = false)
    PersonMapper personMapper;

    @Autowired(required = false)
    GroupMapper groupMapper;

    @Resource
    GroupUserMapper groupUserMapper;

    @Test
    public void grouptest(){
        GroupUser gu=new GroupUser();
        gu.setGroupid("0001g");
        gu.setUserid("0001");
        groupUserMapper.insertSelective(gu);
    }

    @Test
    public void gentest(){
//        System.out.println(JSON.toJSONString(empCopyMapper.selectAll()));
        System.out.println(JSON.toJSONString(personMapper.getAll()));
    }

    @Test
    public void gtest(){
//        System.out.println(JSON.toJSONString(groupMapper.selectByPrimaryKey("0001")));
//        System.out.println(JSON.toJSONString(groupMapper.getByUserId("0001")));
        System.out.println(JSON.toJSONString(groupMapper.getUserByGroupId("0001g")));
    }

}
