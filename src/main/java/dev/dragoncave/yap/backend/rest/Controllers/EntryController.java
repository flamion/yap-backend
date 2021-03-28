package dev.dragoncave.yap.backend.rest.Controllers;

import dev.dragoncave.yap.backend.DatabaseManager;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class EntryController {
    private static final DatabaseManager dbmanager = DatabaseManager.getInstance();

    @GetMapping("/entry/{id}")
    public ResponseEntity<?> getEntry(@PathVariable Long id) {
        try {
            if (!dbmanager.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dbmanager.getEntryJson(id), HttpStatus.OK);
        } catch (SQLException ex ) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/{id}/entries/")
    public ResponseEntity<?> getEntries(@PathVariable Long id) {
        try {
            if (!dbmanager.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(dbmanager.getUserEntries(id), HttpStatus.OK);
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

            long newEntryId = dbmanager.createEntry(newEntry);
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
            if (!dbmanager.entryExists(id)) {
                return new ResponseEntity<>("Entry does not exist", HttpStatus.NO_CONTENT);
            }

            dbmanager.deleteEntry(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
