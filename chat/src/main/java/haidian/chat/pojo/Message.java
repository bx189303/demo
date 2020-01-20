package haidian.chat.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Message {
    private String id;

    private String src;

    private String dst;

    private String dsttype;

    private String readid;

    private String content;

    private String contenttype;

    private String filename;

    private Integer filesize;

    private Integer fileduration;

    private Integer isvalid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivetime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src == null ? null : src.trim();
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst == null ? null : dst.trim();
    }

    public String getDsttype() {
        return dsttype;
    }

    public void setDsttype(String dsttype) {
        this.dsttype = dsttype == null ? null : dsttype.trim();
    }

    public String getReadid() {
        return readid;
    }

    public void setReadid(String readid) {
        this.readid = readid == null ? null : readid.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype == null ? null : contenttype.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public Integer getFileduration() {
        return fileduration;
    }

    public void setFileduration(Integer fileduration) {
        this.fileduration = fileduration;
    }

    public Integer getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public Date getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
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