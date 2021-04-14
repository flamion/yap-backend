package dev.dragoncave.yap.backend.rest.controllers.bulk;

import dev.dragoncave.yap.backend.databasemanagers.EntryController;
import dev.dragoncave.yap.backend.rest.objects.Entry;
import dev.dragoncave.yap.backend.rest.security.tokens.DatabaseTokenStore;
import dev.dragoncave.yap.backend.rest.security.tokens.Tokenstore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bulk/user")
public class BulkUserController {
    Tokenstore tokenstore = new DatabaseTokenStore();

    @GetMapping("/entries")
    public ResponseEntity<?> getBulkEntries(@RequestHeader(value = "Token") String token, @RequestBody List<Long> entryIDs) {
        try {
            if (!tokenstore.tokenIsValid(token)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            long ownerId = tokenstore.getUserIdByToken(token);
            for (int i = 0; i < entryIDs.size(); i++) {
                long entryId = entryIDs.get(i);
                if (!EntryController.entryExists(entryId)) {
                    entryIDs.remove(i);
                }
                if (!EntryController.entryBelongsToUser(ownerId, entryId)) {
                    entryIDs.remove(i);
                }
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