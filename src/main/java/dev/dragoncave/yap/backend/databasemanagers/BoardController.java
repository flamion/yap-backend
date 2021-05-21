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

			var resultSet = getUserBoards.executeQuery(); //TODO: decide whether to put in try with resources

			while (resultSet.next()) {
				boardIDs.add(resultSet.getLong("user_id"));
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
						"INSERT INTO member_in_board (user_id, board_id) VALUES (?, ?);" +
								"INSERT INTO admin_in_board (user_id, board_id) VALUES (?, ?)"
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
			grantBoardAccess.setLong(3, user_id);
			grantBoardAccess.setLong(4, newBoardID);

			grantBoardAccess.execute();

			return newBoardID;
		}
	}

	public static long createNewBoard(long user_id, Board newBoard) throws SQLException {
		return createNewBoard(user_id, newBoard.getName(), System.currentTimeMillis());
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
						"SELECT * FROM admin_in_board WHERE user_id = ? AND board_id =?"
				)
		) {
			getIsAdmin.setLong(1, user_id);
			getIsAdmin.setLong(2, board_id);

			try (ResultSet isAdminResult = getIsAdmin.executeQuery()) {
				return isAdminResult.next();
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
				)
		) {
			getBoard.setLong(1, board_id);

			try (var boardSet = getBoard.executeQuery()) {
				boardSet.next();
				return new Board(
						board_id,
						boardSet.getString("name"),
						boardSet.getLong("create_date"),
						UserController.getUserByID(boardSet.getLong("creator"))
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
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement addAdminToBoard = dbcon.prepareStatement(
						"INSERT INTO admin_in_board (user_id, board_id) VALUES (?, ?)"
				)
		) {
			addAdminToBoard.setLong(1, user_id);
			addAdminToBoard.setLong(2, board_id);

			addAdminToBoard.execute();
		}
	}
}
