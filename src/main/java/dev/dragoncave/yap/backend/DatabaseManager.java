package dev.dragoncave.yap.backend;

public class DatabaseManager {

    private final DatabaseManager manager = new DatabaseManager();
    private String DATABASE_URL;


    private DatabaseManager() {

        if (System.getenv("DATABASE_TYPE").equals("SQLITE")) {
            DATABASE_URL = "jdbc:sqlite:database.db";
        }

    }
}
