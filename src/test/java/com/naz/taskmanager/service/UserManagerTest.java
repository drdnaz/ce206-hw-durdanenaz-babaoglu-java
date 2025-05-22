import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class UserManagerTest {

    @Test
    public void testRegisterAndRemoveUser() {
        UserManager um = UserManager.getInstance();
        boolean registered = um.registerUser("testuser2", "pass", "mail2");
        assertTrue(registered);
        // Kullanıcı silme işlemi yoksa, sadece tekrar kayıt denenebilir
        assertFalse(um.registerUser("testuser2", "pass", "mail2"));
    }

    @Test
    public void testLoginUserEdgeCases() {
        UserManager um = UserManager.getInstance();
        assertNull(um.loginUser("notfound", "1234"));
        um.registerUser("ali", "1234", "ali@mail.com");
        assertNull(um.loginUser("ali", "wrong"));
    }
} 