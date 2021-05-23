package dev.dragoncave.yap.backend.rest.security.controllers;

import dev.dragoncave.yap.backend.databasemanagers.PasswordController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.User;
import dev.dragoncave.yap.backend.rest.security.MailSend;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.TokenUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.Properties;

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

			if (!PasswordUtils.isValidPassword(requestParams.get("newPassword"))) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			tokenstore.invalidateAllUserTokens(user.getUserID());
			UserController.updatePassword(user.getUserID(), requestParams.get("newPassword"));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/resetPasswordRequest")
	public ResponseEntity<?> makeResetPasswordRequest(@RequestBody HashMap<String, String> requestBody) {
		try {
			if (!requestBody.containsKey("emailAddress")) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long userID = UserController.getUserIdFromEmailAddress(requestBody.get("emailAddress"));

			if (userID == -1) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			String resetCode = TokenUtils.generateToken(48);
			PasswordController.insertPasswordResetCode(userID, resetCode);

			MailSend.sendMail(
					"yapreset@gmail.com",
					requestBody.get("emailAddress"),
					"YAP Password reset code",
					"Your Reset code is: " + resetCode +
							"<br><br> You can reset your account password here: https://testseite.dragoncave.dev/reset"
			);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody HashMap<String, String> requestBody) {
		try {
			if (!requestBody.containsKey("resetCode") && !requestBody.containsKey("emailAddress") && !requestBody.containsKey("newPassword")) {
				return new ResponseEntity<>("Missing field", HttpStatus.BAD_REQUEST);
			}

			long userID = UserController.getUserIdFromEmailAddress(requestBody.get("emailAddress"));

			if (!PasswordController.resetCodeIsValid(userID, requestBody.get("resetCode"))) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (!PasswordUtils.isValidPassword(requestBody.get("newPassword"))) {
				return new ResponseEntity<>("Password does not conform to guidelines", HttpStatus.BAD_REQUEST);
			}

			tokenstore.invalidateAllUserTokens(userID);
			PasswordController.removeResetCode(requestBody.get("resetCode"));
			PasswordController.resetUserPassword(userID, requestBody.get("newPassword"));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
