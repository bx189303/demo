package haidian.audio.dao;

import haidian.audio.pojo.GwRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GwRecordMapper {
    int deleteByPrimaryKey(String callId);

    int insert(GwRecord record);

    int insertSelective(GwRecord record);

    GwRecord selectByPrimaryKey(String callId);

    int updateByPrimaryKeySelective(GwRecord record);

    int updateByPrimaryKey(GwRecord record);
}