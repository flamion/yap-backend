package dev.dragoncave.yap.backend.rest.Controllers.security;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityController {


    //Yes this is very shitty and slapped together in 5 minutes but it's just supposed to be a quick way to convert the database
    @GetMapping("/convert")
    public void convert() throws SQLException, NoSuchAlgorithmException {

        Connection dbcon = ConnectionController.getConnection();
        var statement = dbcon.prepareStatement(
                "SELECT user_id, password FROM users"
        );

        var result = statement.executeQuery();

        System.out.println("Got all users");

        List<Triplet<Long, String, String>> new_user_password_triplets = new ArrayList<>();

        while (result.next()) {
            long user_id = result.getLong("user_id");
            String password = result.getString("password");
            String salt = PasswordUtils.getBase64Salt();

            System.out.println("processing user " + user_id);


            new_user_password_triplets.add(Triplet.of(user_id, PasswordUtils.getHash(password, salt), salt));
        }


        var con2 = ConnectionController.getConnection();
        con2.setAutoCommit(false);

        var updateStatement = con2.prepareStatement(
                "UPDATE users SET password = ? WHERE user_id = ?;"
        );

        var insertStatement = con2.prepareStatement(
                "INSERT INTO password_salts VALUES(?, ?);"
        );

        System.out.println("created statements");

        for (var triplet : new_user_password_triplets) {
            insertStatement.setLong(1, triplet.first);
            insertStatement.setString(2, triplet.third);

            insertStatement.addBatch();

            System.out.println("Added Insert Batch for user " + triplet.first);

            updateStatement.setString(1, triplet.second);
            updateStatement.setLong(2, triplet.first);

            updateStatement.addBatch();

            System.out.println("Added Update Batch for user " + triplet.first);
        }

        System.out.println("prepared statements");

        insertStatement.executeBatch();
        updateStatement.executeBatch();

        System.out.println("executed statements");

        con2.commit();

        System.out.println("commited");
    }
}
