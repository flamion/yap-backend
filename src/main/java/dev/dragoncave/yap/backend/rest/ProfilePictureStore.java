package dev.dragoncave.yap.backend.rest;

import dev.dragoncave.yap.backend.databasemanagers.FileDatabaseController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@SuppressWarnings("SqlResolve")
public class ProfilePictureStore {

	private static final Pattern threeCharPictureExtension = Pattern.compile("(.*?)\\.(png|jpg)");
	private static final Pattern fourCharPictureExtension = Pattern.compile("(.*?)\\.(jpeg)");

	private static final String BACKEND_DOMAIN = "https://backend.yap.dragoncave.dev";
	private static final String CDN_DOMAIN = "https://dragoncave.dev:42069/cdn";
	private static final String PROFILE_PICTURE_ROOT = "/var/www/cdn/profile_pictures/";

	public static void storeProfilePicture(MultipartFile multipartFile, long userID) throws IOException, NoSuchAlgorithmException, SQLException {
		String fileHashName = UserUtils.hashFile(multipartFile.getBytes());
		String filename;
		if (multipartFile.getOriginalFilename() == null) {
			filename = "";
		} else {
			filename = multipartFile.getOriginalFilename();
		}

		if (FileDatabaseController.userHasProfilePicture(userID)) {
			try {
				File file = new File(PROFILE_PICTURE_ROOT + FileDatabaseController.getProfilePictureName(userID));
				FileDatabaseController.removeProfilePictureInformation(userID);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String fileEnding = getFileEnding(filename);

		File destinationFile = new File(PROFILE_PICTURE_ROOT + fileHashName + fileEnding);
		OutputStream outputStream = new FileOutputStream(destinationFile);
		outputStream.write(multipartFile.getBytes());

		FileDatabaseController.writeProfilePictureInformation(userID, fileHashName + fileEnding);
	}

	public static String getProfilePictureLocation(long user_id) throws SQLException {
		return CDN_DOMAIN + "/profile_pictures/" + FileDatabaseController.getProfilePictureName(user_id);
	}

	private static String getFileEnding(String filename) {
		String lowercaseFilename = filename.toLowerCase();

		if (threeCharPictureExtension.matcher(lowercaseFilename).matches()) {
			return "." + lowercaseFilename.substring(lowercaseFilename.length() - 3);
		}
		if (fourCharPictureExtension.matcher(lowercaseFilename).matches()) {
			return "." + lowercaseFilename.substring(lowercaseFilename.length() - 4);
		}
		throw new IllegalArgumentException();
	}
}
