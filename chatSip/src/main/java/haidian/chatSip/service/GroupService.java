package haidian.chatSip.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chatSip.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GroupService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void sendGroupNotify(String type,String groupId,String userIds){
        JSONObject groupNotify =new JSONObject();
        groupNotify.put("type","groupNotify");
        groupNotify.put("sendTime", DateUtil.getDateToStrings(new Date()));
        groupNotify.put("isValid",1);
        groupNotify.put("receiveTime", DateUtil.getDateToStrings(new Date()));
        JSONObject data=new JSONObject();
        data.put("type",type);
        data.put("groupId",groupId);
        if("addUser".equals(type)){
            data.put("userIds",userIds);
        }
        groupNotify.put("data",data);
        redisTemplate.convertAndSend("RECEIVE", JSON.toJSONString(groupNotify));
    }
}
