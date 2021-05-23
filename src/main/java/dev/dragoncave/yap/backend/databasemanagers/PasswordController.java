package dev.dragoncave.yap.backend.databasemanagers;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("SqlResolve")
public class PasswordController {
	public static void insertPasswordResetCode(String email_address, String reset_code) throws SQLException {
		insertPasswordResetCode(UserController.getUserIdFromEmailAddress(email_address), reset_code);
	}

	public static void insertPasswordResetCode(long user_id, String reset_code) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement insertResetCode = dbcon.prepareStatement(
						"INSERT INTO password_resets (user_id, valid_until, reset_code) VALUES (?, ?, ?)"
				)
		) {
			insertResetCode.setLong(1, user_id);
			insertResetCode.setLong(2, System.currentTimeMillis() + 1000 * 60 * 15);
			insertResetCode.setString(3, reset_code);

			insertResetCode.execute();
		}
	}

	public static boolean resetCodeIsValid(String email_address, String reset_code) throws SQLException {
		return resetCodeIsValid(UserController.getUserIdFromEmailAddress(email_address), reset_code);
	}

	public static boolean resetCodeIsValid(long user_id, String reset_code) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getCode = dbcon.prepareStatement(
						"SELECT * FROM password_resets WHERE user_id = ? AND reset_code = ?"
				)
		) {
			getCode.setLong(1, user_id);
			getCode.setString(2, reset_code);

			try (ResultSet code = getCode.executeQuery()) {
				return code.next() && System.currentTimeMillis() < code.getLong("valid_until");
			}
		}
	}

	public static void removeResetCode(String reset_code) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement removeResetCode = dbcon.prepareStatement(
						"DELETE FROM password_resets WHERE reset_code = ?"
				)
		) {
			removeResetCode.setString(1, reset_code);
			removeResetCode.execute();
		}
	}

	public static void resetUserPassword(long user_id, String newPassword) throws NoSuchAlgorithmException, SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement setNewPassword = dbcon.prepareStatement(
						"UPDATE users SET password = ? WHERE user_id = ?;" +
								"UPDATE password_salts SET salt = ? WHERE user_id = ?"
				)
		) {
			String newSalt = PasswordUtils.getBase64Salt();
			String newPasswordHashed = PasswordUtils.getHash(newPassword, newSalt);

			setNewPassword.setString(1, newPasswordHashed);
			setNewPassword.setLong(2, user_id);
			setNewPassword.setString(3, newSalt);
			setNewPassword.setLong(4, user_id);

			setNewPassword.execute();
		}
	}
}
