package dev.dragoncave.yap.backend.rest.security.tokens;

import java.util.List;

public interface Tokenstore {
    List<String> getTokensByUserId(long userId);

    List<String> getTokensByEmail(String emailAddress);

    String createToken(long userId);

    void invalidateToken(String token);

    void invalidateAllUserTokens(long userId);

    boolean tokenIsValid(String token);

    long getUserIdByToken(String token);
}
