package com.service_haidian.controller;

import com.alibaba.fastjson.JSON;
import com.service_haidian.bean.JqJjjl;
import com.service_haidian.service.JjjlService;
import com.service_haidian.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    JjjlService jjjlService;

    @RequestMapping("/jjjl/{dh}")
    public Result jjjlByDh(@PathVariable String dh){
        long t1= System.currentTimeMillis();
        List<JqJjjl> jjjss=jjjlService.getJjjlByDh(dh);
        long t2= System.currentTimeMillis();
        double t=new Double((t2-t1)/1000);
        System.out.println("耗时为 ："+t+"秒");
        if(jjjss.isEmpty()){
            return Result.ok("电话："+dh+"无报警记录");
        }else{
            return Result.ok(jjjss);
        }
    };
}
