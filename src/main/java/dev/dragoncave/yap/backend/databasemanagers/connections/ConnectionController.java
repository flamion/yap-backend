package dev.dragoncave.yap.backend.databasemanagers.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {
    private final static ConnectionController instance = new ConnectionController();
    //private static final String DATABASE_URL = "jdbc:sqlite:database.db";
    private static final String DATABASE_URL = "jdbc:postgresql:testseite";
    private static Connection dbcon;


    private ConnectionController() {
        try {
            System.out.println(DATABASE_URL);
            dbcon = DriverManager.getConnection(DATABASE_URL, System.getenv("DB_USERNAME"), System.getenv("DB_PASS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert dbcon != null;
    }

    public static ConnectionController getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return dbcon;
    }
}
