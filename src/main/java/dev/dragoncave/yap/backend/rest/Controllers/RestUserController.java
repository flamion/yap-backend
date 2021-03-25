package dev.dragoncave.yap.backend.rest.Controllers;

import dev.dragoncave.yap.backend.DatabaseManager;
import dev.dragoncave.yap.backend.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class RestUserController {
    private static final DatabaseManager dbManager = DatabaseManager.getInstance();

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        try {
            if (!dbManager.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(dbManager.getUserJson(id), HttpStatus.OK);
        } catch (SQLException ex) {

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(
            value = "/user/",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> newUser(@RequestBody User newUser) {
        try {
            long newUserId = dbManager.createUser(newUser);
            return new ResponseEntity<>(String.valueOf(newUserId), HttpStatus.CREATED);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 19) {
                return new ResponseEntity<>("Field missing", HttpStatus.BAD_REQUEST);
            }
            ex.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!dbManager.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            dbManager.deleteUser(id);
        } catch (SQLException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
