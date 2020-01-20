package haidian.chat.redis.listen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.MessageMapper;
import haidian.chat.dao.NotifyMapper;
import haidian.chat.dao.OnOffMapper;
import haidian.chat.pojo.Message;
import haidian.chat.pojo.Notify;
import haidian.chat.pojo.OnOff;
import haidian.chat.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SaveMysql {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    OnOffMapper onOffMapper;

    @Resource
    NotifyMapper notifyMapper;

    @Resource
    MessageMapper messageMapper;

    /**
     * 监听redis RECEIVE
     */
    public void saveMsg(String msg){
        String type= JSON.parseObject(msg).getString("type");
        if("msg".equalsIgnoreCase(type)){
            saveMsgInMysql(msg);
        }else if("notify".equalsIgnoreCase(type)){
            saveNotify(msg);
        }else if("onoff".equalsIgnoreCase(type)){
            saveOnoff(msg);
        }
    }

    private void saveMsgInMysql(String msg) {
        try {
            Message m=new Message();
            //获取参数
            JSONObject message=JSON.parseObject(msg);
            JSONObject data=message.getJSONObject("data");
            String sendTime=message.getString("sendTime");
            String receiveTime=message.getString("receiveTime");
            int isValid=message.getInteger("isValid");
            String uuid=data.getString("uuid");
            String srcId=data.getJSONObject("src").getString("sId");
            String dstType=data.getString("type");
            String readId=data.getString("readId");
            String dstId="";
            if("single".equalsIgnoreCase(dstType)){
                dstId=data.getJSONObject("dst").getString("sId");
            }else if("group".equalsIgnoreCase(dstType)){
                dstId=data.getJSONObject("dst").getString("id");
            }
            JSONObject contentData=data.getJSONObject("content");
            String contentType=contentData.getString("type");
            String content="";
            String fileName="";
            if("text".equals(contentType)){
                content=contentData.getString("content");
            }else if("file".equals(contentType)){
                JSONObject fileData=contentData.getJSONObject("content");
                content=fileData.getString("content");
                fileName=fileData.getString("name");
                contentType=fileData.getString("type");
                m.setFilename(fileName);
            }
            int fileSize=0;//暂无
            int fileDuration=0;//暂无
            //保存
            m.setId(uuid);
            m.setSrc(srcId);
            m.setDst(dstId);
            m.setDsttype(dstType);
            m.setReadid(readId);
            m.setContent(content);
            m.setContenttype(contentType);
            m.setIsvalid(isValid);
            m.setSendtime(DateUtil.getDateTime(sendTime));
            m.setReceivetime(DateUtil.getDateTime(receiveTime));
            m.setCreatetime(new Date());
            m.setUpdatetime(new Date());
            messageMapper.insertSelective(m);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNotify(String msg) {
//        System.out.println("------------------------收到notify-------------------------");
        try {
            //获取参数
            JSONObject notify=JSON.parseObject(msg);
            JSONObject data=notify.getJSONObject("data");
            String sendTime=notify.getString("sendTime");
            String receiveTime=notify.getString("receiveTime");
            int isValid=notify.getInteger("isValid");
            String uuid=data.getString("uuid");
            String srcId=data.getString("src");
            //保存
            Notify n=new Notify();
            n.setMessageid(uuid);
            n.setIsvalid(isValid);
            n.setSrc(srcId);
            n.setSendtime(DateUtil.getDateTime(sendTime));
            n.setReceivetime(DateUtil.getDateTime(receiveTime));
            n.setCreatetime(new Date());
            n.setUpdatetime(new Date());
            notifyMapper.insertSelective(n);
            //更新message的readid
            String oldReadId = messageMapper.getReadIdById(uuid);
            String newReadId="";
            if(oldReadId.indexOf(srcId)==-1){
                if(StringUtils.isBlank(oldReadId)){
                    newReadId=srcId;
                }else{
                    newReadId=oldReadId+","+srcId;
                }
            }
            Message m=new Message();
            m.setId(uuid);
            m.setReadid(newReadId);
            messageMapper.updateByPrimaryKeySelective(m);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void saveOnoff(String msg) {
        try {
            //获取参数
            JSONObject onoff=JSON.parseObject(msg);
            JSONObject data=onoff.getJSONObject("data");
            String sendTime=onoff.getString("sendTime");
            String receiveTime=onoff.getString("receiveTime");
            int isValid=onoff.getInteger("isValid");
            String onoffType=data.getString("type");
            String userId=data.getString("userId");
            //保存
            OnOff onoffBean=new OnOff();
            onoffBean.setUserid(userId);
            onoffBean.setType(onoffType);
            onoffBean.setSendtime(DateUtil.getDateTime(sendTime));
            onoffBean.setReceivetime(DateUtil.getDateTime(receiveTime));
            onoffBean.setIsvalid(isValid);
            onoffBean.setCreatetime(new Date());
            onoffBean.setUpdatetime(new Date());
            onOffMapper.insertSelective(onoffBean);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
