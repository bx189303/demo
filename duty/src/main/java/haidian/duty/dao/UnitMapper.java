package haidian.duty.dao;

import haidian.duty.pojo.Unit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UnitMapper {

    List<Unit> selectByParentId(String sId);

    int deleteByPrimaryKey(String sId);

    int insert(Unit record);

    int insertSelective(Unit record);

    Unit selectByPrimaryKey(String sId);

    int updateByPrimaryKeySelective(Unit record);

    int updateByPrimaryKeyWithBLOBs(Unit record);

    int updateByPrimaryKey(Unit record);
}