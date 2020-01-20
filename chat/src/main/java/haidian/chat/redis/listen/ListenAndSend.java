package haidian.chat.redis.listen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.controller.FriendController;
import haidian.chat.dao.GroupMapper;
import haidian.chat.redis.util.RedisUtil;
import haidian.chat.service.ZxFileService;
import haidian.chat.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ListenAndSend {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtil r;

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    FriendController friendController;

    @Resource
    GroupMapper groupMapper;

    @Autowired
    ZxFileService zxFileService;

    /**
     *  监听RECEIVE的方法,redis中存接受到的消息，处理后发送到指定topic
     */
    public void listenAndSend(String msg){
        String type=JSON.parseObject(msg).getString("type");
        log.info("监听RECEIVE-"+type+" : "+msg);
        if("msg".equalsIgnoreCase(type)){
            sendMsg(msg);
        }else if("notify".equalsIgnoreCase(type)){
            sendNotify(msg);
        }else if("onoff".equalsIgnoreCase(type)){
            saveOnoff(msg);
        }else if("groupNotify".equalsIgnoreCase(type)){
            sendGroupNotify(msg);
        }
    }

    private void sendGroupNotify(String msg) {
        try {
            JSONObject groupNotify=JSON.parseObject(msg);
            JSONObject data=groupNotify.getJSONObject("data");
            String groupNotifyType=data.getString("type");
            String groupId=data.getString("groupId");
            if("addUser".equals(groupNotifyType)){
                String userIds=data.getString("userIds");
                String[] idArray = userIds.split(",");
                for (String id : idArray) {
                    if (r.get(id+"on")!=null){ //如果在线则发送群通知
                        JSONObject toGroupNotify=groupNotify;
                        JSONObject toGroupNotifyData=data;
                        toGroupNotifyData.remove("userIds");
                        toGroupNotifyData.put("dst",id);
                        toGroupNotify.put("data",toGroupNotifyData);
                        template.convertAndSend("DISPATCH",JSON.toJSONString(toGroupNotify));
                    }
                }
            }else if("updateGroup".equals(groupNotifyType)){
                List<String> userIds = groupMapper.getUserByGroupId(groupId);
                for (String id : userIds) {
                    if (r.get(id+"on")!=null) { //如果在线则发送群通知
                        JSONObject toGroupNotify = groupNotify;
                        JSONObject toGroupNotifyData = data;
                        toGroupNotifyData.put("dst", id);
                        toGroupNotify.put("data", toGroupNotifyData);
                        template.convertAndSend("DISPATCH", JSON.toJSONString(toGroupNotify));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveOnoff(String message) {
        //上线时redis中的websocketon存入id,下线时删除
        try {
            JSONObject onoff=JSON.parseObject(message);
            JSONObject data=onoff.getJSONObject("data");
            String onoffType=data.getString("type");
            String userId=data.getString("userId");
            if(("on").equals(onoffType)){
                r.hset("websocketon",userId,1);
            }else if("off".equals(onoffType)){
                r.hdel("websocketon",userId);
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

            //总线文件处理,下载来自二类网的文件，上传来自三类网的文件
            String terminal=msg.getString("terminal");
            if("2nApp".equals(terminal)){
                //处理来自二类网的文件
                zxFileService.zxDownloadFile(data);
            }else if("3nPc".equals(terminal)){
                //总线上传文件，并添加字段
                data=zxFileService.zxUploadFile(data);
                log.info("总线上传文件后msg："+msg);
            }


            //1.存redis
            String key="";
            if("single".equalsIgnoreCase(type)){
                dstId=data.getJSONObject("dst").getString("sId");
                key=srcId.compareTo(dstId)<0?srcId+"."+dstId:dstId+"."+srcId;
                //如果没有记录则添加好友
                if (r.lGet(key, 0, 1).size()==0){
                    friendController.addFriend(key.replace(".",","));
                }
            }else if("group".equalsIgnoreCase(type)){
                dstId=data.getJSONObject("dst").getString("id");
                key=dstId;
            }
            r.lSet(key,msg);
            //2.发送
            if("single".equalsIgnoreCase(type)){
                template.convertAndSend("DISPATCH",JSON.toJSONString(msg));
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
                    //修改dst为群内成员,并发送到指定topic
                    toMsgData.put("dst",r.get(user));
                    template.convertAndSend("DISPATCH",JSON.toJSONString(toMsg));
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

}
