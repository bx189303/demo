package haidian.duty.dao;

import haidian.duty.pojo.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {

    //根据 警号 查有哪些指挥组id
    List<String> getGroupIdByUserId(String userId);

    //根据组查有哪些用户
    List<String> getUserByGroupId(String groupId);

    //根据用户id查有哪些组
    List<Group> getByUserId(String userId);

    int deleteByPrimaryKey(String id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);

}