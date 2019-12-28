package haidian.chat.dao;

import haidian.chat.pojo.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {

    //根据单位名或姓名查人id
    List<String> searchByNameOrUnitnameOrPolicenum(String name);

    //查所有人
    List<Person> getAll();

    int deleteByPrimaryKey(String sId);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(String sId);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKeyWithBLOBs(Person record);

    int updateByPrimaryKey(Person record);
}