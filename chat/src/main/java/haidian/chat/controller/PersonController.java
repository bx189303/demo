package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.dao.PersonMapper;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    PersonMapper personMapper;

    @Resource
    GroupMapper groupMapper;

    @Autowired
    RedisUtil r;

    //加载redis中的人员基本信息
    @RequestMapping("/loadUserInfo")
    public Result loadUserInfo(){
        Result result = null;
        try {
            List<Person> persons = personMapper.getAll();
//            r.set("persons",persons);
            for (Person person : persons) {
                r.set(person.getsId(),person);
                r.lSet("personList",person);
            }
            log.info("加载人员信息完毕");
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getUserByUserId/{userId}")
    public Result getUserByUserId(@PathVariable String userId) {
        Result result = null;
        try {
            Person person = (Person) r.get(userId);
            result = Result.ok(person);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    /**
     * @param input 可以是姓名，单位名，警号
     */
    @RequestMapping("/searchPerson/{input}")
    public Result searchPerson(@PathVariable String input) {
        Result result = null;
        try {
            List<Person> users=new ArrayList<>();
            String name="%"+input+"%";
            List<String> userIds = personMapper.searchByNameOrUnitnameOrPolicenum(name);
            for (String userId : userIds) {
                Person user= (Person) r.get(userId);
                users.add(user);
            }
            result = Result.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getPersonByGroupId/{groupId}")
    public Result getPersonByGroupId(@PathVariable String groupId) {
        Result result = null;
        try {
            List<Person> users=new ArrayList<>();
            List<String> userIds = groupMapper.getUserByGroupId(groupId);
            for (String userId : userIds) {
                Person user= (Person) r.get(userId);
                users.add(user);
            }
            result = Result.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }
    @RequestMapping("/getUserByGroup/{groupId}")
    public Result getUserByGroup(@PathVariable String groupId) {
        Result result = null;
        try {
            List<Person> persons = new ArrayList<>();
            List<String> userIds=groupMapper.getUserByGroupId(groupId);
            for (String userId : userIds) {
                Person user= (Person) r.get(userId);
                persons.add(user);
            }
            result = Result.ok(persons);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


    public Result getPersonAll() {
        Result result = null;
        try {
            List<Object> persons = r.lGet("personList", 0, -1);
            result = Result.ok(persons);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    public Result getUserByPoliceNum(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String policeNum=json.getString("username");
            Person person= personMapper.getUserByPoliceNum(policeNum);
            result = Result.ok(person);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

}
