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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
            //建群
            String groupOwnerId=json.getString("groupOwnerId");
            String uuid= UUID.randomUUID()+"";
            Group newGroup=new Group();
            newGroup.setId(uuid);
            newGroup.setOwnerid(groupOwnerId);
            JSONObject user = (JSONObject) redisUtil.get(groupOwnerId);
            newGroup.setName(user.getString("sName")+"的群");
            groupMapper.insertSelective(newGroup);
            //加人
            String initGroupMembers=json.getString("initGroupMembers");
            String[] userIds = initGroupMembers.split(",");
            for (String userId : userIds) {

            }

            result=Result.ok();
        }catch (Exception e){
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getIndex/{userId}")
    public Result getIndexList(@PathVariable String userId){
        Result result=null;
        try {
            List<JSONObject> list=new ArrayList<>();
            List<Group> groups = groupMapper.getByUserId(userId);
            JSONObject json=new JSONObject();
            json.put("type","gourp");
            for (Group group : groups) {
                json.put("src",group);
                List<Object> records = redisUtil.lGet(group.getId(), 0, redisUtil.lGetListSize(group.getId()));
                int notice=0;
                for (int i = records.size()-1; i >=0 ; i--) {
                    JSONObject record = (JSONObject) records.get(i);
                    if(i==record.size()-1){
                        json.put("lastTime",record.getString("sendTime"));
                        json.put("content",record.getJSONObject("data").getJSONObject("content"));
                    }
                    //判断几条未读，如果遍历到已读则跳出循环
                    int readCount = record.getJSONObject("data").getIntValue("readCount");
                    if(readCount==1){

                    }
                    notice+=1;
                    if(i==records.size()-11){
                        break;
                    }
                }
                json.put("notice",notice);
                list.add(json);

            }

            Set<String> singleIds = redisUtil.keys("*" + userId + "*");
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
