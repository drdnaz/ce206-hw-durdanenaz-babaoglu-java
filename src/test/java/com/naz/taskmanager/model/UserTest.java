import com.naz.taskmanager.model.User;
import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testConstructorAndGetters() {
        User user = new User("ali", "1234", "ali@mail.com");
        assertEquals("ali", user.getUsername());
        assertEquals("1234", user.getPassword());
        assertEquals("ali@mail.com", user.getEmail());
    }

    @Test
    public void testSetters() {
        User user = new User("ali", "1234", "ali@mail.com");
        user.setUsername("veli");
        user.setPassword("abcd");
        user.setEmail("veli@mail.com");
        assertEquals("veli", user.getUsername());
        assertEquals("abcd", user.getPassword());
        assertEquals("veli@mail.com", user.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        User u1 = new User("ali", "1234", "ali@mail.com");
        User u2 = new User("ali", "abcd", "other@mail.com");
        User u3 = new User("veli", "1234", "ali@mail.com");
        assertTrue(u1.equals(u2));
        assertEquals(u1.hashCode(), u2.hashCode());
        assertFalse(u1.equals(u3));
        assertNotEquals(u1.hashCode(), u3.hashCode());
        assertFalse(u1.equals(null));
        assertFalse(u1.equals("ali"));
    }

    @Test
    public void testEqualsWithNullAndOtherType() {
        User user = new User("ali", "1234", "ali@mail.com");
        assertFalse(user.equals(null));
        assertFalse(user.equals("ali"));
    }
} 