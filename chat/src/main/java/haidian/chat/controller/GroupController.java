package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.dao.GroupUserMapper;
import haidian.chat.pojo.Group;
import haidian.chat.pojo.GroupUser;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.service.GroupService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class GroupController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    GroupMapper groupMapper;

    @Resource
    GroupUserMapper groupUserMapper;

    @Autowired
    GroupService groupService;

    @Autowired
    RedisUtil r;



    @RequestMapping("/outGroup")
    public Result outGroup(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String groupId=json.getString("groupId");
            String userId=json.getString("userId");
            groupUserMapper.outByGroupIdAndUserId(groupId,userId);
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


    @RequestMapping("/updateGroup")
    public Result updateGroup(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String groupId = json.getString("groupId");
            String groupName = json.getString("groupName");
            Group g = new Group();
            g.setId(groupId);
            g.setName(groupName);
            g.setIsValid(1);
            groupMapper.updateByPrimaryKeySelective(g);
            //发送群更新的群通知
            groupService.sendGroupNotify("updateGroup",groupId,"");
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/addGroup")
    public Result addGroup(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String groupId = json.getString("groupId");
            String ids=json.getString("userIds");
            String[] idArray= ids.split(",");
            GroupUser gu = new GroupUser();
            gu.setIsValid(1);
            gu.setCreateTime(new Date());
            gu.setGroupid(groupId);
            List<String> userIdInGroup = groupMapper.getUserByGroupId(groupId);
            for (int i = 0; i <idArray.length ; i++) {
                if(userIdInGroup.contains(idArray[i])){ //如果群内已经有这人，则不添加
                    continue;
                }
                gu.setUserid(idArray[i]);
                groupUserMapper.insertSelective(gu);
            }
            //发送群加人的群通知
            groupService.sendGroupNotify("addUser",groupId,ids);
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/createGroup/{userIds}")
    public Result createGroup(@PathVariable String userIds) {
        Result result = null;
        try {
            String[] userIdArray = userIds.split(",");
            if(userIdArray.length<3){
                return Result.build(500,"创建新群最少3人");
            }
            //判断是否已经有群,如果有直接返回
            List<String> groups=new ArrayList<>();
            for (int i = 0; i <userIdArray.length ; i++) {
                if(i==0){
                    groups = groupMapper.getGroupIdByUserId(userIdArray[i]);
                }else{
                    List<String> groupsByUserId= groupMapper.getGroupIdByUserId(userIdArray[i]);
                    groups.retainAll(groupsByUserId);
                    if(groups.size()==0){
                        break;
                    }
                }
            }
            for (String group : groups) {
                List<String> userIdsByGroupId = groupMapper.getUserByGroupId(group);
                if(userIdsByGroupId.size()==userIdArray.length){
                    //返回群id
                    JSONObject uuidJson = new JSONObject();
                    uuidJson.put("uuid", group);
                    return Result.build(200,"已有该群!",uuidJson);
                }
            }
            //建群
            String groupOwnerId = userIdArray[0];
            String uuid = UUID.randomUUID() + "";
            Group newGroup = new Group();
            newGroup.setId(uuid);
            newGroup.setOwnerid(groupOwnerId);
            Person user = (Person) r.get(groupOwnerId);
            newGroup.setName(user.getsName() + "的群");
            newGroup.setCreateTime(new Date());
            newGroup.setIsValid(1);
            groupMapper.insert(newGroup);
            //加人
            JSONObject addJson=new JSONObject();
            addJson.put("groupId",uuid);
            addJson.put("userIds",userIds);
            addGroup(addJson);
            //返回群id
            JSONObject uuidJson = new JSONObject();
            uuidJson.put("uuid", uuid);
            result = Result.build(200,"创建群成功!",uuidJson);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getGroup/{userId}")
    public Result getGroupByUserId(@PathVariable String userId) {
        Result result = null;
        try {
            List<Group> groups = groupMapper.getByUserId(userId);
            result = Result.ok(groups);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }
}
