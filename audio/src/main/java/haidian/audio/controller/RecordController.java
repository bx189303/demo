package haidian.audio.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.audio.dao.GwCallMapper;
import haidian.audio.pojo.GroupRecord;
import haidian.audio.pojo.GwCall;
import haidian.audio.service.GroupRecordService;
import haidian.audio.service.GwCallService;
import haidian.audio.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RecordController {

    @Resource
    GwCallMapper gwCallMapper;

    @Autowired
    GwCallService gwCallService;

    @Autowired
    GroupRecordService groupRecordService;



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
