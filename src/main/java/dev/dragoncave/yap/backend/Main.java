package dev.dragoncave.yap.backend;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SuppressWarnings("ALL")
public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println(getPasswordHash("a", "1Im3jY3v3qgCqgYHX97iVg=="));
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