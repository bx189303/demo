package haidian.duty.dao;

import haidian.duty.pojo.GroupUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupUserMapper {

    //退出群，根据id改有效值
    int outByGroupIdAndUserId(String groupId, String userId);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupUser record);

    int insertSelective(GroupUser record);

    GroupUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupUser record);

    int updateByPrimaryKey(GroupUser record);
}