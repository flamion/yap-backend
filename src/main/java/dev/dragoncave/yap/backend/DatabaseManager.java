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
//
//        assert DATABASE_URL != null;

        try {
            dbcon = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        return manager;
    }

    private User getUserFromID(long user_id) throws SQLException {
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

    public long createEntry(long creator_id, long due_date, String title, String description) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "INSERT INTO entry (creator, create_date, due_date, title, description)" +
                        "VALUES (?, ?, ?, ?, ?)"
        );
        statement.setLong(1, creator_id);
        statement.setLong(2, System.currentTimeMillis());
        statement.setLong(3, due_date);
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

    public void updateEntry(Entry entry) throws SQLException {
        Entry oldEntry = getEntryByID(entry.getEntryID());
        long due_date = entry.getDueDate();
        String title = entry.getTitle();
        String description = entry.getDescription();

        if (entry.getDueDate() == -1) {
            due_date = oldEntry.getDueDate();
        }

        if (title == null) {
            title = oldEntry.getTitle();
        }

        if (description == null) {
            description = oldEntry.getDescription();
        }
        
        PreparedStatement statement = dbcon.prepareStatement(
                "UPDATE entry " +
                        "SET due_date = ?, title = ?, description = ?" +
                        "WHERE entry_id = ?"
        );

        statement.setLong(1, due_date);
        statement.setString(2, title);
        statement.setString(3, description);
        statement.setLong(4, entry.getEntryID());

        statement.execute();
    }

    public String getUserJson(long user_id) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getUserFromID(user_id));
    }

    public String getEntryJson(long entry_id) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getEntryByID(entry_id));
    }

    public String getGroupJson(long group_id) throws SQLException, AssertionError {
        Gson gson = new Gson();
        Group group = getGroupByID(group_id);

        assert group != null;

        return gson.toJson(group);
    }

    //Returns the group ID of the created group in the Database or -1 if it failed
    public long createGroup(String group_name, User creator) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "INSERT INTO groups (group_name, creator, create_date, last_access_date)" +
                        "VALUES (?, ?, ?, ?)");
        statement.setString(1, group_name);
        statement.setLong(2, creator.getUserid());
        statement.setLong(3, System.currentTimeMillis());
        statement.setLong(4, System.currentTimeMillis());
        statement.execute();
        ResultSet group_id = statement.getGeneratedKeys();
        if (group_id.isBeforeFirst()) {
            return group_id.getLong(1);
        }
        return -1;
    }

    public void deleteGroup(long group_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM groups WHERE group_id = ?"
        );
        statement.setLong(1, group_id);
        statement.execute();
    }

    public Group getGroupByID(long group_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "SELECT * FROM groups WHERE group_id = ?"
        );
        statement.setLong(1, group_id);
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

    public void deleteEntry(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM entry WHERE entry_id = ?"
        );

        statement.setLong(1, entry_id);
        statement.execute();
    }

    public void deleteUser(long user_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM user WHERE user_id = ?"
        );
        statement.setLong(1, user_id);
        statement.execute();
    }
}