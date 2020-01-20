package haidian.chatSip.config;

import haidian.chatSip.redis.util.RedisUtil;
import haidian.chatSip.service.SipServer;
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
    SipServer sipServer;

    @Override
    public void run(String... args) throws Exception {
        sipServer.initSipService();
        log.info("============= 注册sip完成 =============");
        log.info("============= chatSip项目启动完成 =============");

    }
}
