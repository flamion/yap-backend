package dev.dragoncave.yap.backend.rest.security.tokens;

import dev.dragoncave.yap.backend.databasemanagers.connections.ConnectionController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTokenStore implements Tokenstore {
	//Days, hours, minutes, seconds, milliseconds
	private static final long VALID_DURATION = 14 * 24 * 60 * 60 * 1000; //how long the token is valid in milliseconds

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
						"INSERT INTO tokens (user_id, token, valid_until) VALUES (?, ?, ?)"
				)
		) {
			String newToken = TokenUtils.generateToken();

			insertNewTokenStatement.setLong(1, userId);
			insertNewTokenStatement.setString(2, newToken);
			insertNewTokenStatement.setLong(3, (System.currentTimeMillis() + VALID_DURATION));

			insertNewTokenStatement.execute();
			return newToken;
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	@Override
	public void invalidateToken(String token) {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement deleteToken = dbcon.prepareStatement(
						"DELETE FROM tokens WHERE token = ?"
				)
		) {
			deleteToken.setString(1, token);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void invalidateAllUserTokens(long userId) {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement deleteAllUserTokens = dbcon.prepareStatement(
						"DELETE FROM tokens WHERE user_id = ?"
				)
		) {
			deleteAllUserTokens.setLong(1, userId);
			deleteAllUserTokens.execute();

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public boolean tokenIsValid(String token) {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getTokenStatement = dbcon.prepareStatement(
						"SELECT * FROM tokens WHERE token = ?"
				)
		) {
			getTokenStatement.setString(1, token);
			try (ResultSet tokenResultSet = getTokenStatement.executeQuery()) {
				if (tokenResultSet.next()) {
					long validUntil = tokenResultSet.getLong("valid_until");
					if (System.currentTimeMillis() < validUntil) {
						return true;
					}
				}
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return false;
	}

	@Override
	public long getUserIdByToken(String token) {
		try (
				Connection dbcon = ConnectionController.getConnection();
				PreparedStatement getUserIdByTokenStatement = dbcon.prepareStatement(
						"SELECT user_id FROM tokens WHERE token = ?"
				)
		) {
			getUserIdByTokenStatement.setString(1, token);
			try (ResultSet userIdResultSet = getUserIdByTokenStatement.executeQuery()) {
				if (userIdResultSet.next()) {
					return userIdResultSet.getLong("user_id");
				}
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return -1;
	}
}
