package dev.dragoncave.yap.backend;

import java.util.Date;

public class Group {
    private long groupID;
    private String groupName;
    private User creator;
    private Date createDate;
    private Date lastAccessDate;

    public Group(long groupID, String groupName, User creator, Date createDate, Date lastAccessDate) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.creator = creator;
        this.createDate = createDate;
        this.lastAccessDate = lastAccessDate;
    }

    public Group() {

    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }
}
