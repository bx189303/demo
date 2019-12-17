package haidian.duty.service;

import haidian.duty.dao.DutyMapper;
import haidian.duty.pojo.Duty;
import haidian.duty.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DutyService {

    @Resource
    DutyMapper dutyMapper;

    public List<Duty> getDtuyByUnitId(String unitId) {
        String time=DateUtil.getDateToString(new Date());
        List<Duty> dutys = dutyMapper.getDutyByUnitId(unitId, time);
        return dutys;
    }

    public Duty getDtuyByUserId(String userId) {
        String time=DateUtil.getDateToString(new Date());
        Duty duty = dutyMapper.getDutyByUserId(userId,time);
        return duty;
    }
}
