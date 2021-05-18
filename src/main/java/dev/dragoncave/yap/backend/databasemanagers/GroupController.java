package dev.dragoncave.yap.backend.databasemanagers;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;
import dev.dragoncave.yap.backend.rest.objects.Group;
import dev.dragoncave.yap.backend.rest.objects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("SqlResolve")
public class GroupController {
    private GroupController() {

    }


    //Returns the group ID of the created group in the Database or -1 if it failed
    public static long createGroup(String group_name, User creator) throws SQLException {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "INSERT INTO groups (group_name, creator, create_date, last_access_date)" +
                                "VALUES (?, ?, ?, ?)")
        ) {
            statement.setString(1, group_name);
            statement.setLong(2, creator.getUserid());
            statement.setLong(3, System.currentTimeMillis());
            statement.setLong(4, System.currentTimeMillis());
            statement.execute();
            try (ResultSet group_id = statement.getGeneratedKeys()) {
                if (group_id.next()) {
                    return group_id.getLong(1);
                }
            }

        }
        return -1;
    }

    public static void deleteGroup(long group_id) throws SQLException {

        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "DELETE FROM groups WHERE group_id = ?"
                )
        ) {
            statement.setLong(1, group_id);
            statement.execute();
        }
    }

    public static Group getGroupByID(long group_id) throws SQLException {

        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement statement = dbcon.prepareStatement(
                        "SELECT * FROM groups WHERE group_id = ?"
                )
        ) {
            statement.setLong(1, group_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new Group(
                        resultSet.getLong("group_id"),
                        resultSet.getString("group_name"),
                        UserController.getUserByID(resultSet.getLong("creator")),
                        resultSet.getLong("create_date"),
                        resultSet.getLong("last_access_date")
                );
            }
        }
    }

    public static String getGroupJson(long group_id) throws SQLException {
        Gson gson = new Gson();
        Group group = getGroupByID(group_id);

        return gson.toJson(group);
    }
}
