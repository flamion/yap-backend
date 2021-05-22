package dev.dragoncave.yap.backend.rest.objects;

import dev.dragoncave.yap.backend.databasemanagers.UserController;

import java.sql.SQLException;
import java.util.List;

public class Board {
	private long boardID;
	private String name;
	private long createDate;
	private long creatorID;
	private List<Long> members;

	public Board() {

	}

	public Board(long boardID, String name, long createDate, long creatorID, List<Long> members) {
		this.boardID = boardID;
		this.name = name;
		this.createDate = createDate;
		this.creatorID = creatorID;
		this.members = members;
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

	public List<Long> getMembers() {
		return members;
	}

	public void setMembers(List<Long> members) {
		this.members = members;
	}

	public boolean boardIsInvalid() throws SQLException {
		return boardID == -1 && name == null && !UserController.userExists(creatorID);
	}
}
