package haidian.sipApi.dao.db1;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SipUserMapper {

    //调用添加人的存储过程
    void addSipUser(Map<String,Object> map);

    //调用删除人的存储过程
    void delSipUser(String personnel_id);

    //根据名字查id
    List<String> getIdByName(String name);

    void protest();
}
