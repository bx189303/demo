package haidian.duty.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.duty.config.ApplicationPre;
import haidian.duty.pojo.Group;
import haidian.duty.pojo.Person;
import haidian.duty.service.DutyService;
import haidian.duty.service.GroupService;
import haidian.duty.service.PersonService;
import haidian.duty.util.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "综合指挥api接口")
@RestController
public class ZhiHuiController {

    @Autowired
    PersonService personService;

    @Autowired
    DutyService dutyService;

    @Autowired
    GroupService groupService;

    @ApiOperation("查询组织机构,返回简单数据类型,返回集合中第一个为用户单位")
    @PostMapping("/getUnit")
    public Result getUnit() {
        Result result = null;
        try {
            result = Result.ok(ApplicationPre.hdUnitUserTree);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @ApiOperation("根据单位名或姓名警号查人,参数：{unitId:单位id,input:名字或者警号}")
    @PostMapping("/getPersonByUnitAndNameOrNum")
    public Result getPersonByUnitAndNameOrNum(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String unit=json.getString("unitId");
            String NameOrNum=json.getString("input");
            List<Person> persons= personService.getPersonByUnitAndNameOrNum(unit,NameOrNum);
            result = Result.ok(persons);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @ApiOperation("根据单位id查值班信息,参数：{unitId:单位id}")
    @PostMapping("/getDutyInfoByUnitId")
    public Result getDutyInfoByUnitId(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String unitId=json.getString("unitId");
            List<JSONObject> dutys= dutyService.getDtuyInfoByUnitIdGroupByDutyType(unitId);
            result = Result.ok(dutys);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @ApiOperation("根据警号查指挥组成员,参数：{userNum:警号}")
    @PostMapping("/getZhzByNum")
    public Result getZhzByNum(@RequestBody JSONObject json) {
        Result result = null;
        try {
            String policeNum=json.getString("userNum");
            List<Group> groupUsers=groupService.getZhzByNum(policeNum);
            result = Result.ok(groupUsers);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

}
