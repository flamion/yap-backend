package dev.dragoncave.yap.backend.rest.security.tokens;

public class Token {

    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
