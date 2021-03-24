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

    //todo: Add Json response with Http Status code
    @GetMapping("/user/{id}")
    User getUser(@PathVariable("id") Long id) throws SQLException {
        return dbManager.getUserByID(id);
    }

    @PostMapping(
            value = "/user/",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public long newUser(@RequestBody User newUser) throws SQLException {
        return dbManager.createUser(newUser);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!dbManager.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            dbManager.deleteUser(id);
        } catch (SQLException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
