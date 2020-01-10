package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.dao.*;
import haidian.chat.pojo.*;
import haidian.chat.redis.RedisUtil;
import haidian.chat.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class ExtraController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${nginxPort}")
    String nginxPort;

    @Value("${serverHost}")
    String serverHost;

    @Value("${serverPort}")
    String serverPort;

    @Resource
    PersonMapper personMapper;

    @Autowired
    RedisUtil r;


    //html加载后先获取系统参数
    @RequestMapping("/getHtmlparam")
    public Result getHtmlParam(){
        Result result = null;
        try {
            JSONObject json=new JSONObject();
            json.put("nginxUrl",serverHost+":"+nginxPort);
            json.put("serverUrl", serverHost+":"+serverPort);
            result = Result.ok(json);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


}
