package haidian.chat.controller;

import haidian.chat.dao.FriendMapper;
import haidian.chat.pojo.Friend;
import haidian.chat.pojo.Person;
import haidian.chat.redis.util.RedisUtil;
import haidian.chat.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    FriendMapper friendMapper;

    @Autowired
    RedisUtil r;

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

    /** 旧
     *  获取所有人
     */
//    @RequestMapping("/getFriends")
//    public Result getFriends() {
//        Result result = null;
//        try {
//            List<Person> persons = (List<Person>) r.get("persons");
//            result = Result.ok(persons);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result = Result.build(500, e.getMessage());
//        }
//        return result;
//    }
}
