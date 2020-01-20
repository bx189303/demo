package haidian.sipApi.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class AudioRecord {

    private String recordFile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordEndTime;

    private List<String> calls;

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }

    public Date getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(Date recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public Date getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(Date recordEndTime) {
        this.recordEndTime = recordEndTime;
    }

    public List<String> getCalls() {
        return calls;
    }

    public void setCalls(List<String> calls) {
        this.calls = calls;
    }
}
