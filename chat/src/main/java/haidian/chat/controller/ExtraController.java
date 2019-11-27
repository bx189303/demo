package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import haidian.chat.dao.GroupMapper;
import haidian.chat.pojo.Group;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ExtraController {

    @Resource
    GroupMapper groupMapper;

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/addGroup")
    public Result addGroup(@RequestBody JSONObject json){
        Result result=null;
        try {


            result=Result.ok();
        }catch (Exception e){
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getRecord")
    public Result getRecord(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            String key="";
            if("single".equalsIgnoreCase(type)){
                key=src.compareTo(dst)>0?src+"."+dst:dst+"."+src;
            }else if("group".equalsIgnoreCase(type)){
                key=dst;
            }
            List<Object> msgs=  redisUtil.lGet(key, 0, redisUtil.lGetListSize(key));
            result=Result.ok(msgs);
        }catch (Exception e){
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getGroup/{userId}")
    public Result getGroupByUserId(@PathVariable String userId){
        Result result=null;
        System.out.println(userId);
        try {
            List<Group> groups = groupMapper.getByUserId(userId);
            result=Result.ok(groups);
        }catch (Exception e){
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getFriends")
    public Result getFriends(){
        Result result=null;
        try {
            List<Person> persons= (List<Person>) redisUtil.get("persons");
            result=Result.ok(persons);
        }catch (Exception e){
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

}
