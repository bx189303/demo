package haidian.chatSip.service;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.Variant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SipServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${serverHost}")
    String serverHost;

    @Value("${sipServerPort}")
    String sipServerPort;

    @Value("${sipServerHost}")
    String sipServerHost;

    @Value("${sipRegUser}")
    String sipRegUser;

    @Value("${sipRegPwd}")
    String sipRegPwd;

    static ActiveXComponent com = null;
    static Dispatch disp = null;

    public void sendSip(String src, String dst, String msg) {
        if (StringUtils.isEmpty(sipServerHost)) {
            return;
        }
        Variant[] var = new Variant[4];
//        var[0] = new Variant(src);
        var[0] = new Variant(sipRegUser);
        var[1] = new Variant(dst);
        var[2] = new Variant("");
        var[3] = new Variant(msg);
        com.invoke("SendMsg", var);
        System.out.println(com.getProperty("last_error_msg").getString());
    }

    public void initSipService() {
        try {
            if (StringUtils.isEmpty(sipServerHost)) {
                return;
            }
            String serverUrl = serverHost + ":" + sipServerPort;
            System.out.println("------------------------------------- server_addr:"+serverUrl+";local_sip_ip:"+sipServerHost+";username:"+sipRegUser+";pwd:"+sipRegPwd);
            com = new ActiveXComponent(
                    "CLSID:CDC4AB9A-CEB5-42F4-9E91-9A7451A4A4D2");
            disp = com.getObject();
            com.setProperty("local_sip_ip", sipServerHost);
            com.setProperty("server_addr", serverUrl);
            init();//初始化
            Thread.sleep(3000);
            Variant[] var = new Variant[1];
            var[0] = new Variant(1);
            com.invoke("EnableMsgEvent", var);
            //回调
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("___"+com.getProperty("last_error_msg").getString());
                    DispatchEvents de= new DispatchEvents (disp,new MyEvent(),"NWSipMsgCtrl.MsgAgent");
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void init() {
        com.invoke("Init");
        if (com.getProperty("last_error_code").getInt() == 0) {
            System.out.println("sip初始化成功");
            reg();  //初始化成功后调用注册
        } else {
            System.out.println("sip初始化失败");
        }
    }

    private void reg() {
        Variant[] var = new Variant[2];
        var[0] = new Variant(sipRegUser);
        var[1] = new Variant(sipRegPwd);
        com.invoke("Register", var);
        if (com.getProperty("last_error_code").getInt() == 0) {
            System.out.println("sip注册成功");
        } else {
            System.out.println("sip注册失败");
        }
    }
}
