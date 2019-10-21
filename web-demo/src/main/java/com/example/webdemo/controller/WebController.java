package com.example.webdemo.controller;

import com.example.webdemo.bean.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @Value("${name}")
    String name;

    @Value("${age}")
    int age;

    @RequestMapping("/index")
    public String index(ModelMap map){
        map.addAttribute("word1",name);
        map.addAttribute("age",age);
        Person p=new Person("小明",11);
        map.addAttribute("person",p);
        return "index";
    }

    @RequestMapping("/lay")
    public String lay(){
        return "layui";
    }

    @RequestMapping("/sip")
    public String sip(){
        return "sip";
    }

    @RequestMapping("/video")
    public String video(){
        return "video";
    }

    @RequestMapping("/testclick")
    @ResponseBody
    public String testClick(String name){
        System.out.println(name);
        return "点击成功";
    }
}
