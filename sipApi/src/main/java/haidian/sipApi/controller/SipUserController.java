package haidian.sipApi.controller;

import haidian.sipApi.dao.db2.PersonMapper;
import haidian.sipApi.pojo.po.Person;
import haidian.sipApi.service.SipUserService;
import haidian.sipApi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class SipUserController {

    @Resource
    PersonMapper personMapper;

    @Autowired
    SipUserService sipUserService;

    @RequestMapping("/addSipUser/{usernames}")
    public Result addSipUser(@PathVariable String usernames){
        Result result = null;
        try {
            String[] usernameArray = usernames.split(",");
            for (String name : usernameArray) {
                if("admin".equalsIgnoreCase(name)){
                    break;
                }
                sipUserService.addSipUser(name);
            }
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/delSipUser/{usernames}")
    public Result delSipUser(@PathVariable String usernames){
        Result result = null;
        try {
            String[] usernameArray = usernames.split(",");
            for (String name : usernameArray) {
                if("admin".equalsIgnoreCase(name)){
                    break;
                }
                sipUserService.delSipUser(name);
            }
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }

    @RequestMapping("/reloadSipUser")
    public Result reloadSipUser(){
        Result result = null;
        try {
            List<Person> all = personMapper.getAll();
            for (Person p : all) {
                sipUserService.addSipUser(p.getsPolicenum());
            }
            result = Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


}
