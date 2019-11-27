package haidian.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.redis.RedisUtil;
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

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount=0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //新：使用map对象，便于根据userId来获取对应的WebSocket
    private static ConcurrentHashMap<String, WebSocketController> websocketList=new ConcurrentHashMap<>();
    //接收id
    private String userId="";

    private static RedisUtil redisUtil;

    private static StringRedisTemplate stringRedisTemplate;

    public void sendMsg(String message) throws IOException {
        System.out.println("监听dispatch : "+message);
        JSONObject msg=JSON.parseObject(message);
        JSONObject data=msg.getJSONObject("data");
//        String dst=data.getString("dst");//旧
        String dst=data.getJSONObject("dst").getString("sId");
        WebSocketController dstCli= websocketList.get(dst);
        dstCli.sendMessage(message);
    }

    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) throws IOException {
        this.session = session;
        websocketList.put(userId, this);
    }



    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(websocketList.get(this.userId)!=null){
            websocketList.remove(this.userId);
            subOnlineCount();           //在线数减1
            System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
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


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }

    // 静态方法、在SpringBoot启动时被调用，注入bean
    public static void setRedisUtil(RedisUtil redisUtil) {
        WebSocketController.redisUtil = redisUtil;
    }
    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        WebSocketController.stringRedisTemplate = stringRedisTemplate;
    }
}
