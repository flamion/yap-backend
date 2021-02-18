package dev.dragoncave.yap.backend;

import java.util.Date;

public class Entry {
    private long entryID;
    private User creator;
    private Date createDate;
    private Date dueDate;
    private String title;
    private String description;

    public Entry(long entryID, User creator, Date createDate, Date dueDate, String title, String description) {
        this.entryID = entryID;
        this.creator = creator;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.title = title;
        this.description = description;
    }

    public Entry() {

    }

    public long getEntryID() {
        return entryID;
    }

    public void setEntryID(long entryID) {
        this.entryID = entryID;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
