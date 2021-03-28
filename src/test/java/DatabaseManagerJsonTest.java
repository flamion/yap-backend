import dev.dragoncave.yap.backend.DatabaseManager;
import dev.dragoncave.yap.backend.rest.objects.Group;
import dev.dragoncave.yap.backend.rest.objects.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseManagerJsonTest {

    @Test
    @DisplayName("Test User creation and deletion")
    public void testUserIDToJson() throws Exception {
        DatabaseManager instance = DatabaseManager.getInstance();
        long time = System.currentTimeMillis();
        long testUserid = instance.createUser("testuser", "testpassword", time, time, "testuser@test.test");

        assert testUserid != -1L;

        instance.deleteUser(testUserid);

        assert instance.getUserByID(testUserid) == null;
    }

    @Test
    @DisplayName("Entry ID to json test")
    public void testEntryIDToJson() throws Exception {
        assertEquals(
                "{\"entryID\":1,\"creator\":{\"userid\":1,\"username\":\"testuser\",\"createDate\":1612895671000,\"lastLogin\":1612895671000,\"emailAddress\":\"test@test.com\"},\"createDate\":1612650686000,\"dueDate\":1612737086000,\"title\":\"Test Title\",\"description\":\"Test Description\"}",
                DatabaseManager.getInstance().getEntryJson(1L)
        );
    }

    @Test
    @DisplayName("Test Group creation and deletion")
    public void testGroupCreation() throws Exception {
        DatabaseManager instance = DatabaseManager.getInstance();
        long time = System.currentTimeMillis();
        long testUserid = instance.createUser("testuser", "testpassword", time, time, "testuser@test.test");
        long testGroupid = instance.createGroup(
                "Test Group",
                instance.getUserByID(testUserid)
        );

        assert testUserid != -1L && testGroupid != -1L;

        Group testGroup = instance.getGroupByID(testGroupid);
        User testUser = instance.getUserByID(testUserid);

        assert testGroup != null;
        assert testUser != null;

        instance.deleteGroup(testGroupid);

        assert instance.getGroupByID(testGroupid) == null;
    }
}
