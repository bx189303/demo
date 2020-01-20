package haidian.chat.redis.listen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.redis.util.RedisUtil;
import haidian.chat.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static haidian.chat.util.httpUtil.sendPostRequest;

@Service
public class SendInWebsocketoff {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${serverHost}")
    String serverHost;

    @Value("${serverPort}")
    String serverPort;

    @Value("${notifyUrl}")
    String notifyUrl;

    @Value("${notifyUrlSysId}")
    String notifyUrlSysId;

    @Value("${notifyUrlSysName}")
    String notifyUrlSysName;

    @Autowired
    RedisUtil r;

    //监听DISPATCH,如果websocket不在线则发送首页接口
    public void sendIndexMsg(String message) {
        JSONObject msg = JSON.parseObject(message);
        String msgType = msg.getString("type");
        JSONObject data = msg.getJSONObject("data");
        String dstId = "";
        if (msgType.equals("msg")) {
            dstId = data.getJSONObject("dst").getString("sId");
            String srcId = data.getJSONObject("src").getString("sId");
            String srcName=data.getJSONObject("src").getString("sName");
            if (!r.hHasKey("websocketon",dstId)) {
//                System.out.println("离线发送给首页 ： " + message);
                String[] dstArray = {dstId};
                sendMsgOff(srcId, srcName, dstArray);
            }
        }
    }

    //对面离线时发送给第三方接口
    public void sendMsgOff(String src, String srcName, Object[] dst) {
//        System.out.println("发送接口-src:"+src+",srcName:"+srcName+",dst:"+JSON.toJSONString(dst));
        Map<String, Object> map = new HashMap<>();
        map.put("SysId", notifyUrlSysId);//指定
        map.put("SysName", notifyUrlSysName);//指定
        map.put("PushId", src);
        map.put("PushName", srcName);
        map.put("Time", DateUtil.getDateTimeToString(new Date()));
        map.put("Accepts", dst);
        map.put("Target", serverHost + ":" + serverPort + "/chat/index.html?userid=");
        map.put("Title", "");
        if (!StringUtils.isBlank(notifyUrlSysId) && !StringUtils.isBlank(notifyUrlSysName) && !StringUtils.isBlank(notifyUrl)) {
//            log.info("发送门户接口参数：" + JSON.toJSONString(map));
            sendPostRequest(notifyUrl, map);
        }
    }
}
