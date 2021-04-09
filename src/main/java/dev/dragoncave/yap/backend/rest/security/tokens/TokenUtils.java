package dev.dragoncave.yap.backend.rest.security.tokens;


import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtils {
    private static final int TOKEN_SIZE = 32; //how many bytes the token should consist of

    public static String generateToken() {
        byte[] randomBytes = new byte[TOKEN_SIZE];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}
