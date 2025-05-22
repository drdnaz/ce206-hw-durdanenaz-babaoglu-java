import com.naz.taskmanager.model.User;
import com.naz.taskmanager.service.UserService;
import org.junit.*;
import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService service;

    @Before
    public void setUp() {
        service = new UserService();
    }

    @Test
    public void testRegisterAndLoginUser() {
        boolean registered = service.registerUser("ali", "1234", "ali@mail.com");
        assertTrue(registered);
        User user = service.loginUser("ali", "1234");
        assertNotNull(user);
        assertEquals("ali", user.getUsername());
    }

    @Test
    public void testRegisterDuplicateUser() {
        assertTrue(service.registerUser("ali", "1234", "ali@mail.com"));
        assertFalse(service.registerUser("ali", "abcd", "other@mail.com"));
    }

    @Test
    public void testLoginWithWrongPassword() {
        service.registerUser("ali", "1234", "ali@mail.com");
        assertNull(service.loginUser("ali", "wrong"));
    }

    @Test
    public void testLoginNonexistentUser() {
        assertNull(service.loginUser("nouser", "nopass"));
    }
} 