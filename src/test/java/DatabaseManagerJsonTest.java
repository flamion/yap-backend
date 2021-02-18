import dev.dragoncave.yap.backend.DatabaseManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseManagerJsonTest {

    @Test
    @DisplayName("User ID to json test")
    public void testUserIDToJson() throws Exception {
        assertEquals("{\"userid\":1,\"username\":\"testuser\",\"createDate\":1612895671000,\"lastLogin\":1612895671000,\"emailAddress\":\"test@test.com\"}",
                DatabaseManager.getInstance().getUserJson(1L)
        );
    }

    @Test
    @DisplayName("Entry ID to json test")
    public void testEntryIDToJson() throws Exception {
        assertEquals(
                "{\"entryID\":1,\"creator\":{\"userid\":1,\"username\":\"testuser\",\"createDate\":1612895671000,\"lastLogin\":1612895671000,\"emailAddress\":\"test@test.com\"},\"createDate\":1612650686000,\"dueDate\":1612737086000,\"title\":\"Test Title\",\"description\":\"Test Description\"}",
                DatabaseManager.getInstance().getEntryJson(1L)
        );
    }
}
