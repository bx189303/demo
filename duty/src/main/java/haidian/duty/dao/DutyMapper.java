package haidian.duty.dao;

import haidian.duty.pojo.Duty;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DutyMapper {
    //根据用户id查职位
    Duty getDutyByUserId(String userId, String time);

    //根据单位id查所有职位
    List<Duty> getDutyByUnitId(String unitId,String time);

    int deleteByPrimaryKey(Integer id);

    int insert(Duty record);

    int insertSelective(Duty record);

    Duty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Duty record);

    int updateByPrimaryKey(Duty record);


}