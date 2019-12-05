package haidian.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.controller.ExtraController;
import haidian.chat.dao.GroupMapper;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.DateUtil;
import haidian.chat.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static haidian.chat.util.httpUtil.sendPostRequest;

@Service
public class ListenAndSend {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${notifyUrl}")
    String notifyUrl;

    @Value("${notifyUrlSysId}")
    String notifyUrlSysId;

    @Value("${notifyUrlSysName}")
    String notifyUrlSysName;

    @Value("${serverHost}")
    String serverHost;

    @Value("${serverPort}")
    String serverPort;

    @Autowired
    RedisUtil r;

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    ExtraController extraController;

    @Resource
    GroupMapper groupMapper;

    public void listenAndSend(String msg){
        String type=JSON.parseObject(msg).getString("type");
//        System.out.println("监听RECEIVE-"+type+" : "+msg);
        log.info("监听RECEIVE-"+type+" : "+msg);
        if("msg".equalsIgnoreCase(type)){
            sendMsg(msg);
        }else if("notify".equalsIgnoreCase(type)){
            sendNotify(msg);
        }else if("onoff".equalsIgnoreCase(type)){
            saveOnoff(msg);
        }
    }

    public void saveOnoff(String message) {
        //上线存入 id+"on",下线删除
        try {
            JSONObject onoff=JSON.parseObject(message);
            JSONObject data=onoff.getJSONObject("data");
            String onoffType=data.getString("type");
            String userId=data.getString("userId");
            String onoffKey=userId+"on";
            if(("on").equals(onoffType)){
                r.set(onoffKey,1);
            }else if("off".equals(onoffType)){
                r.del(onoffKey);
            }
        }catch (Exception e){
            e.printStackTrace();
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
            //1.更新redis
            String key="";
            if("single".equalsIgnoreCase(type)){
                key=srcId.compareTo(dstId)<0?srcId+"."+dstId:dstId+"."+srcId;
            }else if("group".equalsIgnoreCase(type)){
                String groupId=data.getString("groupId");
                key=groupId;
            }
//            List<Object> records = r.lGet(key, 0, r.lGetListSize(key));
            List<Object> records = r.lGet(key, 0, -1);
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
            //2.如果在线则发送DISPATCH，通知已读
            if(r.get(dstId)!=null){
                template.convertAndSend("DISPATCH",JSON.toJSONString(msg));
            }
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
            String srcName=data.getJSONObject("src").getString("sName");
            String dstId="";
            //1.存redis
            String key="";
            if("single".equalsIgnoreCase(type)){
                dstId=data.getJSONObject("dst").getString("sId");
                key=srcId.compareTo(dstId)<0?srcId+"."+dstId:dstId+"."+srcId;
                //如果没有记录则添加好友
                if (r.lGet(key, 0, 1).size()==0){
                    extraController.addFriend(key.replace(".",","));
                }
            }else if("group".equalsIgnoreCase(type)){
                dstId=data.getJSONObject("dst").getString("id");
                key=dstId;
            }
            r.lSet(key,msg);
            //2.发送
            if("single".equalsIgnoreCase(type)){
                if (r.get(dstId+"on")!=null){//如果用户在线则发送topic
                    //发送kafka
//                    System.out.println("发送DISPATCH ： "+msg);
                    template.convertAndSend("DISPATCH",JSON.toJSONString(msg));
                }else{
//                    System.out.println(data.getJSONObject("dst").getString("sName")+"离线,转发接口");
                    //如果不在线则发送第三方接口
                    String[] dstArray={dstId};
                    sendMsgOff(srcId,srcName,dstArray);
                }
            }else if("group".equalsIgnoreCase(type)){
                //查群内成员
                List<String> users=groupMapper.getUserByGroupId(dstId);
                //修改msg
                JSONObject toMsg=msg;
                JSONObject toMsgData=toMsg.getJSONObject("data");
                //添加group属性为dst
                toMsgData.put("group",data.getJSONObject("dst"));
//                toMsgData.put("type","single");
                //离线用户集合
                List<String> offUserId=new ArrayList<>();
                for (String user : users) {
                    if (srcId.equalsIgnoreCase(user)){
                        continue;//遍历群成员，如果是自己则不转发
                    }
                    if(r.get(user+"on")!=null){
                        //修改dst为群内成员
                        toMsgData.put("dst",r.get(user));
                        //如果用户在线则发送kafka
//                    System.out.println("群转人发送DISPATCH ："+toMsg);
                        template.convertAndSend("DISPATCH",JSON.toJSONString(toMsg));
                    }else{
//                        System.out.println(r.get(user)+"离线,转发接口");
                        offUserId.add(user);
                    }
                }
                if(offUserId.size()>0){
                    //如果不在线则发送第三方接口
                    sendMsgOff(srcId,srcName,offUserId.toArray());
                }
            }
            //返回响应成功到发送客户端
            Response response=Response.ok("response",message);
//            System.out.println("成功响应 : "+ JSON.toJSONString(response));
        }catch (Exception e){
            e.printStackTrace();
            //发送响应失败
            Response response=Response.build("response",500,e.getMessage(),message);
            System.out.println("失败响应 : "+JSON.toJSONString(response));
        }
    }

    //对面离线时发送给第三方接口
    public void sendMsgOff(String src,String srcName,Object[] dst){
//        System.out.println("发送接口-src:"+src+",srcName:"+srcName+",dst:"+dst);
        Map<String,Object> map=new HashMap<>();
        map.put("SysId",notifyUrlSysId);//指定
        map.put("SysName",notifyUrlSysName);//指定
        map.put("PushId",src);
        map.put("PushName",srcName);
        map.put("Time", DateUtil.getDateTimeToString(new Date()));
        map.put("Accepts",dst);
        map.put("Target",serverHost+"/"+serverPort+"/chat/index.html?id=");
        map.put("Title","");
        System.out.println("发送门户接口参数："+ JSON.toJSONString(map));
        if(!StringUtils.isBlank(notifyUrlSysId)&&!StringUtils.isBlank(notifyUrlSysName)&&!StringUtils.isBlank(notifyUrl)){
            sendPostRequest(notifyUrl,map);
        }
    }
}
