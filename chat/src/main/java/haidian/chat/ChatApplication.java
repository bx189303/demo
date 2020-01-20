package haidian.chat;

import haidian.chat.controller.WebSocketController;
import haidian.chat.redis.util.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ChatApplication.class, args);

        SpringApplication springApplication = new SpringApplication(ChatApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

        // 获取Spring IOC容器中的bean并注入
        RedisUtil redisUtil = applicationContext.getBean(RedisUtil.class);
//        RedisThread.setRedisUtil(redisUtil);
        WebSocketController.setRedisUtil(redisUtil);

        StringRedisTemplate template=applicationContext.getBean(StringRedisTemplate.class);
        WebSocketController.setStringRedisTemplate(template);
    }

}
