package com.example.servicefeign.controller;

import com.example.servicefeign.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {
    @Autowired
    FeignService feignService;

    @RequestMapping("/feign")
    public String FeignController(String name){
        return feignService.feignService(name);
    }
}
