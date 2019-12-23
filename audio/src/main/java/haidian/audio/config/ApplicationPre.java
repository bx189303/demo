package haidian.audio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 项目启动后开始执行
 */
@Component
public class ApplicationPre implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public void run(String... args) throws Exception {

        log.info("============= audio 项目启动完成 =========");

    }
}
