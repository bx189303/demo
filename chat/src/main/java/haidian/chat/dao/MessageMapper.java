package haidian.chat.dao;

import haidian.chat.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    //根据内容或文件名查记录-个人
    List<Message> getSingleMessageByContent(String src, String dst, String type, String content, int start, int end);

    //根据内容或文件名查记录-群组
    List<Message> getGroupMessageByContent(String src, String dst, String type, String content, int start, int end);

    //根据时间向前获取记录-个人
    List<Message> getSingleMessageByDateLeft(String src, String dst, String type, String date, int start, int end);

    //根据时间向后获取记录-群组
    List<Message> getGroupMessageByDateLeft(String src, String dst, String type, String date, int start, int end);

    //根据时间获取记录-群组
    List<Message> getGroupMessageByDate(String src, String dst, String type, String date, int start, int end);

    //根据时间获取记录-个人
    List<Message> getSingleMessageByDate(String src, String dst, String type, String date, int start, int end);

    //根据文件类型获取记录-群组
    List<Message> getGroupMessageByFile(String src, String dst, String dstType, String fileType, int start, int end);

    //根据文件类型获取记录-个人
    List<Message> getSingleMessageByFile(String src,String dst,String dstType,String fileType, int start, int end);

    //根据群成员获取记录
    List<Message> getMessageByGroupUser(String groupId,String groupUserId,int start,int end);

    int deleteByPrimaryKey(String id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    String getReadIdById(String id);



}