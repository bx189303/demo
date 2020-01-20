package haidian.audio.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.audio.dao.db1.GroupRecordMapper;
import haidian.audio.dao.db1.GwCallMapper;
import haidian.audio.pojo.vo.AudioRecord;
import haidian.audio.pojo.po.GroupRecord;
import haidian.audio.pojo.po.GwCall;
import haidian.audio.service.GroupRecordService;
import haidian.audio.service.GwCallService;
import haidian.audio.util.DateUtil;
import haidian.audio.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class RecordController {

    @Resource
    GroupRecordMapper groupRecordMapper;

    @Resource
    GwCallMapper gwCallMapper;

    @Autowired
    GwCallService gwCallService;

    @Autowired
    GroupRecordService groupRecordService;

    @Value("${audioHost}")
    String audioHost;

    @RequestMapping("/getAudioByJqId/{jqId}")
    public Result getAudioByJqId(@PathVariable String jqId){
        Result result = null;
        try {
            List<GroupRecord> audios = groupRecordService.getGroupAudioByName(jqId);
            result = Result.ok(audios);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getAudioByCall/{tel}")
    public Result getAudioByTel(@PathVariable String tel){
        Result result = null;
        try {
            List<AudioRecord> audios=new ArrayList<>();
            List<GroupRecord> groupAudios = groupRecordService.getGroupAudio(tel, null, null);
            for (GroupRecord groupAudio : groupAudios) {
                AudioRecord audioRecord=new AudioRecord();
                audioRecord.setRecordFile(audioHost+groupAudio.getRecordFile());
                audioRecord.setRecordStartTime(groupAudio.getRecordStartTime());
                audioRecord.setRecordEndTime(groupAudio.getRecordEndTime());
                List<String> calls=groupRecordMapper.getGroupPerson(groupAudio.getQueueId());
                audioRecord.setCalls(calls);
                audios.add(audioRecord);
            }
            List<GwCall> singleAudios= gwCallService.getSingleAudio(tel, null, null);
            for (GwCall singleAudio : singleAudios) {
                AudioRecord audioRecord=new AudioRecord();
                audioRecord.setRecordFile(audioHost+singleAudio.getFileName());
                audioRecord.setRecordStartTime(singleAudio.getTalkStartTime());
                audioRecord.setRecordEndTime(singleAudio.getTalkEndTime());
                List<String> calls=new ArrayList<>();
                calls.add(singleAudio.getCalledNbr());
                calls.add(singleAudio.getCallingNbr());
                audioRecord.setCalls(calls);
                audios.add(audioRecord);
            }
            Collections.sort(audios, new Comparator<AudioRecord>() {
                @Override
                public int compare(AudioRecord o1, AudioRecord o2) {
                    Date d1 = DateUtil.getDateTime(o1.getRecordStartTime());
                    Date d2 = DateUtil.getDateTime(o2.getRecordStartTime());
                    if (d1.getTime() > d2.getTime()) {
                        return -1;
                    } else if (d1.getTime() < d2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            result = Result.ok(audios);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getGroupAudio")
    public Result getGroupAudio(@RequestBody JSONObject json){
        Result result = null;
        try {
            String userId=json.getString("userId");
            String start=json.getString("startDate");
            String end=json.getString("endDate");
            List<GroupRecord> groupRecords=groupRecordService.getGroupAudio(userId,start,end);
            result = Result.ok(groupRecords);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getSingleAudio")
    public Result getSingleAudio(@RequestBody JSONObject json){
        Result result = null;
        try {
            String userId=json.getString("userId");
            String start=json.getString("startDate");
            String end=json.getString("endDate");
            List<GwCall> audioList= gwCallService.getSingleAudio(userId,start,end);
            result = Result.ok(audioList);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/sqltest/{id}")
    public Result test(@PathVariable String id){
        Result result = null;
        try {
            GwCall gwCall = gwCallMapper.selectByPrimaryKey(id);
            result = Result.ok(gwCall);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

}
