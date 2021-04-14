package dev.dragoncave.yap.backend.rest.controllers;

import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.User;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class RestUserController {
    Tokenstore tokenStore = new DatabaseTokenStore();

    @GetMapping()
    public ResponseEntity<?> getUser(@RequestHeader(value = "Token") String token) {
        try {
            if (!tokenStore.tokenIsValid(token)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            long userId = tokenStore.getUserIdByToken(token);
            return new ResponseEntity<>(UserController.getUserJson(userId), HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/entries")
    public ResponseEntity<?> getEntries(@RequestHeader(value = "Token") String token) {
        try {
            if (!tokenStore.tokenIsValid(token)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            long userId = tokenStore.getUserIdByToken(token);
            return new ResponseEntity<>(EntryController.getUserEntries(userId), HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping()
    public ResponseEntity<?> putEntry(@RequestHeader(value = "Token") String token, @RequestBody User user) {
        try {
            if (!tokenStore.tokenIsValid(token)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            long id = tokenStore.getUserIdByToken(token);
            user.setUserid(id);

            if (user.isInvalid()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            UserController.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> newUser(@RequestBody User newUser) {
        try {
            if (newUser.isInvalid()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (UserController.getUserIdFromEmailAddress(newUser.getEmailAddress()) != -1) {
                return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
            }

            if (!PasswordUtils.isValidPassword(newUser.getPassword())) {
                return new ResponseEntity<>("Invalid Password supplied", HttpStatus.BAD_REQUEST);
            }

            long newUserId = UserController.createUser(newUser);
            return new ResponseEntity<>(newUserId, HttpStatus.CREATED);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!UserController.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            UserController.deleteUser(id);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}