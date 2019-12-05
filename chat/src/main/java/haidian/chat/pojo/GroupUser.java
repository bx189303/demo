package haidian.chat.pojo;

import java.util.Date;

public class GroupUser {
    private Integer id;

    private String groupid;

    private String userid;

    private int isvalid;

    private Date createtime;

    public int getIsValid() {
        return isvalid;
    }

    public void setIsValid(int isValid) {
        this.isvalid = isValid;
    }

    public Date getCreateTime() {
        return createtime;
    }

    public void setCreateTime(Date createTime) {
        this.createtime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }
}