package haidian.chat.dao;

import haidian.chat.pojo.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {

    List<String> searchByNameOrUnitname(String name);

    List<Person> getAll();

    int deleteByPrimaryKey(String sId);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(String sId);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKeyWithBLOBs(Person record);

    int updateByPrimaryKey(Person record);
}