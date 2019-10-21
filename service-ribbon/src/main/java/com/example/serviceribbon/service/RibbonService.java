package com.example.serviceribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "ribbonError")
    public String serviceTest(String name){
        return restTemplate.getForObject("http://service-test1/test?name="+name,String.class);
    }

    public String ribbonError(String name){
        return "你好"+name+",这里是ribbon熔断器";
    }

}
