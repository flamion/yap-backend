package dev.dragoncave.yap.backend.rest.objects;

import dev.dragoncave.yap.backend.databasemanagers.UserController;

import java.sql.SQLException;

public class Board {
	private long boardID;
	private String name;
	private long createDate;
	private User creator;

	public Board() {

	}

	public Board(long boardId, String name, long createDate, User creator) {
		this.boardID = boardId;
		this.name = name;
		this.createDate = createDate;
		this.creator = creator;
	}

	public long getBoardID() {
		return boardID;
	}

	public void setBoardID(long boardID) {
		this.boardID = boardID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public boolean boardIsInvalid() throws SQLException {
		return boardID == -1 && name == null && creator == null && !UserController.userExists(creator.getUserID());
	}
}
