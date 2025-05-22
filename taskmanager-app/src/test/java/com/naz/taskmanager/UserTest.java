package com.naz.taskmanager;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserConstructor() {
        User user = new User("testuser", "password", "test@example.com");
        
        assertNotNull("User instance should not be null", user);
        assertEquals("Username should match", "testuser", user.getUsername());
        assertEquals("Password should match", "password", user.getPassword());
        assertEquals("Email should match", "test@example.com", user.getEmail());
    }
    
    @Test
    public void testSetUsername() {
        User user = new User("testuser", "password", "test@example.com");
        
        user.setUsername("newusername");
        assertEquals("Username should be updated", "newusername", user.getUsername());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetUsernameNull() {
        User user = new User("testuser", "password", "test@example.com");
        user.setUsername(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetUsernameEmpty() {
        User user = new User("testuser", "password", "test@example.com");
        user.setUsername("");
    }
    
    @Test
    public void testSetPassword() {
        User user = new User("testuser", "password", "test@example.com");
        
        user.setPassword("newpassword");
        assertEquals("Password should be updated", "newpassword", user.getPassword());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordNull() {
        User user = new User("testuser", "password", "test@example.com");
        user.setPassword(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordEmpty() {
        User user = new User("testuser", "password", "test@example.com");
        user.setPassword("");
    }
    
    @Test
    public void testSetEmail() {
        User user = new User("testuser", "password", "test@example.com");
        
        user.setEmail("new@example.com");
        assertEquals("Email should be updated", "new@example.com", user.getEmail());
    }
    
    @Test
    public void testSetEmailNull() {
        User user = new User("testuser", "password", "test@example.com");
        
        user.setEmail(null);
        assertNull("Email should be null", user.getEmail());
    }
    
    @Test
    public void testEquals() {
        User user1 = new User("testuser", "password1", "test1@example.com");
        User user2 = new User("testuser", "password2", "test2@example.com");
        User user3 = new User("otheruser", "password3", "test3@example.com");
        
        // İki kullanıcı aynı username'e sahipse eşittir
        assertTrue("Users with same username should be equal", user1.equals(user2));
        
        // Farklı username'e sahip kullanıcılar eşit değildir
        assertFalse("Users with different usernames should not be equal", user1.equals(user3));
        
        // Kendisiyle karşılaştırma
        assertTrue("User should be equal to itself", user1.equals(user1));
        
        // null ile karşılaştırma
        assertFalse("User should not be equal to null", user1.equals(null));
        
        // Farklı türdeki bir nesne ile karşılaştırma
        assertFalse("User should not be equal to different object type", user1.equals("testuser"));
    }
    
    @Test
    public void testHashCode() {
        User user1 = new User("testuser", "password1", "test1@example.com");
        User user2 = new User("testuser", "password2", "test2@example.com");
        
        // Aynı username'e sahip kullanıcıların hashcode'ları aynı olmalı
        assertEquals("Hash codes for users with same username should be equal", 
                     user1.hashCode(), user2.hashCode());
                     
        // Username'in hashcode'u ile User'ın hashcode'u aynı olmalı
        assertEquals("User's hash code should equal username's hash code",
                     "testuser".hashCode(), user1.hashCode());
    }
    
    @Test
    public void testToString() {
        User user = new User("testuser", "password", "test@example.com");
        
        String expectedString = "User: testuser (test@example.com)";
        assertEquals("toString should return correct format", expectedString, user.toString());
    }
    
    @Test
    public void testSerialVersionUID() {
        try {
            java.lang.reflect.Field field = User.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            
            assertEquals("serialVersionUID should be 1L", 1L, field.getLong(null));
        } catch (Exception e) {
            fail("Failed to access serialVersionUID: " + e.getMessage());
        }
    }
} 