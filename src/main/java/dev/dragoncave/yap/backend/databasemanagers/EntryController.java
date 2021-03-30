package dev.dragoncave.yap.backend.databasemanagers;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.rest.objects.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryController {
    private static final Connection dbcon = ConnectionController.getInstance().getConnection();
    private static final UserController userController = UserController.getInstance();

    private static final EntryController instance = new EntryController();

    private EntryController() {

    }

    public static EntryController getInstance() {
        return instance;
    }

    public Entry getEntryByID(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM entry WHERE entry_id = ?");
        statement.setLong(1, entry_id);
        ResultSet resultSet = statement.executeQuery();

        return new Entry(
                resultSet.getLong("entry_id"),
                userController.getUserFromID(resultSet.getLong("creator")),
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

    public boolean entryExists(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement("SELECT * FROM entry WHERE entry_id = ?");
        statement.setLong(1, entry_id);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.isBeforeFirst();
    }

    //Get all entries from a user
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

    public void deleteEntry(long entry_id) throws SQLException {
        PreparedStatement statement = dbcon.prepareStatement(
                "DELETE FROM entry WHERE entry_id = ?"
        );

        statement.setLong(1, entry_id);
        statement.execute();
    }

    public String getEntryJson(long entry_id) throws SQLException {
        Gson gson = new Gson();
        return gson.toJson(getEntryByID(entry_id));
    }
}
