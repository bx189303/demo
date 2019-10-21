package com.example.servicefeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="service-test1",fallback = FeignServiceError.class)
public interface FeignService {
    @RequestMapping("/test")
    String feignService(@RequestParam(value = "name") String name);
}
