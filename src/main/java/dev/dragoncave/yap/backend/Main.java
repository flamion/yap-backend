package dev.dragoncave.yap.backend;

import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.TokenUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SuppressWarnings("ALL")
public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println(TokenUtils.generateToken());
	}

	private static String getPasswordHash(String input, String saltString) throws NoSuchAlgorithmException {
		byte[] salt = Base64.getDecoder().decode(saltString);

		MessageDigest digest = MessageDigest.getInstance("SHA3-512");
		digest.update(input.getBytes(StandardCharsets.UTF_8));
		digest.update(salt);
		byte[] encodedHash = digest.digest();
		return PasswordUtils.bytesToHex(encodedHash);
	}
}