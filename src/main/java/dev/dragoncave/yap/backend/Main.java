package dev.dragoncave.yap.backend;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws Exception {
        String input = "a";
        byte[] salt = Base64.getDecoder().decode("Ko0qAD/ZTFF/h9QWo8mw7w==");


        MessageDigest digest = MessageDigest.getInstance("SHA3-512");
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        digest.update(salt);
        byte[] encodedHash = digest.digest();
        System.out.println(bytesToHex(encodedHash));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}