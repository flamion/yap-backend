package dev.dragoncave.yap.backend.rest.objects;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.databasemanagers.UserController;

import java.sql.SQLException;

public class Entry {
	private long entryID = -1;
	private long creatorID = -1;
	private long createDate = -1;
	private long dueDate = -1;
	private String title;
	private String description;
	private long boardID = -1;

	public Entry(long entryID, long creator, long createDate, long dueDate, String title, String description, long boardID) {
		this.entryID = entryID;
		this.creatorID = creator;
		this.createDate = createDate;
		this.dueDate = dueDate;
		this.title = title;
		this.description = description;
		this.boardID = boardID;
	}

	public Entry() {

	}

	public long getBoardID() {
		return boardID;
	}

	public void setBoardID(long boardID) {
		this.boardID = boardID;
	}

	public long getEntryID() {
		return entryID;
	}

	public void setEntryID(long entryID) {
		this.entryID = entryID;
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

	public long getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(long creator) {
		this.creatorID = creator;
	}

	public boolean isInvalid() throws SQLException {
		return (title == null || description == null);
	}
}
