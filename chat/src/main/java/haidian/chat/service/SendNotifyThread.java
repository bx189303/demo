package haidian.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.controller.MainController;
import haidian.chat.util.ApplicationContextProvider;
import haidian.chat.util.DateUtil;

import java.util.Date;
import java.util.List;

public class SendNotifyThread implements Runnable {

    private String notifySrc;

    private List<Object> msgList;

    private MainController mainController;

    public SendNotifyThread(String notifySrc, List<Object> msgList, MainController mainController) {
        this.notifySrc=notifySrc;
        this.msgList = msgList;
        this.mainController=mainController;
    }

    @Override
    public void run() {
        for (int i = msgList.size()-1; i>=0 ; i--) { //倒序遍历msg记录
            JSONObject msgJson= (JSONObject) msgList.get(i);
            JSONObject msgJsonData = msgJson.getJSONObject("data");
            String msgType=msgJsonData.getString("type");
            //获取发送消息的人，如果是发起通知的人,则跳出本次循环
            String srcId=msgJsonData.getJSONObject("src").getString("sId");;
            if(srcId.equalsIgnoreCase(notifySrc)){
                continue;
            }
            //查已读id,如果已读则认为之前全部已读，跳出循环
            String readIds = msgJsonData.getString("readId");
            if(readIds.indexOf(notifySrc)!=-1){
                break;
            }
            //发送notify
            JSONObject notifyJson=new JSONObject();
            notifyJson.put("type","notify");
            notifyJson.put("sendTime", DateUtil.getDateToStrings(new Date()));
//            notifyJson.put("receiveTime", DateUtil.getDateToStrings(new Date()));
//            notifyJson.put("idValid",1);
            JSONObject notifyJsonData=new JSONObject();
            notifyJsonData.put("type",msgType);
            notifyJsonData.put("uuid",msgJsonData.getString("uuid"));
            notifyJsonData.put("src",notifySrc);
            notifyJsonData.put("dst",msgJsonData.getJSONObject("src").getString("sId"));
            if(msgType.equalsIgnoreCase("group")){
                notifyJsonData.put("groupId",msgJsonData.getJSONObject("dst").getString("id"));
            }
            notifyJson.put("data",notifyJsonData);
            mainController.sendNotify(notifyJson);
//            String notifyParam= JSON.toJSONString(notifyJson);
//            System.out.println("notify参数： "+notifyParam);
//            listenAndSend.sendNotify(notifyParam);
//            ApplicationContextProvider.getBean(MainController.class).sendNotify(notifyJson);

        }
    }
}
