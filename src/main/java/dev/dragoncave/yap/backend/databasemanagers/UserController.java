package dev.dragoncave.yap.backend.databasemanagers;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import dev.dragoncave.yap.backend.rest.objects.User;

import java.sql.*;

public class UserController {
    private UserController() {

    }


    public static User getUserFromID(long user_id) throws SQLException {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM users WHERE user_id = ?")

        ) {
            statement.setLong(1, user_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("username"),
                            resultSet.getLong("create_date"),
                            resultSet.getLong("last_login"),
                            resultSet.getString("email_address")
                    );
                }
            }
        }
        return null;
    }

    public static void updateUser(User user) throws SQLException {
        User oldUser = getUserByID(user.getUserid());
        String username = user.getUsername();
        String emailAddress = user.getEmailAddress();

        if (username == null) {
            username = oldUser.getUsername();
        }
        if (emailAddress == null) {
            emailAddress = oldUser.getEmailAddress();
        }

        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "UPDATE users " +
                                "SET username = ?, email_address = ?" +
                                "WHERE user_id = ?"
                )
        ) {
            statement.setString(1, username);
            statement.setString(2, emailAddress);
            statement.setLong(3, user.getUserid());

            statement.execute();
        }
    }

    //returns the ID of the just created user or -1 if something went wrong
    public static long createUser(String username, String password, long create_date, long last_login, String email_address) throws SQLException {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "INSERT INTO users (username, password, create_date, last_login, email_address)" +
                                "VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS //Add to prepared statement as second argument next to the sql because we otherwise don't get the generated ID unlike with MySQL or SQLITE
                )
        ) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setLong(3, create_date);
            statement.setLong(4, last_login);
            statement.setString(5, email_address);
            statement.execute();

            try (ResultSet new_user_id_resultset = statement.getGeneratedKeys()) {
                if (new_user_id_resultset.next()) {
                    return new_user_id_resultset.getLong(1);
                }
            }
        }

        return -1;
    }

    public static long createUser(User newUser) throws SQLException {
        return createUser(newUser.getUsername(), newUser.getPassword(), System.currentTimeMillis(), System.currentTimeMillis(), newUser.getEmailAddress());
    }

    public static boolean userExists(long user_id) throws SQLException {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "SELECT user_id, username, create_date, last_login, email_address" +
                                " FROM users" +
                                " WHERE user_id = ?"
                )
        ) {
            statement.setLong(1, user_id);
            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                return resultSet.next();
            }
        }
    }

    public static User getUserByID(long user_id) throws SQLException {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "SELECT user_id, username, create_date, last_login, email_address" +
                                " FROM users" +
                                " WHERE user_id = ?"
                )
        ) {
            statement.setLong(1, user_id);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new User(
                        resultSet.getLong("user_id"),
                        resultSet.getString("username"),
                        resultSet.getLong("create_date"),
                        resultSet.getLong("last_login"),
                        resultSet.getString("email_address")
                );
            }
        }
    }

    public static void deleteUser(long user_id) throws SQLException {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "DELETE FROM users WHERE user_id = ?"
                )
        ) {
            statement.setLong(1, user_id);
            statement.execute();
        }
    }

    public static String getUserJson(long user_id) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getUserFromID(user_id));
    }
}
