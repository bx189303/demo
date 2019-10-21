package com.example.rabbitmq;

import com.example.rabbitmq.util.rabbitmq.MsgProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    MsgProducer s;

    @Test
    public void contextLoads() throws InterruptedException {
        s.sendMsg("测试消息123");
        Thread.sleep(5000);
    }

}
