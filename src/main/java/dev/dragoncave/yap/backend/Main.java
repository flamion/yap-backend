package dev.dragoncave.yap.backend;

import javax.xml.crypto.Data;

public class Main {

    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    public static void main(String[] args) {
        try {
            System.out.println(DatabaseManager.getInstance().getUserJson(1L));
            System.out.println(DatabaseManager.getInstance().getEntryJson(1L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
