package dev.dragoncave.yap.backend.rest.Controllers.bulk;

import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.databasemanagers.UserController;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bulk/user")
public class BulkUserController {

    @GetMapping("/{userID}/entries")
    public ResponseEntity<?> getBulkEntries(@RequestBody List<Long> entryIDs, @PathVariable Long userID) {
        try {
            if (!UserController.userExists(userID)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            boolean allValid = true;
            for (var entryID : entryIDs) {
                if (!EntryController.entryExists(entryID)) {
                    continue;
                }
                if (!EntryController.entryBelongsToUser(userID, entryID)) {
                    allValid = false;
                    break;
                }
            }

            if (!allValid) {
                return new ResponseEntity<>("Entry ID provided which does not belong to the user", HttpStatus.BAD_REQUEST);
            }

            List<Entry> entries = new ArrayList<>();

            for (var entryID : entryIDs) {
                entries.add(EntryController.getEntryByID(entryID));
            }

            return new ResponseEntity<>(entries, HttpStatus.OK);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
