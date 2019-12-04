package haidian.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
@Component
public class WebSocketController {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //新：使用map对象，便于根据userId来获取对应的WebSocket
    private static ConcurrentHashMap<String, WebSocketController> websocketList=new ConcurrentHashMap<>();
    //接收id
    private String userId="";

    private static RedisUtil redisUtil;

    private static StringRedisTemplate stringRedisTemplate;

    /**
     * 监听DISPATCH
     */
    public void sendMsg(String message) throws IOException {
        JSONObject msg=JSON.parseObject(message);
        String msgType=msg.getString("type");
        JSONObject data=msg.getJSONObject("data");
        System.out.println("监听DISPATCH-"+msgType+" : "+message);
        String dstId="";
        if(msgType.equals("msg")){
            dstId=data.getJSONObject("dst").getString("sId");
        }else if(msgType.equals("notify")){
            dstId=data.getString("dst");
        }
        WebSocketController dstCli= websocketList.get(dstId);
        if(dstCli!=null){
            dstCli.sendMessage(message);
        }
    }

    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) throws IOException {
        this.session = session;
        websocketList.put(userId, this);
        this.userId=userId;
        //发送上线事件给RECEIVE
        JSONObject onoff=new JSONObject();
        onoff.put("type","onoff");
        onoff.put("sendTime", DateUtil.getDateToStrings(new Date()));
        onoff.put("isValid",1);
        onoff.put("receiveTime", DateUtil.getDateToStrings(new Date()));
        JSONObject data=new JSONObject();
        data.put("type","on");
        data.put("userId",userId);
        onoff.put("data",data);
        stringRedisTemplate.convertAndSend("RECEIVE",JSON.toJSONString(onoff));
    }



    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
//        System.out.println(userId+"下线");
        //发送下线事件给RECEIVE
        JSONObject onoff=new JSONObject();
        onoff.put("type","onoff");
        onoff.put("sendTime", DateUtil.getDateToStrings(new Date()));
        onoff.put("isValid",1);
        onoff.put("receiveTime", DateUtil.getDateToStrings(new Date()));
        JSONObject data=new JSONObject();
        data.put("type","off");
        data.put("userId",userId);
        onoff.put("data",data);
        stringRedisTemplate.convertAndSend("RECEIVE",JSON.toJSONString(onoff));

    }


    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println(("发生错误"));
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    // 静态方法、在SpringBoot启动时被调用，注入bean
    public static void setRedisUtil(RedisUtil redisUtil) {
        WebSocketController.redisUtil = redisUtil;
    }
    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        WebSocketController.stringRedisTemplate = stringRedisTemplate;
    }
}
