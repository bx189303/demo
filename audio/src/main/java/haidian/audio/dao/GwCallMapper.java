package haidian.audio.dao;

import haidian.audio.pojo.GwCall;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GwCallMapper {
    int deleteByPrimaryKey(String callId);

    int insert(GwCall record);

    int insertSelective(GwCall record);

    GwCall selectByPrimaryKey(String callId);

    int updateByPrimaryKeySelective(GwCall record);

    int updateByPrimaryKey(GwCall record);
}