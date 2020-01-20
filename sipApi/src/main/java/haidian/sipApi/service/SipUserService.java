package haidian.sipApi.service;

import haidian.sipApi.dao.db1.SipUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SipUserService {

    @Resource
    SipUserMapper sipUserMapper;

    public void addSipUser(String name){
        Map<String,Object> map=new HashMap<>();
        map.put("personnel_id","");
//        map.put("co_id",name);
        map.put("co_id","");
        map.put("co_per_id",name);
        map.put("personnel_pwd",name);
        map.put("is_actived",1);
        map.put("personnel_type",1);
        map.put("personnel_name",name);
        map.put("personnel_email","");
        map.put("Personnel_business","");
        map.put("personnel_sex",1);
        map.put("personnel_birth","2000-11-11");
        map.put("Personnel_addr","");
        map.put("personnel_info","");
        map.put("msn_number","");
        map.put("qq_number","");
        map.put("personnel_popedom","");
        map.put("call_popedom",1);
        sipUserMapper.addSipUser(map);
    }

    public void delSipUser(String name){
        List<String> ids=sipUserMapper.getIdByName(name);
        if(ids.size()>0){
            String id=ids.get(0);
            sipUserMapper.delSipUser(id);
        }
    }

}
