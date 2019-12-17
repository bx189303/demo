package haidian.duty.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.duty.dao.PersonMapper;
import haidian.duty.pojo.Duty;
import haidian.duty.pojo.Unit;
import haidian.duty.service.DutyService;
import haidian.duty.service.UnitService;
import haidian.duty.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ExtraController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Value("${nginxPort}")
    String nginxPort;

    @Value("${serverHost}")
    String serverHost;

    @Value("${serverPort}")
    String serverPort;

    @Resource
    PersonMapper personMapper;

    @Autowired
    UnitService unitService;

    @Autowired
    DutyService dutyService;

    @RequestMapping("/getDutyByUserId")
    public Result getDutyByUserId(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String userId=json.getString("userId");
//            String date=json.getString("date");
            Duty duty= dutyService.getDtuyByUserId(userId);
            result = Result.ok(duty);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getDutyByUnitId")
    public Result getDutyByUnitId(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String unitId=json.getString("unitId");
//            String date=json.getString("date");
            List<Duty> dutys= dutyService.getDtuyByUnitId(unitId);
            result = Result.ok(dutys);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getUnitByUserId/{userId}")
    public Result getUnitByUserId(@PathVariable String userId) {
        Result result = null;
        try {
            String unitId = personMapper.getUnitByUserId(userId);
            if(StringUtils.isEmpty(unitId)){
                return Result.build(500,"用户id有误");
            }
            List<Unit> units = unitService.getUnits(unitId);
            result = Result.ok(units);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

}
