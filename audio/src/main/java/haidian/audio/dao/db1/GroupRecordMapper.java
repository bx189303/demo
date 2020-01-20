package haidian.audio.dao.db1;

import haidian.audio.pojo.po.GroupRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupRecordMapper {

    //根据群聊名称查录音(名称=警情id)
    List<GroupRecord> getGroupAudioByName(String jqId);

    //根据 会议id查成员
    List<String> getGroupPerson(String groupId);

    //查会议录音
    List<GroupRecord> getGroupAudio(String userId,String start,String end);

    int deleteByPrimaryKey(String logId);

    int insert(GroupRecord record);

    int insertSelective(GroupRecord record);

    GroupRecord selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(GroupRecord record);

    int updateByPrimaryKey(GroupRecord record);

}