package dev.dragoncave.yap.backend.rest.objects;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.databasemanagers.UserController;

import java.sql.SQLException;

public class Entry {
	private long entryID = -1;
	private User creator;
	private long createDate = -1;
	private long dueDate = -1;
	private String title;
	private String description;

	public Entry(long entryID, User creator, long createDate, long dueDate, String title, String description) {
		this.entryID = entryID;
		this.creator = creator;
		this.createDate = createDate;
		this.dueDate = dueDate;
		this.title = title;
		this.description = description;
	}

	public Entry(long creatorId, long dueDate, String title, String description) throws SQLException {
		this.creator = UserController.getUserByID(creatorId);
		this.createDate = System.currentTimeMillis();
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

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getDueDate() {
		return dueDate;
	}

	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public boolean isInvalid() throws SQLException {
		return (creator == null || !UserController.userExists(creator.getUserid()) || title == null || description == null || entryID == -1);
	}
}
