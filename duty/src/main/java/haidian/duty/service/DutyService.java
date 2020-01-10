package haidian.duty.service;

import com.alibaba.fastjson.JSONObject;
import haidian.duty.dao.DutyMapper;
import haidian.duty.pojo.Duty;
import haidian.duty.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DutyService {

    @Resource
    DutyMapper dutyMapper;

    public List<JSONObject> getDtuyInfoByUnitIdGroupByDutyType(String unitId) {
        List<JSONObject> res=new ArrayList<>();
        String time=DateUtil.getDateToStrings(new Date());
        List<Duty> dutys = dutyMapper.getDutyInfoByUnitId(unitId, time);
        HashMap<String,List<Duty>> dutyMap=new HashMap<>();
        for (Duty duty : dutys) {
            String dutyType=duty.getDutytype();
            if(dutyMap.containsKey(dutyType)){
                dutyMap.get(dutyType).add(duty);
            }else{
                List<Duty> dutysOneType=new ArrayList<>();
                dutysOneType.add(duty);
                dutyMap.put(dutyType,dutysOneType);
            }
        }
        for (String key : dutyMap.keySet()) {
            JSONObject dutyJson=new JSONObject();
            dutyJson.put("dutytype",key);
            dutyJson.put("users",dutyMap.get(key));
            res.add(dutyJson);
        }
        return res;
    }

    public List<Duty> getDtuyByUnitId(String unitId) {
        String time=DateUtil.getDateToStrings(new Date());
        List<Duty> dutys = dutyMapper.getDutyByUnitId(unitId, time);
        return dutys;
    }

    public Duty getDtuyByUserId(String userId) {
        String time=DateUtil.getDateToStrings(new Date());
        Duty duty = dutyMapper.getDutyByUserId(userId,time);
        return duty;
    }

    public Duty getDtuyByUserNameOrNum(String nameOrNum) {
        String time=DateUtil.getDateToStrings(new Date());
        Duty duty = dutyMapper.getDutyByUserNameOrNum(nameOrNum,time);
        return duty;
    }
}
