package haidian.audio.dao;

import haidian.audio.pojo.GwCall;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GwCallMapper {

    List<GwCall> getCallRecord(String userId,String start,String end);

    int deleteByPrimaryKey(String callId);

    int insert(GwCall record);

    int insertSelective(GwCall record);

    GwCall selectByPrimaryKey(String callId);

    int updateByPrimaryKeySelective(GwCall record);

    int updateByPrimaryKey(GwCall record);
}