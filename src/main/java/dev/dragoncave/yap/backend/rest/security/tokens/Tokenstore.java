package dev.dragoncave.yap.backend.rest.security.tokens;

import java.util.List;

public interface Tokenstore {
    public List<String> getTokensByUserId(long userId);

    public List<String> getTokensByEmail(String emailAddress);

    public String createToken(long userId);

    public void invalidateToken(String token);

    public void invalidateAllUserTokens(long userId);

    public boolean tokenIsValid(String token);
}
