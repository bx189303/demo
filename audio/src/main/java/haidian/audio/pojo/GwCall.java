package haidian.audio.pojo;

import java.util.Date;

public class GwCall {
    private String callId;

    private String gwName;

    private Integer lineId;

    private String lineName;

    private Integer callType;

    private String callingNbr;

    private String calledNbr;

    private Integer connectFlag;

    private Integer endCode;

    private Date callStartTime;

    private Date talkStartTime;

    private Date talkEndTime;

    private Integer isReaded;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId == null ? null : callId.trim();
    }

    public String getGwName() {
        return gwName;
    }

    public void setGwName(String gwName) {
        this.gwName = gwName == null ? null : gwName.trim();
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public String getCallingNbr() {
        return callingNbr;
    }

    public void setCallingNbr(String callingNbr) {
        this.callingNbr = callingNbr == null ? null : callingNbr.trim();
    }

    public String getCalledNbr() {
        return calledNbr;
    }

    public void setCalledNbr(String calledNbr) {
        this.calledNbr = calledNbr == null ? null : calledNbr.trim();
    }

    public Integer getConnectFlag() {
        return connectFlag;
    }

    public void setConnectFlag(Integer connectFlag) {
        this.connectFlag = connectFlag;
    }

    public Integer getEndCode() {
        return endCode;
    }

    public void setEndCode(Integer endCode) {
        this.endCode = endCode;
    }

    public Date getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Date getTalkStartTime() {
        return talkStartTime;
    }

    public void setTalkStartTime(Date talkStartTime) {
        this.talkStartTime = talkStartTime;
    }

    public Date getTalkEndTime() {
        return talkEndTime;
    }

    public void setTalkEndTime(Date talkEndTime) {
        this.talkEndTime = talkEndTime;
    }

    public Integer getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(Integer isReaded) {
        this.isReaded = isReaded;
    }
}