package dev.dragoncave.yap.backend.rest.security.tokens;

import java.util.List;

public interface Tokenstore {
    public List<Token> getTokensByUserId(long userId);

    public List<Token> getTokensByEmail(String emailAddress);

    public String createToken(long userId);

    public void invalidateToken(String token);

    public void invalidateAllUserTokens(long userId);
}
