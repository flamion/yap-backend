package dev.dragoncave.yap.backend.rest.objects;

import dev.dragoncave.yap.backend.databasemanagers.UserController;

import java.sql.SQLException;

public class Board {
	private long boardID;
	private String name;
	private long createDate;
	private long creatorID;

	public Board() {

	}

	public Board(long boardId, String name, long createDate, long creatorID) {
		this.boardID = boardId;
		this.name = name;
		this.createDate = createDate;
		this.creatorID = creatorID;
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

	public long getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(long creatorID) {
		this.creatorID = creatorID;
	}

	public boolean boardIsInvalid() throws SQLException {
		return boardID == -1 && name == null && !UserController.userExists(creatorID);
	}
}
