package dev.dragoncave.yap.backend.rest.security.controllers;

import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @PostMapping("/password/update")
    public ResponseEntity<?> changePassword(@RequestBody HashMap<String, String> args) {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement passwordUpdateStatement = dbcon.prepareStatement(
                        "UPDATE users SET password = ? WHERE user_id = ?"
                )
        ) {
            System.out.println(Arrays.toString(args.keySet().toArray(new String[0])));
            String password = args.get("password");
            String newPassword = args.get("newPassword");
            long userId = Long.parseLong(args.get("userId"));

            if (!PasswordUtils.isValidPassword(password) || !PasswordUtils.isValidPassword(newPassword)) {
                return new ResponseEntity<>("Either password or newPassword is invalid", HttpStatus.BAD_REQUEST);
            }

            if (!UserController.userExists(userId)) {
                return new ResponseEntity<>("Invalid User ID", HttpStatus.BAD_REQUEST);
            }

            if (!UserController.passwordMatches(userId, password)) {
                System.out.println(password);
                return new ResponseEntity<>("Wrong Password", HttpStatus.BAD_REQUEST);
            }

            UserController.updatePassword(userId, newPassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            if (exception instanceof NumberFormatException) {
                return new ResponseEntity<>("Supplied User ID is not a number", HttpStatus.BAD_REQUEST);
            }
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}