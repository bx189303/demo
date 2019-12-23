package haidian.duty.config;

import haidian.duty.dao.PersonMapper;
import haidian.duty.pojo.Unit;
import haidian.duty.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目启动后开始执行
 */
@Component
public class ApplicationPre implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UnitService unitService;

    //海淀的组织机构树
    public static List<Unit> hdUnitTree=null;


    @Override
    public void run(String... args) throws Exception {
//        redisUtil.keys("null");//提前加载一次redis
//        //加载所有人员信息到redis
//        List<Person> persons = personMapper.getAll();
//        redisUtil.set("persons",persons);
//        for (Person person : persons) {
//            redisUtil.set(person.getsId(),person);
//        }
//        log.info("加载人员信息完毕");
//        //移除所有在线状态
//        Set<String> onKeys = redisUtil.keys("*on");
//        if(onKeys.size()!=0){
//            Iterator<String> it = onKeys.iterator();
//            while (it.hasNext()) {
//                String onId = it.next();
//                redisUtil.del(onId);
//            }
//        }
//        log.info("移除所有在线状态");

        hdUnitTree=unitService.getUnits("110108000000");
        log.info("============= 提前加载海淀分局的组织机构树 =============");
        log.info("============= duty项目启动完成 =============");

    }
}
