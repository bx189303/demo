package haidian.sipApi.service;

import haidian.sipApi.dao.db1.GwCallMapper;
import haidian.sipApi.pojo.po.GwCall;
import haidian.sipApi.util.ListUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GwCallService {

    @Resource
    GwCallMapper gwCallMapper;

    public List<GwCall> getSingleAudio(String userId, String start, String end) {
//        String nextDay= DateUtil.nextDate(date);
        List<GwCall> callRecords = gwCallMapper.getCallRecord(userId, start, end);
        for (GwCall callRecord : callRecords) {
            String oldFileName=callRecord.getFileName();
            callRecord.setFileName(oldFileName.replace("\\","/"));
        }
        callRecords= ListUtil.noDuplicate(callRecords);
        return callRecords;
    }


}
