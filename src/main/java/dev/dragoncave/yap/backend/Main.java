package dev.dragoncave.yap.backend;

import dev.dragoncave.yap.backend.databasemanagers.BoardController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SuppressWarnings("ALL")
public class Main {
	public static void main(String[] args) throws Exception {
		Connection dbcon = ConnectionController.getConnection();
		var members = dbcon.prepareStatement("SELECT * FROM member_in_board").executeQuery();
		var admins = dbcon.prepareStatement("SELECT * FROM admin_in_board").executeQuery();

		List<Pair<Long, Long>> users = new ArrayList<>();

		while (members.next()) {
			users.add(Pair.of(members.getLong("user_id"), members.getLong("board_id")));
		}

		for (var user : users) {
			if (BoardController.userIsBoardAdmin(user.getFirst(), user.getSecond())) {
				System.out.println("Making user " + user.getFirst() + " admin in board " + user.getSecond());
				var statement = dbcon.prepareStatement("UPDATE member_in_board SET permission_level = 100 WHERE user_id = ? AND board_id = ?");
				statement.setLong(1, user.getFirst());
				statement.setLong(2, user.getSecond());
				statement.execute();

				System.out.println("Set Permissionlevel of user " + user.getFirst() + " in board " + user.getSecond() + " to 100 (admin)");
			}
		}

	}

	private static String getPasswordHash(String input, String saltString) throws NoSuchAlgorithmException {
		byte[] salt = Base64.getDecoder().decode(saltString);


		MessageDigest digest = MessageDigest.getInstance("SHA3-512");
		digest.update(input.getBytes(StandardCharsets.UTF_8));
		digest.update(salt);
		byte[] encodedHash = digest.digest();
		return bytesToHex(encodedHash);
	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}