package haidian.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.dao.GroupUserMapper;
import haidian.chat.pojo.Group;
import haidian.chat.pojo.GroupUser;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.service.ListenAndSend;
import haidian.chat.service.NotifyThread;
import haidian.chat.util.DateUtil;
import haidian.chat.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ExtraController {

    @Resource
    GroupMapper groupMapper;

    @Resource
    GroupUserMapper groupUserMapper;

    @Autowired
    RedisUtil r;

    @Autowired
    ListenAndSend listenAndSend;

    @RequestMapping("/addGroup")
    public Result addGroup(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String groupId = json.getString("groupId");
            String addUserIds = json.getString("addUserIds");
            String[] userIds = addUserIds.split(",");
            GroupUser gu = new GroupUser();
            gu.setGroupid(groupId);
            for (String userId : userIds) {
                gu.setUserid(userId);
                groupUserMapper.insertSelective(gu);
            }
            result = Result.ok();
        } catch (Exception e) {
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
            groupMapper.updateByPrimaryKeySelective(g);
            result = Result.ok();
        } catch (Exception e) {
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/createGroup")
    public Result createGroup(@RequestBody JSONObject json) {
        Result result = null;
        try {
            //建群
            String groupOwnerId = json.getString("groupOwnerId");
            String uuid = UUID.randomUUID() + "";
            Group newGroup = new Group();
            newGroup.setId(uuid);
            newGroup.setOwnerid(groupOwnerId);
            JSONObject user = (JSONObject) r.get(groupOwnerId);
            newGroup.setName(user.getString("sName") + "的群");
            groupMapper.insertSelective(newGroup);
            //加人
            String initGroupMembers = json.getString("initGroupMembers");
            String[] userIds = initGroupMembers.split(",");
            GroupUser gu = new GroupUser();
            gu.setGroupid(uuid);
            for (String userId : userIds) {
                gu.setUserid(userId);
                groupUserMapper.insertSelective(gu);
            }
            JSONObject uuidJson = new JSONObject();
            uuidJson.put("uuid", uuid);
            result = Result.ok(uuidJson);
        } catch (Exception e) {
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getIndex/{userId}")
    public Result getIndexList(@PathVariable String userId) {
        Result result = null;
        try {
            List<JSONObject> recordList = new ArrayList<>();
            //查组
            List<Group> groups = groupMapper.getByUserId(userId);
            for (Group group : groups) {//遍历组
                if (!r.hasKey(group.getId())) {//如果没有记录则跳出
                    break;
                }
                JSONObject json = new JSONObject();
                json.put("type", "group");
                json.put("dst", group);
                //查redis中每个组的记录
                List<Object> records = r.lGet(group.getId(), 0, r.lGetListSize(group.getId()));
                int notice = 0;
                for (int i = records.size() - 1; i >= 0; i--) {//倒序遍历组的记录
                    JSONObject record = (JSONObject) records.get(i);

                    if (i == records.size() - 1) {//记录最后一条的数据
                        json.put("lastTime", record.getString("sendTime"));
                        json.put("content", record.getJSONObject("data").getJSONObject("content"));
                    }
                    //判断几条未读，如果遍历到已读则跳出循环
                    String recordSrcId=record.getJSONObject("data").getJSONObject("src").getString("sId");
                    if(recordSrcId.equalsIgnoreCase(userId)){//如果是自己发送的消息，则结束当前循环
                        continue;
                    };
                    String readIds = record.getJSONObject("data").getString("readId");
                    if (readIds.indexOf(userId) != -1) {//如果已读id有自己则跳出
                        break;
                    }
                    notice += 1;
//                    if(notice==10){//最多显示10条未读
//                        break;
//                    }
                }
                json.put("notice", notice);
                recordList.add(json);
            }
            //查两人对话
            Set<String> singleIds = r.keys("*" + userId + "*");
            Iterator<String> it = singleIds.iterator();
            while (it.hasNext()) {
                String id = it.next();
                if (id.indexOf(".") == -1) {
                    it.remove();
                }
            }
            for (String singleId : singleIds) {
                JSONObject json = new JSONObject();
                json.put("type", "single");
                //查redis中每个对话的记录
                List<Object> records = r.lGet(singleId, 0, r.lGetListSize(singleId));
                int notice = 0;
                for (int i = records.size() - 1; i >= 0; i--) {
                    JSONObject record = (JSONObject) records.get(i);
                    if (i == records.size() - 1) {//记录最后一条的数据
                        json.put("lastTime", record.getString("sendTime"));
                        json.put("content", record.getJSONObject("data").getJSONObject("content"));
                        JSONObject src = record.getJSONObject("data").getJSONObject("src");
                        JSONObject dst = record.getJSONObject("data").getJSONObject("dst");
                        if (userId.equalsIgnoreCase(src.getString("sId"))) {
                            json.put("dst", dst);
                        } else if (userId.equalsIgnoreCase(dst.getString("sId"))) {
                            json.put("dst", src);
                        }
                    }
                    //判断几条未读，如果遍历到已读则跳出循环
                    String recordSrcId=record.getJSONObject("data").getJSONObject("src").getString("sId");
                    if(recordSrcId.equalsIgnoreCase(userId)){//未读不算自己发送的消息，如果是自己发送的消息，则结束本次循环
                        continue;
                    };
                    String readIds = record.getJSONObject("data").getString("readId");
                    if (readIds.indexOf(userId) != -1) {//如果已读id有自己则跳出
                        break;
                    }
                    notice += 1;
//                    if(notice==10){//最多显示10条未读
//                        break;
//                    }
                }
                json.put("notice", notice);
                recordList.add(json);
            }
            //按时间倒序排序
            Collections.sort(recordList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject j1, JSONObject j2) {
                    Date d1 = DateUtil.getDateTime(j1.getString("lastTime"));
                    Date d2 = DateUtil.getDateTime(j2.getString("lastTime"));
                    if (d1.getTime() > d2.getTime()) {
                        return -1;
                    } else if (d1.getTime() < d2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            result = Result.ok(recordList);
        } catch (Exception e) {
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
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getFriends")
    public Result getFriends() {
        Result result = null;
        try {
            List<Person> persons = (List<Person>) r.get("persons");
            result = Result.ok(persons);
        } catch (Exception e) {
            result = Result.build(500, e.getMessage());
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
            List<Object> msgs=  r.lGet(key, 0, r.lGetListSize(key));
            //前台收到数据进行显示，直接在后台发送已读
            //发送notify
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new NotifyThread(src,msgs,listenAndSend));

            result=Result.ok(msgs);
        }catch (Exception e){
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

}
