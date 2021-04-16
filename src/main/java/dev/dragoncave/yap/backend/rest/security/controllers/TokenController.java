package dev.dragoncave.yap.backend.rest.security.controllers;

import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("/security/token")
public class TokenController {
    private static final Tokenstore tokenStore = new DatabaseTokenStore();

    @PostMapping(produces = {MediaType.TEXT_PLAIN_VALUE})
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
            return new ResponseEntity<>("Incorrect email address or password", HttpStatus.FORBIDDEN);
        } catch (SQLException | NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(produces = {MediaType.TEXT_PLAIN_VALUE})
    @RequestMapping("/checkValid")
    public ResponseEntity<?> tokenIsValid(@RequestHeader(value = "Token") String token) {
        if (tokenStore.tokenIsValid(token)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
}