package dev.dragoncave.yap.backend.rest.Controllers;

import dev.dragoncave.yap.backend.DatabaseManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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



}
