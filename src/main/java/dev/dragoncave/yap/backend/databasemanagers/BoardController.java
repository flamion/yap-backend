package dev.dragoncave.yap.backend.databasemanagers;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import dev.dragoncave.yap.backend.rest.objects.Board;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class BoardController {

	private BoardController() {
	}

	public static List<Long> getUserBoards(long user_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getUserBoards = dbcon.prepareStatement(
						"SELECT board_id FROM member_in_board WHERE user_id = ?"
				)
		) {
			getUserBoards.setLong(1, user_id);


			List<Long> boardIDs = new ArrayList<>();
			try (ResultSet userBoards = getUserBoards.executeQuery()) {
				while (userBoards.next()) {
					boardIDs.add(userBoards.getLong("board_id"));
				}
			}

			return boardIDs;
		}
	}

	public static long createNewBoard(long user_id, String name, long create_date) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement createNewBoard = dbcon.prepareStatement(
						"INSERT INTO boards (name, create_date, creator) VALUES (?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS
				);
				PreparedStatement grantBoardAccess = dbcon.prepareStatement(
						"INSERT INTO member_in_board (user_id, board_id) VALUES (?, ?);"
				)
		) {
			createNewBoard.setString(1, name);
			createNewBoard.setLong(2, create_date);
			createNewBoard.setLong(3, user_id);

			createNewBoard.execute();

			long newBoardID = -1;

			try (ResultSet generatedKeys = createNewBoard.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					newBoardID = generatedKeys.getLong(1);
				}
			}

			if (newBoardID == -1) {
				return newBoardID;
			}

			grantBoardAccess.setLong(1, user_id);
			grantBoardAccess.setLong(2, newBoardID);

			grantBoardAccess.execute();

			addAdminToBoard(user_id, newBoardID);

			return newBoardID;
		}
	}

	public static long createNewBoard(long user_id, Board newBoard) throws SQLException {
		return createNewBoard(user_id, newBoard.getName(), System.currentTimeMillis());
	}

	public static void modifyBoardName(long board_id, String newName) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement updateBoard = dbcon.prepareStatement(
						"UPDATE boards SET name = ? WHERE board_id = ?"
				)
		) {
			updateBoard.setString(1, newName);
			updateBoard.setLong(2, board_id);

			updateBoard.execute();
		}
	}

	public static boolean userIsBoardMember(long user_id, long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement canAccess = dbcon.prepareStatement(
						"SELECT * FROM member_in_board WHERE user_id = ? AND board_id = ?"
				)
		) {
			canAccess.setLong(1, user_id);
			canAccess.setLong(2, board_id);

			return canAccess.executeQuery().next();
		}
	}

	public static boolean userIsBoardAdmin(long user_id, long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getIsAdmin = dbcon.prepareStatement(
						"SELECT permission_level FROM member_in_board WHERE user_id = ? AND board_id =?"
				)
		) {
			getIsAdmin.setLong(1, user_id);
			getIsAdmin.setLong(2, board_id);

			try (ResultSet isAdminResult = getIsAdmin.executeQuery()) {
				isAdminResult.next();
				return isAdminResult.getInt("permission_level") >= 100;
			}

		}
	}

	public static boolean boardExists(long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM boards WHERE board_id = ?")
		) {
			statement.setLong(1, board_id);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	public static Board getBoardByID(long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getBoard = dbcon.prepareStatement(
						"SELECT * FROM boards WHERE board_id = ?"
				);
				PreparedStatement getMembers = dbcon.prepareStatement(
						"SELECT user_id FROM member_in_board WHERE board_id = ?"
				)
		) {
			getBoard.setLong(1, board_id);
			getMembers.setLong(1, board_id);

			try (
					var boardSet = getBoard.executeQuery();
					var members = getMembers.executeQuery()
			) {
				boardSet.next();
				members.next();

				List<Long> memberIDs = new ArrayList<>();
				while (members.next()) {
					memberIDs.add(members.getLong("user_id"));
				}

				return new Board(
						board_id,
						boardSet.getString("name"),
						boardSet.getLong("create_date"),
						boardSet.getLong("creator"),
						memberIDs
				);
			}
		}
	}

	public static void addMemberToBoard(long user_id, long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement addMemberToBoard = dbcon.prepareStatement(
						"INSERT INTO member_in_board (user_id, board_id) VALUES (?, ?)"
				)
		) {
			addMemberToBoard.setLong(1, user_id);
			addMemberToBoard.setLong(2, board_id);

			addMemberToBoard.execute();
		}
	}

	public static void addAdminToBoard(long user_id, long board_id) throws SQLException {
		setMemberPermissionLevel(user_id, board_id, 100);
	}

	public static void setMemberPermissionLevel(long user_id, long board_id, int permission_level) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement addAdminToBoard = dbcon.prepareStatement(
						"UPDATE member_in_board SET permissionlevel = ? WHERE user_id = ? AND board_id = ?"
				)
		) {
			addAdminToBoard.setInt(1, permission_level);
			addAdminToBoard.setLong(2, user_id);
			addAdminToBoard.setLong(3, board_id);

			addAdminToBoard.execute();
		}
	}

	public static int getUserPermissionLevel(long user_id, long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getPermissionlevel = dbcon.prepareStatement(
						"SELECT permission_level FROM member_in_board WHERE user_id = ? AND board_id = ?"
				)
		) {
			getPermissionlevel.setLong(1, user_id);
			getPermissionlevel.setLong(2, board_id);

			try (ResultSet permissionLevel = getPermissionlevel.executeQuery()) {
				permissionLevel.next();
				return permissionLevel.getInt("permission_level");
			}
		}
	}

	public static void removeMemberFromBoard(long user_id, long board_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement removeMember = dbcon.prepareStatement(
						"DELETE FROM member_in_board WHERE user_id = ? AND board_id = ?;"
				)
		) {
			removeMember.setLong(1, user_id);
			removeMember.setLong(2, board_id);

			removeMember.execute();
		}
	}

	public static void deleteBoard(long boardID) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement deleteBoard = dbcon.prepareStatement(
						"DELETE FROM boards WHERE board_id = ?"
				)
		) {
			deleteBoard.setLong(1, boardID);
			deleteBoard.execute();
		}
	}
}
