package haidian.chat;

import haidian.duty.DutyApplication;
import haidian.duty.dao.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {DutyApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Autowired(required = false)
    PersonMapper personMapper;


}
