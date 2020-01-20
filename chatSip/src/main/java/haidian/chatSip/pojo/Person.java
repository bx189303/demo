package haidian.chatSip.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Person {
    private String sId;

    private String sName;

    private String sPassword;

    private String sUnit;

    private String sUnitname;

    private String sDescription;

    private String sSex;

    private BigDecimal nSequence;

    private String sIdentification;

    private String sDuty;

    private String sTel;

    private String sPhone;

    private String sEmail;

    private String sFax;

    private String sSt;

    private String sJwt;

    private String sPolicenum;

    private String sDtel;

    private Integer nDisuse;

    private String sGroup;

    private String sCategory;

    private String sAssignment;

    private BigDecimal nStatus;

    private BigDecimal nNormal;

    private Date dCreatetime;

    private Date dUpdatetime;

    private String sRemarks;

    private String sRemarkstwo;

    private String sRemarksthree;

    private String sDeviceid;

    private BigDecimal nIsdelete;

    private Short nVersion;

    private String sIp;

    private String sPhoto;

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId == null ? null : sId.trim();
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName == null ? null : sName.trim();
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword == null ? null : sPassword.trim();
    }

    public String getsUnit() {
        return sUnit;
    }

    public void setsUnit(String sUnit) {
        this.sUnit = sUnit == null ? null : sUnit.trim();
    }

    public String getsUnitname() {
        return sUnitname;
    }

    public void setsUnitname(String sUnitname) {
        this.sUnitname = sUnitname == null ? null : sUnitname.trim();
    }

    public String getsDescription() {
        return sDescription;
    }

    public void setsDescription(String sDescription) {
        this.sDescription = sDescription == null ? null : sDescription.trim();
    }

    public String getsSex() {
        return sSex;
    }

    public void setsSex(String sSex) {
        this.sSex = sSex == null ? null : sSex.trim();
    }

    public BigDecimal getnSequence() {
        return nSequence;
    }

    public void setnSequence(BigDecimal nSequence) {
        this.nSequence = nSequence;
    }

    public String getsIdentification() {
        return sIdentification;
    }

    public void setsIdentification(String sIdentification) {
        this.sIdentification = sIdentification == null ? null : sIdentification.trim();
    }

    public String getsDuty() {
        return sDuty;
    }

    public void setsDuty(String sDuty) {
        this.sDuty = sDuty == null ? null : sDuty.trim();
    }

    public String getsTel() {
        return sTel;
    }

    public void setsTel(String sTel) {
        this.sTel = sTel == null ? null : sTel.trim();
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone == null ? null : sPhone.trim();
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail == null ? null : sEmail.trim();
    }

    public String getsFax() {
        return sFax;
    }

    public void setsFax(String sFax) {
        this.sFax = sFax == null ? null : sFax.trim();
    }

    public String getsSt() {
        return sSt;
    }

    public void setsSt(String sSt) {
        this.sSt = sSt == null ? null : sSt.trim();
    }

    public String getsJwt() {
        return sJwt;
    }

    public void setsJwt(String sJwt) {
        this.sJwt = sJwt == null ? null : sJwt.trim();
    }

    public String getsPolicenum() {
        return sPolicenum;
    }

    public void setsPolicenum(String sPolicenum) {
        this.sPolicenum = sPolicenum == null ? null : sPolicenum.trim();
    }

    public String getsDtel() {
        return sDtel;
    }

    public void setsDtel(String sDtel) {
        this.sDtel = sDtel == null ? null : sDtel.trim();
    }

    public Integer getnDisuse() {
        return nDisuse;
    }

    public void setnDisuse(Integer nDisuse) {
        this.nDisuse = nDisuse;
    }

    public String getsGroup() {
        return sGroup;
    }

    public void setsGroup(String sGroup) {
        this.sGroup = sGroup == null ? null : sGroup.trim();
    }

    public String getsCategory() {
        return sCategory;
    }

    public void setsCategory(String sCategory) {
        this.sCategory = sCategory == null ? null : sCategory.trim();
    }

    public String getsAssignment() {
        return sAssignment;
    }

    public void setsAssignment(String sAssignment) {
        this.sAssignment = sAssignment == null ? null : sAssignment.trim();
    }

    public BigDecimal getnStatus() {
        return nStatus;
    }

    public void setnStatus(BigDecimal nStatus) {
        this.nStatus = nStatus;
    }

    public BigDecimal getnNormal() {
        return nNormal;
    }

    public void setnNormal(BigDecimal nNormal) {
        this.nNormal = nNormal;
    }

    public Date getdCreatetime() {
        return dCreatetime;
    }

    public void setdCreatetime(Date dCreatetime) {
        this.dCreatetime = dCreatetime;
    }

    public Date getdUpdatetime() {
        return dUpdatetime;
    }

    public void setdUpdatetime(Date dUpdatetime) {
        this.dUpdatetime = dUpdatetime;
    }

    public String getsRemarks() {
        return sRemarks;
    }

    public void setsRemarks(String sRemarks) {
        this.sRemarks = sRemarks == null ? null : sRemarks.trim();
    }

    public String getsRemarkstwo() {
        return sRemarkstwo;
    }

    public void setsRemarkstwo(String sRemarkstwo) {
        this.sRemarkstwo = sRemarkstwo == null ? null : sRemarkstwo.trim();
    }

    public String getsRemarksthree() {
        return sRemarksthree;
    }

    public void setsRemarksthree(String sRemarksthree) {
        this.sRemarksthree = sRemarksthree == null ? null : sRemarksthree.trim();
    }

    public String getsDeviceid() {
        return sDeviceid;
    }

    public void setsDeviceid(String sDeviceid) {
        this.sDeviceid = sDeviceid == null ? null : sDeviceid.trim();
    }

    public BigDecimal getnIsdelete() {
        return nIsdelete;
    }

    public void setnIsdelete(BigDecimal nIsdelete) {
        this.nIsdelete = nIsdelete;
    }

    public Short getnVersion() {
        return nVersion;
    }

    public void setnVersion(Short nVersion) {
        this.nVersion = nVersion;
    }

    public String getsIp() {
        return sIp;
    }

    public void setsIp(String sIp) {
        this.sIp = sIp == null ? null : sIp.trim();
    }

    public String getsPhoto() {
        return sPhoto;
    }

    public void setsPhoto(String sPhoto) {
        this.sPhoto = sPhoto == null ? null : sPhoto.trim();
    }
}