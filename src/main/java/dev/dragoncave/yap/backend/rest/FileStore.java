package dev.dragoncave.yap.backend.rest;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("SqlResolve")
public class FileStore {

	public static void storeProfilePicture(MultipartFile multipartFile, long userID) throws IOException, NoSuchAlgorithmException, SQLException {
		String fileHash = UserUtils.hashFile(multipartFile.getBytes());

		File destinationFile = new File("/var/www/cdn/profile_pictures/" + fileHash + multipartFile
				.getOriginalFilename()
				.substring(multipartFile.getOriginalFilename().length() - 4));
		OutputStream outputStream = new FileOutputStream(destinationFile);
		outputStream.write(multipartFile.getBytes());

		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement insertProfilePictureInformationIntoDatabase = dbcon.prepareStatement(
						"INSERT INTO profile_picture_locations (user_id, name) VALUES (?, ?)"
				)
		) {
			insertProfilePictureInformationIntoDatabase.setLong(1, userID);
			insertProfilePictureInformationIntoDatabase.setString(2, fileHash);
			insertProfilePictureInformationIntoDatabase.execute();
		}
	}
}
