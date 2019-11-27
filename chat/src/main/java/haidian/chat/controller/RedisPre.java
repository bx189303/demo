package haidian.chat.controller;

import haidian.chat.dao.PersonMapper;
import haidian.chat.pojo.Person;
import haidian.chat.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目启动后开始执行
 */
@Component
public class RedisPre implements CommandLineRunner {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    PersonMapper personMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("项目启动完毕");
        redisUtil.keys("null");//提前加载一次redis

        List<Person> persons = personMapper.getAll();
        redisUtil.set("persons",persons);
        for (Person person : persons) {
            redisUtil.set(person.getsId(),person);
        }
        System.out.println("加载人员信息完毕");

    }
}
