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

    public void sendMsg(String message){
        System.out.println("监听dispatch : "+message);

    }

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) throws IOException {
        this.session=session;
        websocketList.put(userId,this);
        addOnlineCount();
        System.out.println("有新窗口开始监听:"+userId+",当前在线人数为" + getOnlineCount());
        this.userId=userId;
        //从redis读取最近的历史记录
        long t1= System.currentTimeMillis();
        Set<String> keys = redisUtil.keys("*"+userId+"*");
        if (!keys.isEmpty()) {
            for (String key : keys) {
                List<JSONObject> chatList = (List<JSONObject>) redisUtil.get(key);
                sendMessage(JSON.toJSONString(chatList));
                //遍历查未读
                for (int i = 0; i < chatList.size(); i++) {
                    JSONObject chat=chatList.get(i);
                    int read = chat.getIntValue("read");
                    if(read==1){
                        //通知发送人已读
                        //更改为已读
                        chat.put("read",0);
                        chatList.set(i,chat);
                    }
                }
                redisUtil.set(key,chatList);
                System.out.println(JSON.toJSONString(chatList));
            }
        }
        System.out.println("读取redis用时："+ new Double(System.currentTimeMillis()-t1)/1000);
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

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(("收到来自窗口"+userId+"的信息:"+message));
        if(StringUtils.isNotBlank(message)){
            JSONArray list=JSONArray.parseArray(message);
            for (int i = 0; i < list.size(); i++) {
                try {
                    //解析发送的报文
                    JSONObject object = list.getJSONObject(i);
                    String fromUserId=object.getString("fromUserId");
                    String toUserId=object.getString("toUserId");
                    String contentText=object.getString("contentText");
                    //在记录基础上添加字段
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    object.put("sendTime",sdf.format(new Date()));
                    object.put("uuid",UUID.randomUUID()+"");
//                    object.put("fromUserId",this.userId);
                    //传送给对应用户的websocket
                    if(StringUtils.isNotBlank(toUserId)&&StringUtils.isNotBlank(contentText)){
                        WebSocketController toSocket=websocketList.get(toUserId);
                        //需要进行转换，userId
                        if(toSocket!=null){
                            toSocket.sendMessage(JSON.toJSONString((object)));
                            object.put("read",0);
                            //此处可以放置相关业务代码，例如存储到数据库
                            //存到redis
                            RedisThread.addChatList(object);
                        }
                        if(toSocket==null){
                            sendMessage("对面离线");
                            WebSocketController fromSocket=websocketList.get(fromUserId);

                            object.put("read",1);
                            RedisThread.addChatList(object);
                        }
                        System.out.println(JSON.toJSONString(object));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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
