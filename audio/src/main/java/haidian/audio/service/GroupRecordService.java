package haidian.audio.service;

import haidian.audio.dao.db1.GroupRecordMapper;
import haidian.audio.pojo.po.GroupRecord;
import haidian.audio.util.ListUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GroupRecordService {

    @Resource
    GroupRecordMapper groupRecordMapper;

    public List<GroupRecord> getGroupAudio(String userId, String start,String end) {
//        String nextDay= DateUtil.nextDate(date);
//        List<GroupRecord> groupRecords = groupRecordMapper.getGroupAudio("%"+userId+"%", start, end);
        List<GroupRecord> groupRecords = groupRecordMapper.getGroupAudio(userId, start, end);
        for (GroupRecord groupRecord : groupRecords) {
            String oldFileName=groupRecord.getRecordFile();
            groupRecord.setRecordFile(oldFileName.replace("\\","/"));
        }
        groupRecords= ListUtil.noDuplicate(groupRecords);
        return groupRecords;
    }

    public List<GroupRecord> getGroupAudioByName(String jqId) {
        List<GroupRecord> groupRecords=groupRecordMapper.getGroupAudioByName(jqId);
        for (GroupRecord groupRecord : groupRecords) {
            String oldFileName=groupRecord.getRecordFile();
            groupRecord.setRecordFile(oldFileName.replace("\\","/"));
        }
        groupRecords= ListUtil.noDuplicate(groupRecords);
        return groupRecords;
    }
}
