package dev.dragoncave.yap.backend;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import dev.dragoncave.yap.backend.rest.objects.Group;
import dev.dragoncave.yap.backend.rest.objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final static DatabaseManager manager = new DatabaseManager();
    private static Connection dbcon;
    private String DATABASE_URL = "jdbc:sqlite:database.db";

    private DatabaseManager() {
//        if (System.getenv("DATABASE_TYPE").equals("SQLITE")) {
//            DATABASE_URL = "jdbc:sqlite:database.db";
//        }

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
                result.getLong("create_date"),
                result.getLong("last_login"),
                result.getString("email_address")
        );
    }

    public boolean entryExists(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM entry WHERE entry_id = ?");
        statement.setLong(1, entry_id);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.isBeforeFirst();
    }

    public List<Long> getUserEntries(long user_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM entry WHERE creator = ?");
        statement.setLong(1, user_id);
        ResultSet resultSet = statement.executeQuery();
        List<Long> entryIds = new ArrayList<>();

        while (resultSet.next()) {
            entryIds.add(resultSet.getLong("entry_id"));
        }

        return entryIds;
    }

    public Entry getEntryByID(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM entry WHERE entry_id = ?");
        statement.setLong(1, entry_id);
        ResultSet resultSet = statement.executeQuery();

        return new Entry(
                resultSet.getLong("entry_id"),
                getUserFromID(resultSet.getLong("creator")),
                resultSet.getLong("create_date"),
                resultSet.getLong("due_date"),
                resultSet.getString("title"),
                resultSet.getString("description")
        );
    }

    public long createEntry(long creatorId, long dueDate, String title, String description) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "INSERT INTO entry (creator, create_date, due_date, title, description)" +
                        "VALUES (?, ?, ?, ?, ?)"
        );
        statement.setLong(1, creatorId);
        statement.setLong(2, System.currentTimeMillis());
        statement.setLong(3, dueDate);
        statement.setString(4, title);
        statement.setString(5, description);

        statement.execute();

        ResultSet entryId = statement.getGeneratedKeys();
        if (entryId.isBeforeFirst()) {
            return entryId.getLong(1);
        }
        return -1;
    }

    public long createEntry(Entry newEntry) throws SQLException {
        return createEntry(newEntry.getCreator().getUserid(), newEntry.getDueDate(), newEntry.getTitle(), newEntry.getDescription());
    }

    public String getUserJson(long userid) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getUserFromID(userid));
    }

    public String getEntryJson(long entryid) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getEntryByID(entryid));
    }

    public String getGroupJson(long groupid) throws SQLException, AssertionError {
        Gson gson = new Gson();
        Group group = getGroupByID(groupid);

        assert group != null;

        return gson.toJson(group);
    }

    //Returns the group ID of the created group in the Database or -1 if it failed
    public long createGroup(String groupName, User creator) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "INSERT INTO groups (group_name, creator, create_date, last_access_date)" +
                        "VALUES (?, ?, ?, ?)");
        statement.setString(1, groupName);
        statement.setLong(2, creator.getUserid());
        statement.setLong(3, System.currentTimeMillis());
        statement.setLong(4, System.currentTimeMillis());
        statement.execute();
        ResultSet groupid = statement.getGeneratedKeys();
        if (groupid.isBeforeFirst()) {
            return groupid.getLong(1);
        }
        return -1;
    }

    public void deleteGroup(long groupid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM groups WHERE group_id = ?"
        );
        statement.setLong(1, groupid);
        statement.execute();
    }

    public Group getGroupByID(long groupid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "SELECT * FROM groups WHERE group_id = ?"
        );
        statement.setLong(1, groupid);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.isBeforeFirst()) {
            return null;
        }
        return new Group(
                resultSet.getLong("group_id"),
                resultSet.getString("group_name"),
                getUserFromID(resultSet.getLong("creator")),
                resultSet.getLong("create_date"),
                resultSet.getLong("last_access_date")
        );
    }

    //returns the ID of the just created user or -1 if something went wrong
    public long createUser(String username, String password, long createDate, long lastLogin, String emailAddress) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "INSERT INTO user (username, password, create_date, last_login, email_address)" +
                        "VALUES (?, ?, ?, ?, ?)"
        );
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setLong(3, createDate);
        statement.setLong(4, lastLogin);
        statement.setString(5, emailAddress);
        statement.execute();
        ResultSet groupid = statement.getGeneratedKeys();
        if (groupid.isBeforeFirst()) {
            return groupid.getLong(1);
        }
        return -1;
    }

    public long createUser(User newUser) throws SQLException {
        return createUser(newUser.getUsername(), newUser.getPassword(), System.currentTimeMillis(), System.currentTimeMillis(), newUser.getEmailAddress());
    }

    public boolean userExists(long userid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "SELECT user_id, username, create_date, last_login, email_address" +
                        " FROM user" +
                        " WHERE user_id = ?"
        );
        statement.setLong(1, userid);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        return resultSet.isBeforeFirst();
    }

    public User getUserByID(long userid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "SELECT user_id, username, create_date, last_login, email_address" +
                        " FROM user" +
                        " WHERE user_id = ?"
        );
        statement.setLong(1, userid);
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

    public void deleteEntry(long entryID) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM entry WHERE entry_id = ?"
        );

        statement.setLong(1, entryID);
        statement.execute();
    }

    public void deleteUser(long userid) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM user WHERE user_id = ?"
        );
        statement.setLong(1, userid);
        statement.execute();
    }
}