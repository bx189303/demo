package haidian.duty.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Duty {
    private Integer id;

    private String policeid;

    private String policenum;

    private String policename;

    private String unitid;

    private String unitname;

    private String duty;

    private String tel;

    private String nx;

    private String wx;

    private String ydjw;

    private String tel800m;

    private String carnum;

    private String idcard;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    private Integer isvalid;

    private Date createtime;

    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPoliceid() {
        return policeid;
    }

    public void setPoliceid(String policeid) {
        this.policeid = policeid == null ? null : policeid.trim();
    }

    public String getPolicenum() {
        return policenum;
    }

    public void setPolicenum(String policenum) {
        this.policenum = policenum == null ? null : policenum.trim();
    }

    public String getPolicename() {
        return policename;
    }

    public void setPolicename(String policename) {
        this.policename = policename == null ? null : policename.trim();
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid == null ? null : unitid.trim();
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname == null ? null : unitname.trim();
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getNx() {
        return nx;
    }

    public void setNx(String nx) {
        this.nx = nx == null ? null : nx.trim();
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx == null ? null : wx.trim();
    }

    public String getYdjw() {
        return ydjw;
    }

    public void setYdjw(String ydjw) {
        this.ydjw = ydjw == null ? null : ydjw.trim();
    }

    public String getTel800m() {
        return tel800m;
    }

    public void setTel800m(String tel800m) {
        this.tel800m = tel800m == null ? null : tel800m.trim();
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum == null ? null : carnum.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }


}