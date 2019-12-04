package haidian.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.GroupMapper;
import haidian.chat.pojo.Group;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RedisTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisUtil r;

    @Resource
    GroupMapper groupMapper;

    @Test
    public void autotest(){
        r.del("0005");
        System.out.println(r.get("0005")==null);
    }


    @Test
    public void indexTest(){
        long t1= System.currentTimeMillis();
        String userId="0001";
        List<JSONObject> list=new ArrayList<>();
        //查组
        List<Group> groups = groupMapper.getByUserId(userId);
//        JSONObject json=new JSONObject();
//        json.put("type","gourp");
        for (Group group : groups) {//遍历组
            if(!r.hasKey(group.getId())){//如果没有记录则跳出
                break;
            }
            JSONObject json=new JSONObject();
            json.put("type","gourp");
            json.put("dst",group);
            //查redis中每个组的记录
            List<Object> records = r.lGet(group.getId(), 0, r.lGetListSize(group.getId()));
            int notice=0;
            for (int i = records.size()-1; i >=0 ; i--) {//倒序遍历组的记录
                JSONObject record = (JSONObject) records.get(i);
                if(i==records.size()-1){//记录最后一条的数据
                    json.put("lastTime",record.getString("sendTime"));
                    json.put("content",record.getJSONObject("data").getJSONObject("content"));
                }
                //判断几条未读，如果遍历到已读则跳出循环
                String recordSrcId=record.getJSONObject("data").getJSONObject("src").getString("sId");
                if(recordSrcId.equalsIgnoreCase(userId)){//如果是自己发送的消息，则结束当前循环
                    continue;
                };
                String readIds = record.getJSONObject("data").getString("readId");
                if(readIds.indexOf(userId)!=-1){//如果已读id有自己则跳出
                    break;
                }
                notice+=1;
                if(notice==10){//最多显示10条未读
                    break;
                }
            }
            json.put("notice",notice);
            list.add(json);
        }
        System.out.println("遍历完组： "+JSON.toJSONString(list));
        //查两人对话
        Set<String> singleIds = r.keys("*" + userId + "*");
        Iterator<String>  it = singleIds.iterator();
        while(it.hasNext()){
            String id = it.next();
            if(id.indexOf(".")==-1){
                it.remove();
            }
        }
        for (String singleId : singleIds) {
            JSONObject json=new JSONObject();
            json.put("type","single");
            //查redis中每个对话的记录
            List<Object> records = r.lGet(singleId, 0, r.lGetListSize(singleId));
            int notice=0;
            for (int i = records.size()-1; i >=0 ; i--) {
                JSONObject record = (JSONObject) records.get(i);
                if(i==records.size()-1){ //记录最后一条的数据
                    json.put("lastTime",record.getString("sendTime"));
                    json.put("content",record.getJSONObject("data").getJSONObject("content"));
                    JSONObject src = record.getJSONObject("data").getJSONObject("src");
                    JSONObject dst = record.getJSONObject("data").getJSONObject("dst");
                    if(userId.equalsIgnoreCase(src.getString("sId"))){
                        json.put("dst",src);
                    }else if(userId.equalsIgnoreCase(dst.getString("sId"))){
                        json.put("dst",dst);
                    }
                }
                //判断几条未读，如果遍历到已读则跳出循环
                String recordSrcId=record.getJSONObject("data").getJSONObject("src").getString("sId");
                if(recordSrcId.equalsIgnoreCase(userId)){ //如果是自己发送的消息，则结束当前循环
                    continue;
                };
                String readIds = record.getJSONObject("data").getString("readId");
                if(readIds.indexOf(userId)!=-1){ //如果已读id有自己则跳出
                    break;
                }
                notice+=1;
                if(notice==10){//最多显示10条未读
                    break;
                }
            }
            json.put("notice",notice);
            list.add(json);
        }
        System.out.println("两个合并后："+JSON.toJSONString(list));
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject j1, JSONObject j2) {
                Date d1= DateUtil.getDateTime(j1.getString("lastTime"));
                Date d2= DateUtil.getDateTime(j2.getString("lastTime"));
                if (d1.getTime() > d2.getTime()) {
                    return -1;
                } else if (d1.getTime() <d2.getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        System.out.println("排序后："+JSON.toJSONString(list));
        long t2= System.currentTimeMillis();
        System.out.println("耗时： "+new Double(t2-t1)/1000+" s");

    }


    @Test
    public void listsettest(){
        r.lSet("list","a");
        r.lSet("list","b");
        r.lSet("list","c");
        r.lSet("list","z");
        List<Object> objects = r.lGet("G2", 0, r.lGetListSize("G2"));
        System.out.println(JSON.toJSONString(objects));
    }

    @Test
    public void listupdatetest(){
        long t1= System.currentTimeMillis();
        List<Object> list = r.lGet("list", 0, r.lGetListSize("list"));
        for (int i = 0; i < list.size() ; i++) {
            String v= (String) list.get(i);
            if (v.equals("z")){
                r.lUpdateIndex("list",i,"替换");
            }
        }
        long t2= System.currentTimeMillis();
        System.out.println(JSON.toJSONString(list));
        System.out.println(new Double(t2-t1)/1000);
    }

    @Test
    public void setTest() {
        r.set("testchat","测试插入2");
        System.out.println(r.get("test"));
    }

    @Test
    void haskeytest(){
        System.out.println(r.hasKey("testabc"));
        System.out.println(r.hasKey("testchat"));
    }

    @Test
    void getkeytest(){
        long t1= System.currentTimeMillis();
//        Set<String> keys=r.keys("chat");
//        for (String key : keys) {
//            System.out.println(key);
//        }
        Set<String> keys = redisTemplate.keys("null");
        System.out.println(JSON.toJSONString(keys));
        long t2= System.currentTimeMillis();
        System.out.println("第一次时间："+ new Double(t2-t1)/1000);
        keys = redisTemplate.keys("*25*");
        System.out.println(JSON.toJSONString(keys));
        long t3=System.currentTimeMillis();
        System.out.println("双*时间："+ new Double(t3-t2)/1000);
//        keys = redisTemplate.keys("chat");
//        System.out.println(JSON.toJSONString(keys));
        for (String key : keys) {
            r.get(key);
        }
        long t4=System.currentTimeMillis();
        System.out.println("无*时间："+ new Double(t4-t3)/1000);
    }

    @Test
    public void keystest(){
        long t1= System.currentTimeMillis();
        Set<String> keys = r.keys(25+"*");
        Set<String> keys2 = r.keys("*"+25);
        keys.addAll(keys2);
        long t2= System.currentTimeMillis();
        System.out.println("合并前后*时间："+ new Double(t2-t1)/1000);
    }

    @Test
    void settest(){
        for (int i = 0; i < 10000; i++) {
            String key=UUID.randomUUID()+"";
            r.set(key,"test");
        }

    }

}
