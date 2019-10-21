package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Value("${baidu_ip}")
    String ip;

    @Value("${server.port}")
    String port;

    @RequestMapping("/test")
    public String test(@RequestParam(value = "name", defaultValue = "defaulteName") String name) {
        return "你好" + name + ",这里是" + ip + ":" + port;
    }

    @RequestMapping("/test2")
    public String test2(@RequestBody JSONObject param){
        System.out.println(JSON.toJSONString(param));
        return ""+param.getString("name");
    }

    @RequestMapping("/test3/{name}")
    public String test3(@PathVariable String name){
        return "测试方法3返回 ： "+name;
    }
}
