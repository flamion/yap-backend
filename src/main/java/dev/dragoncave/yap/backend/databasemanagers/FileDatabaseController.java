package dev.dragoncave.yap.backend.databasemanagers;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("SqlResolve")
public class FileDatabaseController {
	public static boolean userHasProfilePicture(long user_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getProfilePicture = dbcon.prepareStatement(
						"SELECT * FROM profile_picture_locations WHERE user_id = ?"
				)
		) {
			getProfilePicture.setLong(1, user_id);

			try (ResultSet profilePictureResultSet = getProfilePicture.executeQuery()) {
				return profilePictureResultSet.next();
			}
		}
	}


	public static void writeProfilePictureInformation(long user_id, String profile_picture_hash) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement insertProfilePictureInformationIntoDatabase = dbcon.prepareStatement(
						"INSERT INTO profile_picture_locations (user_id, name) VALUES (?, ?)"
				)
		) {
			insertProfilePictureInformationIntoDatabase.setLong(1, user_id);
			insertProfilePictureInformationIntoDatabase.setString(2, profile_picture_hash);
			insertProfilePictureInformationIntoDatabase.execute();
		}
	}

	public static void removeProfilePictureInformation(long user_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement insertProfilePictureInformationIntoDatabase = dbcon.prepareStatement(
						"DELETE FROM profile_picture_locations WHERE user_id = ?"
				)
		) {
			insertProfilePictureInformationIntoDatabase.setLong(1, user_id);
			insertProfilePictureInformationIntoDatabase.execute();
		}
	}

	public static String getProfilePictureName(long user_id) throws SQLException {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getProfilePictureLocation = dbcon.prepareStatement(
						"SELECT name FROM profile_picture_locations WHERE user_id = ?"
				)
		) {
			getProfilePictureLocation.setLong(1, user_id);
			try (ResultSet profilePictureLocation = getProfilePictureLocation.executeQuery()) {
				profilePictureLocation.next();
				return profilePictureLocation.getString("name");
			}
		}
	}
}
