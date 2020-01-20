package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.pojo.Group;
import haidian.chat.redis.util.RedisUtil;
import haidian.chat.service.SendNotifyThread;
import haidian.chat.util.DateUtil;
import haidian.chat.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class RecordController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Resource
    GroupMapper groupMapper;

    @Autowired
    RedisUtil r;

    @Autowired
    MainController mainController;


    /**
     *  首页消息列表
     */
    @RequestMapping("/getIndex/{userId}")
    public Result getIndexList(@PathVariable String userId) {
        Result result = null;
        try {
            List<JSONObject> recordList = new ArrayList<>();
            //查组
            List<Group> groups = groupMapper.getByUserId(userId);
            for (Group group : groups) {//遍历组
                if (!r.hasKey(group.getId())) {//如果没有记录则跳出
                    continue;
                }
                JSONObject json = new JSONObject();
                json.put("type", "group");
                json.put("dst", group);
                //查redis中每个组的记录
                List<Object> records = r.lGet(group.getId(), 0,-1);
                int notice = 0;
                for (int i = records.size() - 1; i >= 0; i--) {//倒序遍历组的记录
                    JSONObject record = (JSONObject) records.get(i);
                    if (i == records.size() - 1) {//记录最后一条的数据
                        json.put("lastTime", record.getString("sendTime"));
                        json.put("content", record.getJSONObject("data").getJSONObject("content"));
                    }
                    //判断几条未读，如果遍历到已读则跳出循环
                    String recordSrcId=record.getJSONObject("data").getJSONObject("src").getString("sId");
                    if(recordSrcId.equalsIgnoreCase(userId)){//如果是自己发送的消息，则结束当前循环
                        continue;
                    };
                    String readIds = record.getJSONObject("data").getString("readId");
                    if (readIds.indexOf(userId) != -1) {//如果已读id有自己则跳出
                        break;
                    }
                    notice += 1;
//                    if(notice==10){//最多显示10条未读
//                        break;
//                    }
                }
                json.put("notice", notice);
                recordList.add(json);
            }
            //查两人对话
            Set<String> singleIds = r.keys("*" + userId + "*");
            Iterator<String> it = singleIds.iterator();
            while (it.hasNext()) {
                String id = it.next();
                if (id.indexOf(".") == -1) {
                    it.remove();
                }
            }
            for (String singleId : singleIds) {
                JSONObject json = new JSONObject();
                json.put("type", "single");
                //查redis中每个对话的记录
                List<Object> records = r.lGet(singleId, 0, -1);
                int notice = 0;
                for (int i = records.size() - 1; i >= 0; i--) {
                    JSONObject record = (JSONObject) records.get(i);
                    if (i == records.size() - 1) {//记录最后一条的数据
                        json.put("lastTime", record.getString("sendTime"));
                        json.put("content", record.getJSONObject("data").getJSONObject("content"));
                        //直接从redis中取数据改为取出后在查一遍
//                        JSONObject src = record.getJSONObject("data").getJSONObject("src");
//                        JSONObject dst = record.getJSONObject("data").getJSONObject("dst");
//                        if (userId.equalsIgnoreCase(src.getString("sId"))) {
//                            json.put("dst", dst);
//                        } else if (userId.equalsIgnoreCase(dst.getString("sId"))) {
//                            json.put("dst", src);
//                        }
                        String srcId = record.getJSONObject("data").getJSONObject("src").getString("sId");
                        String dstId = record.getJSONObject("data").getJSONObject("dst").getString("sId");
                        if (userId.equalsIgnoreCase(srcId)) {
                            json.put("dst", r.get(dstId));
                        } else if (userId.equalsIgnoreCase(dstId)) {
                            json.put("dst", r.get(srcId));
                        }
                    }
                    //判断几条未读，如果遍历到已读则跳出循环
                    String recordSrcId=record.getJSONObject("data").getJSONObject("src").getString("sId");
                    if(recordSrcId.equalsIgnoreCase(userId)){//未读不算自己发送的消息，如果是自己发送的消息，则结束本次循环
                        continue;
                    };
                    String readIds = record.getJSONObject("data").getString("readId");
                    if (readIds.indexOf(userId) != -1) {//如果已读id有自己则跳出
                        break;
                    }
                    notice += 1;
//                    if(notice==10){//最多显示10条未读
//                        break;
//                    }
                }
                json.put("notice", notice);
                recordList.add(json);
            }
            //按时间倒序排序
            Collections.sort(recordList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject j1, JSONObject j2) {
                    Date d1 = DateUtil.getDateTime(j1.getString("lastTime"));
                    Date d2 = DateUtil.getDateTime(j2.getString("lastTime"));
                    if (d1.getTime() > d2.getTime()) {
                        return -1;
                    } else if (d1.getTime() < d2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            result = Result.ok(recordList);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

//    @RequestMapping("/getRecord")
//    public Result getRecordAndNotify(@RequestBody JSONObject json){
//        Result result=null;
//        try {
//            String src=json.getString("src");
//            String dst=json.getString("dst");
//            String type=json.getString("type");
//            String key="";
//            if("single".equalsIgnoreCase(type)){
//                key=src.compareTo(dst)<0?src+"."+dst:dst+"."+src;
//            }else if("group".equalsIgnoreCase(type)){
//                key=dst;
//            }
//            List<Object> msgs=  r.lGet(key, 0, -1);
//            //前台收到数据进行显示，直接在后台发送已读
//            //发送notify
//            executorService.execute(new SendNotifyThread(src,msgs,mainController));
//            result=Result.ok(msgs);
//        }catch (Exception e){
//            e.printStackTrace();
//            result=Result.build(500,e.getMessage());
//        }
//        return result;
//    }

    @RequestMapping("/getRecordByPage")
    public Result getRecordByPage(@RequestBody JSONObject json){
        Result result=null;
        try {
            String src=json.getString("src");
            String dst=json.getString("dst");
            String type=json.getString("type");
            int size=json.getInteger("size");
            int page=json.getInteger("page");
            String key="";
            if("single".equalsIgnoreCase(type)){
                key=src.compareTo(dst)<0?src+"."+dst:dst+"."+src;
            }else if("group".equalsIgnoreCase(type)){
                key=dst;
            }
            int msgSize= (int) r.lGetListSize(key);
            if(page==1){ //如果第一次请求则发送notify，默认查看最近10000次
                int notifyListStart=msgSize-10000>0?msgSize-10000:0;
                List<Object> notifyMsgs=  r.lGet(key, notifyListStart, msgSize);
                executorService.execute(new SendNotifyThread(src,notifyMsgs,mainController));
            }
            int start=msgSize-(size*page)>0?msgSize-(size*page):0;
            int end=msgSize-(size*(page-1))>0?msgSize-(size*(page-1)):0;
            if(end==0){ //如果结束坐标为0，则返回
                return result.ok();
            }
            List<Object> msgs=  r.lGet(key, start, end);
            //处理redis中取出的数据
            for (Object msg : msgs) {
                JSONObject msgJson = (JSONObject) msg;
                JSONObject msgDataJson=msgJson.getJSONObject("data");
                String msgType=msgDataJson.getString("type");
                String srcId=msgDataJson.getJSONObject("src").getString("sId");
                msgDataJson.put("src",r.get(srcId));
                if("single".equals(msgType)){
                    String dstId=msgDataJson.getJSONObject("dst").getString("sId");
                    msgDataJson.put("dst",r.get(dstId));
                }else if("group".equals(msgType)){

                }
            }

            result=Result.ok(msgs);
        }catch (Exception e){
            e.printStackTrace();
            result=Result.build(500,e.getMessage());
        }
        return result;
    }
}
