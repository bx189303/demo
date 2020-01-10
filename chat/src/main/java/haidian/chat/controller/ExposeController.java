package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Api
@RestController
public class ExposeController {

    @Autowired
    MainController mainController;

    @Autowired
    PersonController personController;

    @Autowired
    GroupController groupController;

    @Autowired
    RecordController recordController;

    @ApiOperation("即时通信接口")
    @PostMapping("/chatApi")
    public Result chatApi(@RequestBody JSONObject requestJson){
        Result result = null;
        try {
            int apiType = requestJson.getInteger("type");
            if (apiType==1) {
                JSONObject dataJson=requestJson.getJSONObject("data");
                result=personController.getUserByPoliceNum(dataJson);
            }else if(apiType==2){
                JSONObject dataJson=requestJson.getJSONObject("data");
                result=mainController.service(dataJson);
            }else if(apiType==3){
                String userId=requestJson.getString("data");
                result=recordController.getIndexList(userId);
            }else if(apiType==4){
                JSONObject dataJson=requestJson.getJSONObject("data");
                result=recordController.getRecordByPage(dataJson);
            }else if(apiType==5){
                result=personController.getPersonAll();
            }else if(apiType==6){
                String userId=requestJson.getString("data");
                result=personController.getUserByUserId(userId);
            }else if(apiType==7){
                String input=requestJson.getString("data");
                result=personController.searchPerson(input);
            }else if(apiType==8){
                String groupId=requestJson.getString("data");
                result=personController.getPersonByGroupId(groupId);
            }else if(apiType==9){
                String userId=requestJson.getString("data");
                result=groupController.getGroupByUserId(userId);
            }else if(apiType==10){
                String userIds=requestJson.getString("data");
                result=groupController.createGroup(userIds);
            }else if(apiType==11){
                JSONObject dataJson=requestJson.getJSONObject("data");
                result=groupController.addGroup(dataJson);
            }else if(apiType==12){
                JSONObject dataJson=requestJson.getJSONObject("data");
                result=groupController.updateGroup(dataJson);
            }else if(apiType==13){
                JSONObject dataJson=requestJson.getJSONObject("data");
                result=groupController.outGroup(dataJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.build(500, e.getMessage());
        }
        return result;
    }


}
