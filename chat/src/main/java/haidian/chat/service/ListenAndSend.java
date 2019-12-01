package haidian.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListenAndSend {

    @Autowired
    RedisUtil r;

    @Autowired
    private StringRedisTemplate template;

    @Resource
    GroupMapper groupMapper;

    public void listenAndSend(String msg){
        System.out.println("监听RECEIVE ： "+msg);
        String type=JSON.parseObject(msg).getString("type");
        if("msg".equalsIgnoreCase(type)){
            sendMsg(msg);
        }else if("notify".equalsIgnoreCase(type)){
            sendNotify(msg);
        }
    }

    public void sendNotify(String message) {
        try {
            JSONObject msg=JSON.parseObject(message);
            JSONObject data=msg.getJSONObject("data");
            String uuid=data.getString("uuid");
            String type=data.getString("type");
            String srcId=data.getString("src");//旧
            String dstId=data.getString("dst");//旧
            //更新redis
            String key="";
            if("single".equalsIgnoreCase(type)){
                key=srcId.compareTo(dstId)<0?srcId+"."+dstId:dstId+"."+srcId;
            }else if("group".equalsIgnoreCase(type)){
                String groupId=data.getString("groupId");
                key=groupId;
            }
            List<Object> records = r.lGet(key, 0, r.lGetListSize(key));
            for (int i = records.size()-1; i >=0 ; i--) {
                JSONObject record = (JSONObject) records.get(i);
                JSONObject recordData = record.getJSONObject("data");
                String recordUuid = recordData.getString("uuid");
                if(recordUuid.equalsIgnoreCase(uuid)){//根据uuid找到记录
                    String readIds = recordData.getString("readId");
                    if(readIds.indexOf(srcId)!=-1){//如果已读id中有，则跳出
                        break;
                    }else if(readIds.equals("")){//如果已读id为空则添加
                        recordData.put("readId",srcId);
                    }else {//如果有其它id，则继续添加
                        recordData.put("readId",readIds+","+srcId);
                    }
                    //替换记录后跳出
                    r.lUpdateIndex(key,i,record);
                    break;
                }
            }
            //发送DISPATCH，通知已读

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMsg(String message){
//        if(true)return;
        //处理
        try {
            JSONObject msg=JSON.parseObject(message);
            JSONObject data=msg.getJSONObject("data");
            String type=data.getString("type");
//            String src=data.getString("src");//旧
//            String dst=data.getString("dst");//旧
            String srcId=data.getJSONObject("src").getString("sId");
            String dstId="";
            //存redis
            String key="";
            if("single".equalsIgnoreCase(type)){
                dstId=data.getJSONObject("dst").getString("sId");
                key=srcId.compareTo(dstId)<0?srcId+"."+dstId:dstId+"."+srcId;
            }else if("group".equalsIgnoreCase(type)){
                dstId=data.getJSONObject("dst").getString("id");
                key=dstId;
            }
            r.lSet(key,msg);
            //发送
            if("single".equalsIgnoreCase(type)){
                if (r.get(srcId)!=null){//如果用户在线则发送topic
                    //发送kafka
//                    System.out.println("发送DISPATCH ： "+msg);
                    template.convertAndSend("DISPATCH",JSON.toJSONString(msg));
                };
            }else if("group".equalsIgnoreCase(type)){
                //查群内成员
                List<String> users=groupMapper.getUserByGroupId(dstId);
                //修改msg
                JSONObject toMsg=msg;
                JSONObject toMsgData=toMsg.getJSONObject("data");
                //添加group属性为dst
                toMsgData.put("group",data.getJSONObject("dst"));
//                toMsgData.put("type","single");
                for (String user : users) {
                    if (srcId.equalsIgnoreCase(user)){
                        continue;//遍历群成员，如果是自己则不转发
                    }
                    //修改dst为群内成员
                    toMsgData.put("dst",r.get(user));
                    //发送kafka
//                    System.out.println("群转人发送DISPATCH ："+toMsg);
                    template.convertAndSend("DISPATCH",JSON.toJSONString(toMsg));
                }
            }
            //发送响应成功
            Response response=Response.ok("response",message);
            System.out.println("成功响应 : "+ JSON.toJSONString(response));
        }catch (Exception e){
            e.printStackTrace();
            //发送响应失败
            Response response=Response.build("response",500,e.getMessage(),message);
            System.out.println("失败响应 : "+JSON.toJSONString(response));
        }

    }
}
