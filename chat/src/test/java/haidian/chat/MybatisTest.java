package haidian.chat;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisTest {

    @Autowired(required = false)
//    EmpCopyMapper empCopyMapper;

    @Test
    public void gentest(){
//        System.out.println(JSON.toJSONString(empCopyMapper.selectAll()));
    }

}
