package dev.dragoncave.yap.backend.rest.objects;

public class Board {
	private long boardId;
	private String name;
	private long createDate;
	private User creator;

	public Board() {

	}

	public Board(long boardId, String name, long createDate, User creator) {
		this.boardId = boardId;
		this.name = name;
		this.createDate = createDate;
		this.creator = creator;
	}

	public long getBoardId() {
		return boardId;
	}

	public void setBoardId(long boardId) {
		this.boardId = boardId;
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
}
