package dev.dragoncave.yap.backend.rest.security.tokens;


import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtils {
	private static final int TOKEN_SIZE = 36; //how many bytes the token should consist of

	public static String generateToken() {
		return generateToken(TOKEN_SIZE);
	}

	public static String generateToken(int tokenSize) {
		byte[] randomBytes = new byte[tokenSize];
		new SecureRandom().nextBytes(randomBytes);
		return Base64.getUrlEncoder().encodeToString(randomBytes);
	}
}
