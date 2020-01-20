package haidian.chatSip.pojo;

public class Friend {
    private Integer id;

    private String userid1;

    private String userid2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid1() {
        return userid1;
    }

    public void setUserid1(String userid1) {
        this.userid1 = userid1 == null ? null : userid1.trim();
    }

    public String getUserid2() {
        return userid2;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2 == null ? null : userid2.trim();
    }
}