package dev.dragoncave.yap.backend.rest.security.controllers;

import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.User;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController()
@RequestMapping("/security")
public class SecurityController {
	private final Tokenstore tokenstore = new DatabaseTokenStore();

	@PutMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody HashMap<String, String> requestParams) {
		try {
			if (!(requestParams.containsKey("emailAddress") && requestParams.containsKey("newPassword") && requestParams.containsKey("oldPassword"))) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			User user = UserController.getUserFromEmailAddress(requestParams.get("emailAddress"));
			if (user == null) {
				return new ResponseEntity<>("Incorrect email address provided", HttpStatus.BAD_REQUEST);
			}

			if (!UserController.passwordMatches(user.getUserID(), requestParams.get("oldPassword"))) {
				return new ResponseEntity<>("Incorrect email address or password provided", HttpStatus.FORBIDDEN);
			}

			tokenstore.invalidateAllUserTokens(user.getUserID());
			UserController.updatePassword(user.getUserID(), requestParams.get("newPassword")); //TODO add password constraint check
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
