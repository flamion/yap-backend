package dev.dragoncave.yap.backend;

import com.google.gson.Gson;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class Main {

    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    public static void main(String[] args) {
        var gson = new Gson();
        try (var con = DriverManager.getConnection(DATABASE_URL)) {

            PreparedStatement statement = con.prepareStatement("SELECT * FROM user");
            var resultSet = statement.executeQuery();
            System.out.println("Size: " + resultSet.getFetchSize());

            while (resultSet.next()) {
                System.out.println("user_id: " + resultSet.getInt("user_id") + "  username: " + resultSet.getString("username") + "   createDate: " + resultSet.getString("create_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
