package dev.dragoncave.yap.backend.databasemanagers.connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionController {
	//private static final String DATABASE_URL = "jdbc:sqlite:database.db";
	private static final String DATABASE_URL = "jdbc:postgresql:testseite";
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
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}