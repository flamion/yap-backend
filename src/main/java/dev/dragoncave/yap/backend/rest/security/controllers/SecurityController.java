package dev.dragoncave.yap.backend.rest.security.controllers;

import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.User;
import dev.dragoncave.yap.backend.rest.security.PasswordUtils;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
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

			if (PasswordUtils.isValidPassword(requestParams.get("newPassword"))) {
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

//	@PostMapping("/resetPassword")
//	public ResponseEntity<?> resetPassword(@RequestBody HashMap<String, String> requestBody) {
//		try {
//			Properties prop = new Properties();
//			prop.put("mail.smtp.auth", true);
//			prop.put("mail.smtp.starttls.enable", "true");
//			prop.put("mail.smtp.host", "smtp.mailtrap.io");
//			prop.put("mail.smtp.port", "25");
//			prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
//
//			Session session = Session.getInstance(prop, new Authenticator() {
//				@Override
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(username, password);
//				}
//			});
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("from@gmail.com"));
//			message.setRecipients(
//					Message.RecipientType.TO, InternetAddress.parse("to@gmail.com"));
//			message.setSubject("Mail Subject");
//
//			String msg = "This is my first email using JavaMailer";
//
//			MimeBodyPart mimeBodyPart = new MimeBodyPart();
//			mimeBodyPart.setContent(msg, "text/html");
//
//			Multipart multipart = new MimeMultipart();
//			multipart.addBodyPart(mimeBodyPart);
//
//			message.setContent(multipart);
//
//			Transport.send(message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}
