package haidian.audio.controller;

import haidian.audio.dao.GwCallMapper;
import haidian.audio.pojo.GwCall;
import haidian.audio.util.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RecordController {

    @Resource
    GwCallMapper gwCallMapper;

    @RequestMapping("/sqltest/{id}")
    public Result test(@PathVariable String id){
        Result result = null;
        try {
            GwCall gwCall = gwCallMapper.selectByPrimaryKey(id);
            result = Result.ok(gwCall);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

}
