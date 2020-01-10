package haidian.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.MessageMapper;
import haidian.chat.pojo.Message;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HisRecordService {

    @Resource
    MessageMapper messageMapper;

    @Autowired
    RedisUtil r;


    public List<Object> getMessageByGroupUser(String groupId,String groupUser,int page,int size){
        List<Object> res=new ArrayList<>();
        int start=(page-1)*size;
        int end=size;
        List<Message> messages = messageMapper.getMessageByGroupUser(groupId, groupUser,start,end);
        if(messages.size()!=0){
            for (Message message : messages) {
                JSONObject msg= JSON.parseObject(JSON.toJSONString(message));
                msg.put("src",r.get(message.getSrc()));
                msg.put("src",r.get(message.getSrc()));
                msg.put("sendtime", DateUtil.getDateTimeToString(message.getSendtime()));
                res.add(msg);
            }
        }
        return res;
    }

    public List<Object> getMessageByFile(String src,String dst,String dstType,String fileType,int page, int size) {
        List<Object> res=new ArrayList<>();
        int start=(page-1)*size;
        int end=size;
        List<Message> messages=null;
        if(("single").equals(dstType)){
            messages = messageMapper.getSingleMessageByFile(src,dst,dstType,fileType,start,end);
        }else if("group".equals(dstType)){
            messages = messageMapper.getGroupMessageByFile(src,dst,dstType,fileType,start,end);
        }
        if(messages.size()!=0){
            for (Message message : messages) {
                JSONObject msg= JSON.parseObject(JSON.toJSONString(message));
                msg.put("src",r.get(message.getSrc()));
                msg.put("src",r.get(message.getSrc()));
                msg.put("sendtime", DateUtil.getDateTimeToString(message.getSendtime()));
                res.add(msg);
            }
        }
        return res;
    }

    public List<Object> getMessageByDate(String src, String dst, String type, String date, int page, int size) {
        List<Object> res=new ArrayList<>();
        List<Message> messages=null;
        int start=(page-1)*size;
        int end=size;
        if(("single").equals(type)){
            messages = messageMapper.getSingleMessageByDate(src,dst,type,date,start,end);
        }else if("group".equals(type)){
            messages = messageMapper.getGroupMessageByDate(src,dst,type,date,start,end);
        }
        if(messages.size()!=0){
            for (Message message : messages) {
                JSONObject msg= JSON.parseObject(JSON.toJSONString(message));
                msg.put("src",r.get(message.getSrc()));
                msg.put("src",r.get(message.getSrc()));
                msg.put("sendtime", DateUtil.getDateTimeToString(message.getSendtime()));
                res.add(msg);
            }
        }
        return res;
    }

    public List<Object> getMessageByDateLeft(String src, String dst, String type, String date, int page, int size) {
        List<Object> res=new ArrayList<>();
        List<Message> messages=null;
        int start=(page-1)*size;
        int end=size;
        if(("single").equals(type)){
            messages = messageMapper.getSingleMessageByDateLeft(src,dst,type,date,start,end);
        }else if("group".equals(type)){
            messages = messageMapper.getGroupMessageByDateLeft(src,dst,type,date,start,end);
        }
        if(messages.size()!=0){
            for (Message message : messages) {
                JSONObject msg= JSON.parseObject(JSON.toJSONString(message));
                msg.put("src",r.get(message.getSrc()));
                msg.put("src",r.get(message.getSrc()));
                msg.put("sendtime", DateUtil.getDateTimeToString(message.getSendtime()));
                res.add(msg);
            }
        }
        return res;
    }

    public List<Object> getMessageByContent(String src, String dst, String type, String content, int page, int size) {
        List<Object> res=new ArrayList<>();
        int start=(page-1)*size;
        int end=size;
        content="%"+content+"%";
        List<Message> messages=null;
        if(("single").equals(type)){
            messages = messageMapper.getSingleMessageByContent(src,dst,type,content,start,end);
        }else if("group".equals(type)){
            messages = messageMapper.getGroupMessageByContent(src,dst,type,content,start,end);
        }
        if(messages.size()!=0){
            for (Message message : messages) {
                JSONObject msg= JSON.parseObject(JSON.toJSONString(message));
                msg.put("src",r.get(message.getSrc()));
                msg.put("src",r.get(message.getSrc()));
                msg.put("sendtime", DateUtil.getDateTimeToString(message.getSendtime()));
                res.add(msg);
            }
        }
        return res;
    }


}
