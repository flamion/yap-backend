package dev.dragoncave.yap.backend;

import dev.dragoncave.yap.backend.rest.Controllers.security.PasswordUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    public static void main(String[] args) throws Exception {
        String test = "ABCDEF";
        String test2;
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        System.out.println(Arrays.toString(bytes));
        String formatted = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(formatted);
        System.out.println(Arrays.toString(formatted.getBytes(StandardCharsets.UTF_8)));

        System.out.println("--------------------------------------\n");

        System.out.println(Arrays.toString(bytes));
        String base64 = Base64.getEncoder().encodeToString(bytes);
        System.out.println(base64);
        System.out.println(Arrays.toString(Base64.getDecoder().decode(base64)));

        System.out.println("--------------------------------------\n");

        String password = "Super duper secret password";

        String salt = PasswordUtils.getBase64Salt();
        String hash = PasswordUtils.getHash(password, salt);

        System.out.println(hash);

        System.out.println(PasswordUtils.isExpectedPassword(password, salt, hash));
    }
}