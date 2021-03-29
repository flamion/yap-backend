package dev.dragoncave.yap.backend.rest.Controllers;

import dev.dragoncave.yap.backend.DatabaseManager;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class RestEntryController {
    private static final DatabaseManager dbManager = DatabaseManager.getInstance();

    @GetMapping("/entry/{id}")
    public ResponseEntity<?> getEntry(@PathVariable Long id) {
        try {
            if (!dbManager.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dbManager.getEntryJson(id), HttpStatus.OK);
        } catch (SQLException ex ) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/{id}/entries/")
    public ResponseEntity<?> getEntries(@PathVariable Long id) {
        try {
            if (!dbManager.userExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(dbManager.getUserEntries(id), HttpStatus.OK);
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
            if (!dbManager.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            dbManager.updateEntry(entry);
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

            long newEntryId = dbManager.createEntry(newEntry);
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
            if (!dbManager.entryExists(id)) {
                return new ResponseEntity<>("Entry does not exist", HttpStatus.NO_CONTENT);
            }

            dbManager.deleteEntry(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
