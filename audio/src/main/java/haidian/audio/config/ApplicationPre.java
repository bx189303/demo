package haidian.audio.config;

import haidian.audio.dao.db2.PersonMapper;
import haidian.audio.pojo.po.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目启动后开始执行
 */
@Component
public class ApplicationPre implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static Map<String,Person> personMap=null;

    @Resource
    PersonMapper personMapper;

    @Override
    public void run(String... args) throws Exception {
        List<Person> ps = personMapper.getAll();
        personMap=new HashMap<>();
        for (Person p : ps) {
            String policeNum=p.getsPolicenum();
            if(policeNum!=null){
                personMap.put(p.getsPolicenum(),p);
            }
        }
        log.info("============= 人员信息加载完成 =========");

        log.info("============= audio 项目启动完成 =========");

    }
}
