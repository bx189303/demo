package haidian.chatSip.redis.listen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chatSip.redis.util.RedisUtil;
import haidian.chatSip.service.SipFile;
import haidian.chatSip.service.SipServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SipService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SipServer sipServer;

    @Autowired
    SipFile sipFile;

    /**
     * 监听DISPATCH
     */
    public void sendSip(String message) throws IOException {
        JSONObject msg = JSON.parseObject(message);
        String msgType = msg.getString("type");
        JSONObject data = msg.getJSONObject("data");
        log.info("sendSip : 监听DISPATCH - " + msgType + " : " + message);
        String srcNum ="";
        String dstNum = "";
        if (msgType.equals("msg")) {
            srcNum = data.getJSONObject("src").getString("sPolicenum");
            dstNum = data.getJSONObject("dst").getString("sPolicenum");
        } else if (msgType.equals("notify") || msgType.equals("groupNotify")) {
            String srcId = data.getString("src");
            String dstId = data.getString("dst");
            srcNum=(String)redisUtil.hget("personMapOfIdNum",srcId);
            dstNum=(String)redisUtil.hget("personMapOfIdNum",dstId);
        } else {
            log.info("未知类型消息");
            return;
        }
        System.out.println("===================== 发送sip - src："+ srcNum +",dst："+dstNum);
        sipServer.sendSip(srcNum,dstNum,message);
    }


    public void sleeptest(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
