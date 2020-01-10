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

    //海淀的组织机构成员树
    public static List<Unit> hdUnitUserTree=null;


    @Override
    public void run(String... args) {

        hdUnitTree=unitService.getUnits("110108000000");
        log.info("============= 提前加载海淀分局的组织机构树 =============");
        hdUnitUserTree=unitService.getUnitUsers();
        log.info("============= 提前加载海淀分局的组织机构成员树 =============");
        log.info("============= duty项目启动完成 =============");

    }
}
