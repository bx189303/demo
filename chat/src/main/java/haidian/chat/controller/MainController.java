package haidian.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.pojo.Group;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.DateUtil;
import haidian.chat.util.Response;
import haidian.chat.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class MainController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtil r;

    @Autowired
    private StringRedisTemplate template;

    @Resource
    private GroupMapper groupMapper;

    @RequestMapping("/send")
    public Result service(@RequestBody JSONObject json){
        Result result=null;
        String type=json.getString("type");
        if("msg".equals(type)){
            result=sendMsg(json);
        }else if("notify".equals(type)){
            result=sendNotify(json);
        }else if("onoff".equals(type)){
            result=onoff(json);
        }else{
            return Result.build(500,"未知type");
        }
        return result;
    }

    public Result sendMsg(JSONObject msg){
        //添加字段 uuid ,readCount ,isValid,receiveTime
        msg.put("isValid",1);
        String receiveTime=DateUtil.getDateToStrings(new Date());
        msg.put("receiveTime", receiveTime);
        JSONObject data=msg.getJSONObject("data");
        String uuid=""+ UUID.randomUUID();
        data.put("uuid",uuid);
        data.put("readId","");
        //添加人员信息
        String src=data.getString("src");
        Person srcInfo= (Person) r.get(src);
        data.put("src",srcInfo);
        String type=data.getString("type");
        String dst=data.getString("dst");
        if ("single".equalsIgnoreCase(type)) {
            Person dstInfo=(Person) r.get(dst);
            data.put("dst",dstInfo);
        }else if ("group".equalsIgnoreCase(type)) {
            Group dstInfo=groupMapper.selectByPrimaryKey(dst);
            data.put("dst",dstInfo);
        }
//        System.out.println("添加字段后msg："+msg);

        //发送kafka  r_msg
//        redisOnKafka(msg);//模拟已经监听到kafka消息
        template.convertAndSend("RECEIVE",JSON.toJSONString(msg));

        //返回uuid和接受时间
        JSONObject dataJson=new JSONObject();
        dataJson.put("uuid",uuid);
        dataJson.put("receiveTime",receiveTime);
        return Result.ok(dataJson);
    }

    public Result sendNotify(JSONObject notify){
        //添加字段
        notify.put("isValid",1);
        notify.put("receiveTime", DateUtil.getDateToStrings(new Date()));
        //发送kafka   r_notify
        //发送redis RECEIVE
        template.convertAndSend("RECEIVE",JSON.toJSONString(notify));

        return Result.ok();
    }

    public Result onoff(JSONObject notify){
        //添加字段
        notify.put("isValid",1);
        //发送kafka   onoff
        //改为在websocket监听事件处发送
        return Result.ok();
    }





}
