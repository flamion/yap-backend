package dev.dragoncave.yap.backend.rest.security.controllers;

import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.User;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Token;
import dev.dragoncave.yap.backend.rest.security.tokens.TokenUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("/security/token")
public class TokenController {
    private static final Tokenstore tokenStore = new DatabaseTokenStore();

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody HashMap<String, String> loginDetails) {
        if (!loginDetails.containsKey("emailAddress") && !loginDetails.containsKey("password")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String emailAddress = loginDetails.get("emailAddress");
        String password = loginDetails.get("password");

        try {
            long userId = UserController.getUserIdFromEmailAddress(emailAddress);
            if (UserController.passwordMatches(userId, password)) {
                String newToken = tokenStore.createToken(userId);
                return new ResponseEntity<>(newToken, HttpStatus.OK);
            }
            return new ResponseEntity<>("Wrong user or password", HttpStatus.UNAUTHORIZED);
        } catch (SQLException | NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
