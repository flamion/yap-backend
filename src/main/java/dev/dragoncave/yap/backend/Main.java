package dev.dragoncave.yap.backend;

import dev.dragoncave.yap.backend.databasemanagers.BoardController;
import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import dev.dragoncave.yap.backend.rest.objects.Board;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SuppressWarnings("ALL")
public class Main {
    public static void main(String[] args) throws Exception {
        Connection dbcon = ConnectionController.getConnection();
        var allUsers = dbcon.prepareStatement("SELECT * FROM users").executeQuery();

        List<Long> users = new ArrayList<>();
        while (allUsers.next()) {
            users.add(allUsers.getLong("user_id"));
        }

        Board defaultBoard = new Board();
        defaultBoard.setName("Default Board");

        for (long userID : users) {
            long newBoardID = BoardController.createNewBoard(userID, defaultBoard);

            var statement = dbcon.prepareStatement("UPDATE entry SET board_id = ? WHERE creator = ?");
            statement.setLong(1, newBoardID);
            statement.setLong(2, userID);
            statement.execute();
        }

    }

    private static String getPasswordHash(String input, String saltString) throws NoSuchAlgorithmException {
        byte[] salt = Base64.getDecoder().decode(saltString);


        MessageDigest digest = MessageDigest.getInstance("SHA3-512");
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        digest.update(salt);
        byte[] encodedHash = digest.digest();
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}