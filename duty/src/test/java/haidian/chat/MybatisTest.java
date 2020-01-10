package haidian.chat;

import com.alibaba.fastjson.JSON;
import haidian.duty.DutyApplication;
import haidian.duty.dao.PersonMapper;
import haidian.duty.pojo.Unit;
import haidian.duty.service.UnitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {DutyApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Autowired(required = false)
    PersonMapper personMapper;


    @Autowired
    UnitService unitService;

    @Test
    public void dgtest(){
//        List<Unit> unitUsers = unitService.getUnitUsers("110108000000");
        List<Unit> unitUsers = unitService.getUnitUsers();
        System.out.println(JSON.toJSONString(unitUsers));
    }

}
