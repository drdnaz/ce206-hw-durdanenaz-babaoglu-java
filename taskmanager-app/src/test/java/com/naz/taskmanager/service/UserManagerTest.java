package com.naz.taskmanager.service;

import com.naz.taskmanager.model.User;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;

public class UserManagerTest {
    @Before
    public void setUp() {
        // Test öncesi kullanıcı dosyasını temizle
        File file = new File("data/users.dat");
        if (file.exists()) file.delete();
        // Singleton instance sıfırlanmalı (gerekirse reflection ile)
        try {
            java.lang.reflect.Field instanceField = UserManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception ignored) {}
    }

    @Test
    public void testSingletonInstance() {
        UserManager um1 = UserManager.getInstance();
        UserManager um2 = UserManager.getInstance();
        assertSame(um1, um2);
    }

    @Test
    public void testRegisterAndLoginUser() {
        UserManager um = UserManager.getInstance();
        boolean registered = um.registerUser("testuser", "1234", "test@mail.com");
        assertTrue(registered);
        User user = um.loginUser("testuser", "1234");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("1234", user.getPassword());
        assertEquals("test@mail.com", user.getEmail());
    }

    @Test
    public void testRegisterDuplicateUser() {
        UserManager um = UserManager.getInstance();
        assertTrue(um.registerUser("testuser", "1234", "test@mail.com"));
        assertFalse(um.registerUser("testuser", "abcd", "other@mail.com"));
    }

    @Test
    public void testLoginWithWrongPassword() {
        UserManager um = UserManager.getInstance();
        um.registerUser("testuser", "1234", "test@mail.com");
        assertNull(um.loginUser("testuser", "wrong"));
    }

    @Test
    public void testLoginNonexistentUser() {
        UserManager um = UserManager.getInstance();
        assertNull(um.loginUser("nouser", "nopass"));
    }
} 