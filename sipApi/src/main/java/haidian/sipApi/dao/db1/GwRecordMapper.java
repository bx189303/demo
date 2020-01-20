package haidian.sipApi.dao.db1;

import haidian.sipApi.pojo.po.GwRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GwRecordMapper {

    void addtest(String id,String type);

    int deleteByPrimaryKey(String callId);

    int insert(GwRecord record);

    int insertSelective(GwRecord record);

    GwRecord selectByPrimaryKey(String callId);

    int updateByPrimaryKeySelective(GwRecord record);

    int updateByPrimaryKey(GwRecord record);
}