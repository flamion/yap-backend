package dev.dragoncave.yap.backend.rest.controllers;

import dev.dragoncave.yap.backend.databasemanagers.BoardController;
import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.Board;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("/boards")
public class RestBoardController {
	private final Tokenstore tokenstore = new DatabaseTokenStore();

	@GetMapping("/user")
	public ResponseEntity<?> getUserBoards(@RequestHeader(value = "Token") String token) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long userID = tokenstore.getUserIdByToken(token);
			return new ResponseEntity<>(BoardController.getUserBoards(userID), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/{boardID}/entries")
	public ResponseEntity<?> getBoardEntries(@PathVariable Long boardID, @RequestHeader(value = "Token") String token) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long userID = tokenstore.getUserIdByToken(token);

			if (!BoardController.userIsBoardMember(userID, boardID)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			return new ResponseEntity<>(EntryController.getBoardEntries(boardID), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/{boardID}/entry")
	public ResponseEntity<?> createNewEntry(@PathVariable Long boardID, @RequestHeader(value = "Token") String token, @RequestBody Entry newEntry) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long userID = tokenstore.getUserIdByToken(token);
			newEntry.setCreator(UserController.getUserByID(userID));

			if (newEntry.isInvalid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			if (!BoardController.userIsBoardMember(userID, boardID)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			long newEntryId = EntryController.createEntry(newEntry, boardID);
			if (newEntryId == -1) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			return new ResponseEntity<>(newEntryId, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/{boardID}")
	public ResponseEntity<?> getBoard(@RequestHeader(value = "Token") String token, @PathVariable Long boardID) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (!BoardController.boardExists(boardID)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			long userID = tokenstore.getUserIdByToken(token);
			if (!BoardController.userIsBoardMember(userID, boardID)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			return new ResponseEntity<>(BoardController.getBoardByID(boardID), HttpStatus.OK);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping()
	public ResponseEntity<?> createBoard(@RequestHeader(value = "Token") String token, @RequestBody Board newBoard) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long userID = tokenstore.getUserIdByToken(token);
			newBoard.setCreator(UserController.getUserByID(userID));

			if (newBoard.boardIsInvalid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long newBoardID = BoardController.createNewBoard(userID, newBoard);
			return new ResponseEntity<>(newBoardID, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/{boardID}/member")
	public ResponseEntity<?> addMember(@PathVariable Long boardID, @RequestHeader(value = "Token") String token, @RequestBody HashMap<String, String> requestBody) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (!requestBody.containsKey("emailAddress")) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long userID = tokenstore.getUserIdByToken(token);
			if (!BoardController.userIsBoardAdmin(userID, boardID)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			long newMemberID = UserController.getUserIdFromEmailAddress(requestBody.get("emailAddress"));
			BoardController.addMemberToBoard(newMemberID, boardID);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/{boardID}/admin")
	public ResponseEntity<?> addAdmin(@PathVariable Long boardID, @RequestHeader(value = "Token") String token, @RequestBody HashMap<String, String> requestBody) {
		try {
			if (!tokenstore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (!requestBody.containsKey("emailAddress")) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long userID = tokenstore.getUserIdByToken(token);
			if (!BoardController.userIsBoardAdmin(userID, boardID)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			long newMemberID = UserController.getUserIdFromEmailAddress(requestBody.get("emailAddress"));
			if (!BoardController.userIsBoardMember(newMemberID, boardID)) {
				BoardController.addMemberToBoard(userID, boardID);
			}

			BoardController.addAdminToBoard(newMemberID, boardID);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
