package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.redis.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class RedisThread implements Runnable {

    private static RedisUtil redisUtil;

    private static Queue<Object> chatList = new LinkedList<Object>();//聊天记录队列

    static{
        new Thread(new RedisThread()).start();
    }

    // 静态方法、在SpringBoot启动时被调用，注入bean
    public static void setRedisUtil(RedisUtil redisUtil) {
        RedisThread.redisUtil = redisUtil;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (chatList.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }
                while (!chatList.isEmpty()) {
                    JSONObject chat = (JSONObject) chatList.poll();
                    String fromUserId=chat.getString("fromUserId");
                    String toUserId=chat.getString("toUserId");
                    String contentText=chat.getString("contentText");
                    if(redisUtil.hasKey(fromUserId+"_"+toUserId)){
                        List<Object> chatList= (List<Object>) redisUtil.get(fromUserId+"_"+toUserId);
                        chatList.add(chat);
                        redisUtil.set(fromUserId+"_"+toUserId,chatList);
                    }else if(redisUtil.hasKey(toUserId+"_"+fromUserId)){
                        List<Object> chatList= (List<Object>) redisUtil.get(toUserId+"_"+fromUserId);
                        chatList.add(chat);
                        redisUtil.set(toUserId+"_"+fromUserId,chatList);
                    }else{
                        List<Object> chatList=new ArrayList<>();
                        chatList.add(chat);
                        String key=fromUserId+"_"+toUserId;
                        redisUtil.set(key,chatList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    public static void addChatList(Object obj){
        chatList.add(obj);
    }
}
