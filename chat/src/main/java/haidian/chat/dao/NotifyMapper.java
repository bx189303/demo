package haidian.chat.dao;

import haidian.chat.pojo.Notify;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notify record);

    int insertSelective(Notify record);

    Notify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notify record);

    int updateByPrimaryKey(Notify record);
}