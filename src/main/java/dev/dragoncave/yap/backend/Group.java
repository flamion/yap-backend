package dev.dragoncave.yap.backend;

import com.google.gson.Gson;

public class Group {
    private long groupID;
    private String groupName;
    private User creator;
    private long createDate;
    private long lastAccessDate;

    public Group(long groupID, String groupName, User creator, long createDate, long lastAccessDate) {
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(long lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
