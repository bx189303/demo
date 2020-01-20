package haidian.sipApi.service;

import haidian.sipApi.dao.db1.GroupRecordMapper;
import haidian.sipApi.pojo.po.GroupRecord;
import haidian.sipApi.util.ListUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GroupRecordService {

    @Resource
    GroupRecordMapper groupRecordMapper;

    public List<GroupRecord> getGroupAudio(String userId, String start, String end) {
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

}
