package dev.dragoncave.yap.backend;

import java.util.Date;

public class User {
    private long userid;
    private String username;
    private Date createDate;
    private Date lastLogin;
    private String emailAddress;

    public User() {

    }

    public User(long userid, String username, Date createDate, Date lastLogin, String emailAddress) {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
