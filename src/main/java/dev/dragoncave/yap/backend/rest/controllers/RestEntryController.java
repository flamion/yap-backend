package dev.dragoncave.yap.backend.rest.controllers;

import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/entry")
public class RestEntryController {
	Tokenstore tokenStore = new DatabaseTokenStore();

	@GetMapping("/{entryId}")
	public ResponseEntity<?> getEntry(@RequestHeader(value = "Token") String token, @PathVariable Long entryId) {
		try {
			if (!tokenStore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long ownerId = tokenStore.getUserIdByToken(token);
			if (!EntryController.entryBelongsToUser(ownerId, entryId)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			if (!EntryController.entryExists(entryId)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(EntryController.getEntryJson(entryId), HttpStatus.OK);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping()
	public ResponseEntity<?> putEntry(@RequestHeader(value = "Token") String token, @RequestBody Entry entry) {
		try {
			if (!tokenStore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (entry.isInvalid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long entryId = entry.getEntryID();
			if (!EntryController.entryExists(entryId)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			long ownerId = tokenStore.getUserIdByToken(token);
			if (!EntryController.entryBelongsToUser(ownerId, entryId)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			EntryController.updateEntry(entry);
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
	public ResponseEntity<?> createEntry(@RequestHeader(value = "Token") String token, @RequestBody Entry newEntry) {
		try {
			if (tokenStore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			long ownerId = tokenStore.getUserIdByToken(token);
			newEntry.setCreator(UserController.getUserFromID(ownerId));

			if (newEntry.isInvalid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			long newEntryId = EntryController.createEntry(newEntry);
			if (newEntryId == -1) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			return new ResponseEntity<>(newEntryId, HttpStatus.CREATED);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/{entryId}")
	public ResponseEntity<?> deleteEntry(@RequestHeader(value = "Token") String token, @PathVariable Long entryId) {
		try {
			if (tokenStore.tokenIsValid(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (!EntryController.entryExists(entryId)) {
				return new ResponseEntity<>("Entry does not exist", HttpStatus.NO_CONTENT);
			}

			long ownerId = tokenStore.getUserIdByToken(token);
			if (!EntryController.entryBelongsToUser(ownerId, entryId)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			EntryController.deleteEntry(entryId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}