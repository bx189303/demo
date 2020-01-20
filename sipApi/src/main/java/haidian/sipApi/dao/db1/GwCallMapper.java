package haidian.sipApi.dao.db1;

import haidian.sipApi.pojo.po.GwCall;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GwCallMapper {

    List<GwCall> getCallRecord(String userId, String start, String end);

    int deleteByPrimaryKey(String callId);

    int insert(GwCall record);

    int insertSelective(GwCall record);

    GwCall selectByPrimaryKey(String callId);

    int updateByPrimaryKeySelective(GwCall record);

    int updateByPrimaryKey(GwCall record);
}