package haidian.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListenAndSend {

    @Autowired
    RedisUtil r;

    @Autowired
    private StringRedisTemplate template;

    public void sendMsg(String message){
        System.out.println("监听receive ： "+message);
//        if(true)return;
        JSONObject msg=JSON.parseObject(message);
        //处理
        try {
            JSONObject data=msg.getJSONObject("data");
            String type=data.getString("type");
//            String src=data.getString("src");//旧
//            String dst=data.getString("dst");//旧
            String src=data.getJSONObject("src").getString("sId");
            String dst=data.getJSONObject("dst").getString("sId");
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
                if (r.get(src)!=null){//如果用户在线则发送topic
                    //发送kafka
//                    System.out.println(msg);
                    template.convertAndSend("DISPATCH",JSON.toJSONString(msg));
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
            System.out.println("成功响应 : "+ JSON.toJSONString(response));
        }catch (Exception e){
            e.printStackTrace();
            //发送响应失败
            Response response=Response.build("response",500,e.getMessage(),msg);
            System.out.println("失败响应 : "+JSON.toJSONString(response));
        }

    }
}
