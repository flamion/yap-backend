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
@RequestMapping("/entry")
public class RestEntryController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getEntry(@PathVariable Long id) {
        try {
            if (!EntryController.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(EntryController.getEntryJson(id), HttpStatus.OK);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putEntry(@PathVariable Long id, @RequestBody Entry entry) {
        try {
            //prevent manipulation of the id inside the entry object but allow if it absent from the object
            if (id != entry.getEntryID()) {
                entry.setEntryID(id);
            }
            if (entry.isInvalid()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (!EntryController.entryExists(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            EntryController.updateEntry(entry);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> createEntry(@RequestBody Entry newEntry) {
        try {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable Long id) {
        try {
            if (!EntryController.entryExists(id)) {
                return new ResponseEntity<>("Entry does not exist", HttpStatus.NO_CONTENT);
            }

            EntryController.deleteEntry(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
