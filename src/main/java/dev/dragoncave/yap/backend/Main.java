package dev.dragoncave.yap.backend;

import com.google.gson.Gson;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.security.tokens.TokenUtils;

import java.util.HashMap;

public class Main {

    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    public static void main(String[] args) throws Exception {
        System.out.println(TokenUtils.generateToken());
        System.out.println(UserController.getUserIdFromEmailAddress("Unixcorn@i-use-arch.com"));
        System.out.println(UserController.getUserIdFromEmailAddress("asfdsafblasdfhba"));
    }
}