package dev.dragoncave.yap.backend.rest.security.tokens;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTokenStore implements Tokenstore {

    private static final long VALID_DURATION = 30 * 1000; //how long the token is valid in milliseconds

    @Override
    public List<String> getTokensByUserId(long userId) {
        List<String> tokens = new ArrayList<>();

        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement getTokensStatement = dbcon.prepareStatement(
                        "SELECT * FROM tokens WHERE user_id = ?"
                )
        ) {
            getTokensStatement.setLong(1, userId);

            try (ResultSet tokenInformationResultSet = getTokensStatement.executeQuery()) {
                while (tokenInformationResultSet.next()) {
                    tokens.add(tokenInformationResultSet.getString("token"));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return tokens;
    }

    @Override
    public List<String> getTokensByEmail(String emailAddress) {
        List<String> tokens = new ArrayList<>();

        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement getTokensStatement = dbcon.prepareStatement(
                        "SELECT * FROM tokens WHERE user_id = (SELECT user_id FROM users WHERE email_address = ?)"
                )
        ) {
            getTokensStatement.setString(1, emailAddress);

            try (ResultSet tokenInformationResultSet = getTokensStatement.executeQuery()) {
                while (tokenInformationResultSet.next()) {
                    tokens.add(tokenInformationResultSet.getString("token"));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return tokens;
    }

    @Override
    public String createToken(long userId) {
        try (
                Connection dbcon = ConnectionController.getConnection();
                PreparedStatement insertNewTokenStatement = dbcon.prepareStatement(
                        "INSERT INTO tokens VALUES (?, ?, ?)"
                )
        ) {
            String newToken = TokenUtils.generateToken();

            insertNewTokenStatement.setLong(1, userId);
            insertNewTokenStatement.setString(2, newToken);
            insertNewTokenStatement.setLong(3, System.currentTimeMillis() + VALID_DURATION);
            return newToken;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public void invalidateToken(String token) {

    }

    @Override
    public void invalidateAllUserTokens(long userId) {

    }
}