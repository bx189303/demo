package haidian.chat.config;

import haidian.chat.controller.PersonController;
import haidian.chat.redis.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动后开始执行
 */
@Component
public class ApplicationPre implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    PersonController personController;

    @Override
    public void run(String... args) throws Exception {
        redisUtil.keys("null");//提前加载一次redis
        //加载所有人员信息到redis
        personController.loadUserInfo();
        //移除所有在线状态
        redisUtil.del("websocketon");
        log.info("移除所有在线状态");
        log.info("============= chat项目启动完成 =============");

    }
}
