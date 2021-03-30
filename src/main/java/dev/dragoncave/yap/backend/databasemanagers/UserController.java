package dev.dragoncave.yap.backend.databasemanagers;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.rest.objects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    private static final Connection dbcon = ConnectionController.getInstance().getConnection();
    private static final UserController instance = new UserController();


    private UserController() {

    }

    public static UserController getInstance() {
        return instance;
    }


    public User getUserFromID(long user_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM user WHERE user_id = ?");
        statement.setLong(1, user_id);
        ResultSet result = statement.executeQuery();

        return new User(
                result.getInt("user_id"),
                result.getString("username"),
                result.getLong("create_date"),
                result.getLong("last_login"),
                result.getString("email_address")
        );
    }

    public void updateUser(User user) throws SQLException {
        User oldUser = getUserByID(user.getUserid());
        String username = user.getUsername();
        String emailAddress = user.getEmailAddress();

        if (username == null) {
            username = oldUser.getUsername();
        }
        if (emailAddress == null) {
            emailAddress = oldUser.getEmailAddress();
        }

        PreparedStatement statement = dbcon.prepareStatement(
                "UPDATE user " +
                        "SET username = ?, email_address = ?" +
                        "WHERE user_id = ?"
        );

        statement.setString(1, username);
        statement.setString(2, emailAddress);

        statement.execute();
    }

    public String getUserJson(long user_id) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getUserFromID(user_id));
    }

    //returns the ID of the just created user or -1 if something went wrong
    public long createUser(String username, String password, long create_date, long last_login, String email_address) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "INSERT INTO user (username, password, create_date, last_login, email_address)" +
                        "VALUES (?, ?, ?, ?, ?)"
        );
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setLong(3, create_date);
        statement.setLong(4, last_login);
        statement.setString(5, email_address);
        statement.execute();
        ResultSet group_id = statement.getGeneratedKeys();
        if (group_id.isBeforeFirst()) {
            return group_id.getLong(1);
        }
        return -1;
    }

    public long createUser(User newUser) throws SQLException {
        return createUser(newUser.getUsername(), newUser.getPassword(), System.currentTimeMillis(), System.currentTimeMillis(), newUser.getEmailAddress());
    }

    public boolean userExists(long user_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "SELECT user_id, username, create_date, last_login, email_address" +
                        " FROM user" +
                        " WHERE user_id = ?"
        );
        statement.setLong(1, user_id);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        return resultSet.isBeforeFirst();
    }

    public User getUserByID(long user_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "SELECT user_id, username, create_date, last_login, email_address" +
                        " FROM user" +
                        " WHERE user_id = ?"
        );
        statement.setLong(1, user_id);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        if (!resultSet.isBeforeFirst()) {
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

    public void deleteUser(long user_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM user WHERE user_id = ?"
        );
        statement.setLong(1, user_id);
        statement.execute();
    }
}
