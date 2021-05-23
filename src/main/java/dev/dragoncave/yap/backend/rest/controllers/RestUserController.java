package dev.dragoncave.yap.backend.rest.controllers;

import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.FileStore;
import dev.dragoncave.yap.backend.rest.objects.User;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class RestUserController {
	Tokenstore tokenstore = new DatabaseTokenStore();

	@GetMapping()
	public ResponseEntity<?> getUser(@RequestHeader(value = "Token") String token) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long userId = tokenstore.getUserIdByToken(token);
			return new ResponseEntity<>(UserController.getUserJson(userId), HttpStatus.OK);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/entries")
	public ResponseEntity<?> getEntries(@RequestHeader(value = "Token") String token) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long userId = tokenstore.getUserIdByToken(token);
			return new ResponseEntity<>(EntryController.getUserEntries(userId), HttpStatus.OK);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/{userID}")
	public ResponseEntity<?> putEntry(@RequestHeader(value = "Token") String token, @PathVariable Long userID, @RequestBody User user) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (tokenstore.getUserIdByToken(token) != userID) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			user.setUserID(userID);

			if (user.isInvalid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			UserController.updateUser(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<?> newUser(@RequestBody User newUser) {
		try {
			if (newUser.isInvalid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			if (UserController.getUserIdFromEmailAddress(newUser.getEmailAddress()) != -1) {
				return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
			}

			if (!PasswordUtils.isValidPassword(newUser.getPassword())) {
				return new ResponseEntity<>("Invalid Password supplied", HttpStatus.BAD_REQUEST);
			}

			long newUserId = UserController.createUser(newUser);
			return new ResponseEntity<>(newUserId, HttpStatus.CREATED);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/profilePicture")
	public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile, @RequestHeader(value = "Token") String token) {
		try {
			long userID = tokenstore.getUserIdByToken(token);
			FileStore.storeProfilePicture(multipartFile, userID);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IllegalArgumentException illegalArgumentException) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping()
	public ResponseEntity<?> deleteUser(@RequestHeader(value = "Token") String token, @RequestBody HashMap<String, String> requestBody) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (!requestBody.containsKey("password")) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long userId = tokenstore.getUserIdByToken(token);

			if (!UserController.passwordMatches(userId, requestBody.get("password"))) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			tokenstore.invalidateAllUserTokens(userId);
			UserController.deleteUser(userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (SQLException | NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}