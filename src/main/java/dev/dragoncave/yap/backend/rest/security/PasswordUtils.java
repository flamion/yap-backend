package dev.dragoncave.yap.backend.rest.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

	public static String getBase64Salt() {
		return Base64.getEncoder().encodeToString(getSalt());
	}

	public static byte[] getSalt() {
		byte[] bytes = new byte[16];
		new SecureRandom().nextBytes(bytes);
		return bytes;
	}

	public static boolean isValidPassword(String password) {
		return password != null && password.length() >= 10;
	}

	public static boolean isExpectedPassword(String password, String base64Salt, String expectedHash) throws NoSuchAlgorithmException {
		return isExpectedPassword(password, Base64.getDecoder().decode(base64Salt), expectedHash);
	}

	public static boolean isExpectedPassword(String password, byte[] salt, String expectedHash) throws NoSuchAlgorithmException {
		return getHash(password, salt).equals(expectedHash);
	}

	public static String getHash(String input, String base64Salt) throws NoSuchAlgorithmException {
		return getHash(input, Base64.getDecoder().decode(base64Salt));
	}

	public static String getHash(String input, byte[] salt) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA3-512");
		digest.update(input.getBytes(StandardCharsets.UTF_8));
		digest.update(salt);
		byte[] encodedHash = digest.digest();
		return bytesToHex(encodedHash);
	}

	public static String getHash(String input) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA3-512");
		byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(encodedHash);
	}

	public static String bytesToHex(byte[] hash) {
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
