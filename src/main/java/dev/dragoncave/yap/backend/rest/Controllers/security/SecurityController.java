package dev.dragoncave.yap.backend.rest.Controllers.security;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @PostMapping("/password/update")
    public ResponseEntity<?> changePassword(HashMap<String, String> args) {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement passwordUpdateStatement = dbcon.prepareStatement(
                        "UPDATE users SET password = ? WHERE user_id = ?"
                )
        ) {
            String password = args.get("password");
            String newPassword = args.get("newPassword");
            long userId = Long.parseLong(args.get("userId"));

            if (password == null || newPassword == null) {

            }


        } catch (Exception exception) {
            if (exception instanceof NumberFormatException) {
                return new ResponseEntity<>("Supplied User ID is not a number", HttpStatus.BAD_REQUEST);
            }
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}