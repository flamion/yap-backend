package dev.dragoncave.yap.backend.rest.Controllers;

import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestEntryController {
    private static final EntryController entryController = EntryController.getInstance();
    private static final UserController userController = UserController.getInstance();

    @GetMapping("/entry/{id}")
    public ResponseEntity<?> getEntry(@PathVariable Long id) {
        try {
            if (!entryController.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(entryController.getEntryJson(id), HttpStatus.OK);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/{id}/entries")
    public ResponseEntity<?> getEntries(@PathVariable Long id) {
        try {
            if (!userController.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(entryController.getUserEntries(id), HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/bulk/user/{userID}/entries")
    public ResponseEntity<?> getBulkEntries(@RequestBody List<Long> entryIDs, @PathVariable Long userID) {
        try {
            if (!userController.userExists(userID)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            boolean allValid = true;
            for (var entryID : entryIDs) {
                if (!entryController.entryExists(entryID)) {
                    continue;
                }
                if (!entryController.entryBelongsToUser(userID, entryID)) {
                    allValid = false;
                    break;
                }
            }

            if (!allValid) {
                return new ResponseEntity<>("Entry ID provided which does not belong to the user",HttpStatus.BAD_REQUEST);
            }

            List<Entry> entries = new ArrayList<>();

            for (var entryID : entryIDs) {
                entries.add(entryController.getEntryByID(entryID));
            }

            return new ResponseEntity<>(entries, HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/entry/{id}")
    public ResponseEntity<?> putEntry(@PathVariable Long id, @RequestBody Entry entry) {
        try {
            //prevent manipulation of the id inside the entry object but allow if it absent from the object
            if (id != entry.getEntryID()) {
                entry.setEntryID(id);
            }
            if (entry.isInvalid()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (!entryController.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            entryController.updateEntry(entry);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(
            value = "/entry/",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> createEntry(@RequestBody Entry newEntry) {
        try {
            if (newEntry.isInvalid()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            long newEntryId = entryController.createEntry(newEntry);
            if (newEntryId == -1) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(newEntryId, HttpStatus.CREATED);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/entry/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable Long id) {
        try {
            if (!entryController.entryExists(id)) {
                return new ResponseEntity<>("Entry does not exist", HttpStatus.NO_CONTENT);
            }

            entryController.deleteEntry(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
