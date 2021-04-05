package dev.dragoncave.yap.backend;

import com.google.gson.Gson;

import java.util.HashMap;

public class Main {

    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    public static void main(String[] args) throws Exception {
        HashMap<String, String> testMap = new HashMap<>();

        testMap.put("test1", "test2");
        testMap.put("test3", "test4");
        testMap.put("test5", "test6");

        System.out.println(new Gson().toJson(testMap));
    }
}