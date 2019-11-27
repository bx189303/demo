package haidian.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.DateUtil;
import haidian.chat.util.Response;
import haidian.chat.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class MainController {

    @Autowired
    RedisUtil r;

    @Autowired
    private StringRedisTemplate template;

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
        msg.put("receiveTime", DateUtil.getDateToStrings(new Date()));
        JSONObject data=msg.getJSONObject("data");
        String uuid=""+ UUID.randomUUID();
        data.put("uuid",uuid);
        data.put("readCount",0);
        //添加人员信息
        String src=data.getString("src");
        String dst=data.getString("dst");
        Person srcInfo= (Person) r.get(src);
        Person dstInfo=(Person) r.get(dst);
        data.put("src",srcInfo);
        data.put("dst",dstInfo);

        System.out.println("添加字段后msg："+msg);

        //发送kafka  r_msg
//        redisOnKafka(msg);//模拟已经监听到kafka消息
        template.convertAndSend("RECEIVE",JSON.toJSONString(msg));

        //返回uuid
        JSONObject uuidJson=new JSONObject();
        uuidJson.put("uuid",uuid);
        return Result.ok(uuidJson);
    }

    public void redisOnKafka(JSONObject msg){
        //处理
        try {
            JSONObject data=msg.getJSONObject("data");
            String type=data.getString("type");
            String src=data.getString("src");
            String dst=data.getString("dst");
            //存redis
            String key="";
            if("single".equalsIgnoreCase(type)){
                key=src.compareTo(dst)>0?src+"."+dst:dst+"."+src;
            }else if("group".equalsIgnoreCase(type)){
                key=dst;
            }
            r.lSet(key,msg);
            //发送
            if("single".equalsIgnoreCase(type)){
                if (r.get(src)!=null){
                    //发送kafka
                    System.out.println(msg);
                };
            }else if("group".equalsIgnoreCase(type)){
                //模拟查库读取群组成员
                List<String> users=new ArrayList<>();
                users.add("A");
                users.add("B");
                users.add("C");
                for (String user : users) {
                    if (!src.equalsIgnoreCase(user)){
                        JSONObject toMsg=msg;
                        JSONObject toMsgData=toMsg.getJSONObject("data");
                        toMsgData.put("group",dst);
                        toMsgData.put("dst",user);
                        toMsgData.put("type","single");
                        //发送kafka
                        System.out.println("群转人："+toMsg);
                    }
                }
            }

            //发送响应成功
            Response response=Response.ok("response",msg);
            System.out.println("成功响应"+JSON.toJSONString(response));
        }catch (Exception e){
            e.printStackTrace();
            //发送响应失败
            Response response=Response.build("response",500,e.getMessage(),msg);
            System.out.println("失败响应"+JSON.toJSONString(response));
        }

    }

    public Result sendNotify(JSONObject notify){
        //添加字段
        notify.put("readCount",0);
        notify.put("isValid",1);
        //发送kafka   r_notify

        return Result.ok();
    }

    public Result onoff(JSONObject notify){
        //添加字段
        notify.put("readCount",0);
        notify.put("isValid",1);
        //发送kafka   onoff


        return Result.ok();
    }





}
