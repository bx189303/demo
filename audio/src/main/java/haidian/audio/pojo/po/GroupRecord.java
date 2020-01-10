package haidian.audio.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Objects;

public class GroupRecord {
    private String logId;

    private String queueId;

    private String recordFile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date recordStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date recordEndTime;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId == null ? null : queueId.trim();
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile == null ? null : recordFile.trim();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupRecord that = (GroupRecord) o;
        return Objects.equals(logId, that.logId) &&
                Objects.equals(queueId, that.queueId) &&
                Objects.equals(recordFile, that.recordFile) &&
                Objects.equals(recordStartTime, that.recordStartTime) &&
                Objects.equals(recordEndTime, that.recordEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, queueId, recordFile, recordStartTime, recordEndTime);
    }
}