package haidian.chatSip.service;

import com.jacob.com.Variant;

import java.util.concurrent.LinkedBlockingQueue;

public class MyEvent {
	
	LinkedBlockingQueue<String> list=null;

	public MyEvent() {

	}

	
	public MyEvent(LinkedBlockingQueue<String> list) {
		this.list=list;
	}
	//对方主动发消息,回调接收事件
	public void OnMsgRecv(Variant[] args) {
		try {
//			list.put(args[3].toString());
			System.out.println("========================================="+args[3].toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(args[0]+"   "+args[1]+"  "+args[2]+"   "+args[3]);
	}
	//发消息后，远端的回复
	public void OnMsgSendResponse(Variant[] args) {
//		if(!"".equals(args[5])) {
//			System.out.println("结果消息内容为：："+args[5]);
//		}else {
//			System.out.println("结果为空");
//		}
//		System.out.println("___________"+"状态码="+args[0]+"    消息主叫="+args[1]+"  "
//				+ "消息被叫="+args[2]+"    发送结果码="+args[3]+"    发送结果消息="+args[4]+"    发送结果包含的内容="+args[5]);
	}
}
