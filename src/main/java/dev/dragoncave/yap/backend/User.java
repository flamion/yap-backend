package dev.dragoncave.yap.backend;

public class User {
    private long userid;
    private String username;
    private long createDate;
    private long lastLogin;
    private String emailAddress;

    public User() {

    }

    public User(long userid, String username, long createDate, long lastLogin, String emailAddress) {
        this.userid = userid;
        this.username = username;
        this.createDate = createDate;
        this.lastLogin = lastLogin;
        this.emailAddress = emailAddress;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
}
