package dev.dragoncave.yap.backend;

import com.google.gson.Gson;

import java.sql.*;

public class DatabaseManager {

    private final static DatabaseManager manager = new DatabaseManager();
    private static Connection dbcon;
    private String DATABASE_URL;


    private DatabaseManager() {
        if (System.getenv("DATABASE_TYPE").equals("SQLITE")) {
            DATABASE_URL = "jdbc:sqlite:database.db";
        }

        assert DATABASE_URL != null;

        try {
            dbcon = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        return manager;
    }

    private User getUserFromID(long userid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM user WHERE user_id = ?");
        statement.setLong(1, userid);
        ResultSet result = statement.executeQuery();

        return new User(
                result.getInt("user_id"),
                result.getString("username"),
                new Date(result.getLong("create_date") * 1000L),
                new Date(result.getLong("last_login") * 1000L),
                result.getString("email_address")
        );
    }

    private Group getGroupFromID(long groupid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM groups WHERE group_id = ?");
        statement.setLong(1, groupid);
        ResultSet result = statement.executeQuery();

        return new Group(
                result.getLong("group_id"),
                result.getString("group_name"),
                getUserFromID(result.getLong("creator")),
                result.getLong("create_date") * 1000L,
                result.getLong("last_access_date") * 1000L
        );
    }

    private Entry getEntryByID(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM entry WHERE entry_id = ?");
        statement.setLong(1, entry_id);
        ResultSet result = statement.executeQuery();

        return new Entry(
                result.getLong("entry_id"),
                getUserFromID(result.getLong("creator")),
                result.getLong("create_date") * 1000,
                result.getLong("due_date") * 1000,
                result.getString("title"),
                result.getString("description")
        );
    }

    public String getUserJson(long userid) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getUserFromID(userid));
    }

    public String getGroupJson(long groupid) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getGroupFromID(groupid));
    }

    public String getEntryJson(long entryid) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getEntryByID(entryid));
    }
}