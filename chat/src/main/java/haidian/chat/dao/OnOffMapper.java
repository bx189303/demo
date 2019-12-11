package haidian.chat.dao;

import haidian.chat.pojo.OnOff;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OnOffMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OnOff record);

    int insertSelective(OnOff record);

    OnOff selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OnOff record);

    int updateByPrimaryKey(OnOff record);
}