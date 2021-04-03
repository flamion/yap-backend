package dev.dragoncave.yap.backend.rest.Controllers;

import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class RestUserController {


    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        try {
            if (!UserController.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(UserController.getUserJson(id), HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> putEntry(@PathVariable Long id, @RequestBody User user) {
        try {
            //prevent manipulation of the id inside the user object but allow if it absent from the object
            if (id != user.getUserid()) {
                user.setUserid(id);
            }

            if (user.isInvalid()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (!UserController.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            UserController.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(
            value = "/user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> newUser(@RequestBody User newUser) {
        try {
            long newUserId = UserController.createUser(newUser);
            return new ResponseEntity<>(newUserId, HttpStatus.CREATED);
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 19) {
                return new ResponseEntity<>("Field missing", HttpStatus.BAD_REQUEST);
            }
            exception.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus
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
