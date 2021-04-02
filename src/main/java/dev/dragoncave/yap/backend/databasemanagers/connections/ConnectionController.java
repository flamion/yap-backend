package dev.dragoncave.yap.backend.databasemanagers.connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {
    private final static ConnectionController instance = new ConnectionController();
    //private static final String DATABASE_URL = "jdbc:sqlite:database.db";
    private static final String DATABASE_URL = "jdbc:postgresql:testseite";
    private static Connection dbcon;
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DATABASE_URL);
        config.setUsername(System.getenv("DB_USERNAME"));
        config.setPassword(System.getenv("DB_PASS"));
        config.setPoolName("Postgres DB Pool for yap-backend");
        config.setMaximumPoolSize(15);
        dataSource = new HikariDataSource(config);
    }

    private ConnectionController() {
        try {
            System.out.println(System.getenv("DB_USERNAME") + " " + System.getenv("DB_PASS"));
            dbcon = DriverManager.getConnection(DATABASE_URL, System.getenv("DB_USERNAME"), System.getenv("DB_PASS"));
            //dbcon = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert dbcon != null;


    }

    public static ConnectionController getInstance() {
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
