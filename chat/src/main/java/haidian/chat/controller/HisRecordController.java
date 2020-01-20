package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.redis.util.RedisUtil;
import haidian.chat.service.HisRecordService;
import haidian.chat.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HisRecordController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HisRecordService hisRecordService;

    @Autowired
    RedisUtil r;


    @RequestMapping("/getRecordByGroupUser")
    public Result getRecordByGroupUser(@RequestBody JSONObject json){
        Result result=null;
        try {
            String groupId=json.getString("groupId");
            String groupUserId=json.getString("groupUserId");
            String type=json.getString("type");
            int page=json.getInteger("page");
            int size=json.getInteger("size");
            List<Object> messages = hisRecordService.getMessageByGroupUser(groupId, groupUserId,page,size);
            result=Result.ok(messages);
        }catch (Exception e){
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getRecordByFile")
    public Result getRecordByFile(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            String fileType=json.getString("fileType");
            int size=json.getInteger("size");
            int page=json.getInteger("page");
            List<Object> messages = hisRecordService.getMessageByFile(src,dst,type,fileType,page,size);
            result=Result.ok(messages);
        }catch (Exception e){
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getRecordByDate")
    public Result getRecordByDate(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            String date=json.getString("date");
            int size=json.getInteger("size");
            int page=json.getInteger("page");
            List<Object> messages=null;
            messages = hisRecordService.getMessageByDate(src,dst,type,date,page,size);
            result=Result.ok(messages);
        }catch (Exception e){
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/getRecordByDateLeft")
    public Result getRecordByDateLeft(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            String date=json.getString("date");
            int size=json.getInteger("size");
            int page=json.getInteger("page");
            List<Object> messages=null;
            messages = hisRecordService.getMessageByDateLeft(src,dst,type,date,page,size);
            result=Result.ok(messages);
        }catch (Exception e){
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getRecordByContent")
    public Result getRecordByContent(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            String content=json.getString("content");
            int size=json.getInteger("size");
            int page=json.getInteger("page");
            List<Object> messages=null;
            messages = hisRecordService.getMessageByContent(src,dst,type,content,page,size);
            result=Result.ok(messages);
        }catch (Exception e){
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }
}
