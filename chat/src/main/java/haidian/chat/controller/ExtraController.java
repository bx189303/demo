package haidian.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.FriendMapper;
import haidian.chat.dao.GroupMapper;
import haidian.chat.dao.GroupUserMapper;
import haidian.chat.dao.PersonMapper;
import haidian.chat.pojo.Friend;
import haidian.chat.pojo.Group;
import haidian.chat.pojo.GroupUser;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.service.ListenAndSend;
import haidian.chat.service.NotifyThread;
import haidian.chat.util.DateUtil;
import haidian.chat.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${nginxUrl}")
    String nginxUrl;

    @Value("${websocketUrl}")
    String websocketUrl;

    @Resource
    PersonMapper personMapper;

    @Resource
    GroupMapper groupMapper;

    @Resource
    GroupUserMapper groupUserMapper;

    @Resource
    FriendMapper friendMapper;

    @Autowired
    RedisUtil r;

    @Autowired
    ListenAndSend listenAndSend;

    //html加载后先获取系统参数
    @RequestMapping("/getHtmlparam")
    public Result getHtmlParam(){
        Result result = null;
        try {
            JSONObject json=new JSONObject();
            json.put("nginxUrl",nginxUrl);
            json.put("websocketUrl",websocketUrl);
            result = Result.ok(json);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


    @RequestMapping("/getFriend/{userId}")
    public Result getFriend(@PathVariable String userId) {
        Result result = null;
        try {
            List<Person> persons = new ArrayList<>();
            List<String> friendIds=friendMapper.getFriendIdByUserId(userId);
            for (String friendId : friendIds) {
                Person user= (Person) r.get(friendId);
                persons.add(user);
            }
            result = Result.ok(persons);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/addFriend/{userIds}")
    public Result addFriend(@PathVariable String userIds) {
        Result result = null;
        try {
            String[] userIdArray = userIds.split(",");
            if(userIdArray.length!=2){
                return Result.build(500, "参数格式错误");
            }
            String userId1=userIdArray[0].compareTo(userIdArray[1])<0?userIdArray[0]:userIdArray[1];
            String userId2=userIdArray[0].compareTo(userIdArray[1])>0?userIdArray[0]:userIdArray[1];
            List<Friend> fList = friendMapper.getByTwoUserId(userId1, userId2);
            if(fList.size()!=0){
                return Result.build(500, "两人已是好友");
            }
            Friend f=new Friend();
            f.setUserid1(userId1);
            f.setUserid2(userId2);
            friendMapper.insert(f);
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


    @RequestMapping("/searchPerson/{input}")
    public Result searchPerson(@PathVariable String input) {
        Result result = null;
        try {
            List<Person> users=new ArrayList<>();
            String name="%"+input+"%";
            List<String> userIds = personMapper.searchByNameOrUnitname(name);
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
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

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
//            System.out.println(JSON.toJSONString(g));
            groupMapper.updateByPrimaryKeySelective(g);
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
            GroupUser gu = new GroupUser();
            gu.setIsValid(1);
            gu.setCreateTime(new Date());
            gu.setGroupid(uuid);
            for (String userId : userIdArray) {
                gu.setUserid(userId);
                groupUserMapper.insertSelective(gu);
            }
            JSONObject uuidJson = new JSONObject();
            uuidJson.put("uuid", uuid);
            result = Result.ok(uuidJson);
        } catch (Exception e) {
            e.printStackTrace();
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
                    continue;
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

    /** 旧
     *  获取所有人
     */
    @RequestMapping("/getFriends")
    public Result getFriends() {
        Result result = null;
        try {
            List<Person> persons = (List<Person>) r.get("persons");
            result = Result.ok(persons);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getRecord")
    public Result getRecordAndNotify(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            String key="";
            if("single".equalsIgnoreCase(type)){
                key=src.compareTo(dst)<0?src+"."+dst:dst+"."+src;
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
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

}
