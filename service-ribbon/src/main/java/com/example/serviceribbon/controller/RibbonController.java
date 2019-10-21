package com.example.serviceribbon.controller;

import com.example.serviceribbon.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RibbonController {
    @Autowired
    RibbonService ribbonService;

    @RequestMapping("/ribbon")
        public String ribbonTest(String name){
        return ribbonService.serviceTest(name);
    }
}
