package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.chat.dao.GroupMapper;
import haidian.chat.dao.GroupUserMapper;
import haidian.chat.dao.PersonMapper;
import haidian.chat.pojo.GroupUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Autowired(required = false)
    PersonMapper personMapper;

    @Autowired(required = false)
    GroupMapper groupMapper;

    @Resource
    GroupUserMapper groupUserMapper;

    @Test
    public void guutest(){
        groupUserMapper.outByGroupIdAndUserId("92455589-cb93-44f2-9123-c85adaf96253","0002");
    }


    @Test
    public void ngtest(){
        String id="0001";
        System.out.println(JSON.toJSONString(groupMapper.getByUserId(id)));
        System.out.println(JSON.toJSONString(groupMapper.getUserByGroupId("3754a381-4511-41ab-8111-fd74950e9a8b")));
        System.out.println(JSON.toJSONString(groupMapper.getGroupIdByUserId(id)));
    }

    @Test
    public void pathtest() throws FileNotFoundException {
        String path= ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(path);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(ResourceUtils.getURL("classpath:").getPath());
        File fileDif=new File("src/main/resources/static/uploadFile/");
        System.out.println(fileDif.getAbsolutePath());
        String jar_parent = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
        System.out.println(jar_parent);
    }

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
