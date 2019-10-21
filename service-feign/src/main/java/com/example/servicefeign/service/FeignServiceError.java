package com.example.servicefeign.service;

import org.springframework.stereotype.Component;

@Component
public class FeignServiceError implements FeignService{

    @Override
    public String feignService(String name) {
        return "你好"+name+",这里是feign熔断器";
    }
}
