package com.naz.taskmanager.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User("testUser", "password123", "test@example.com");
        
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testSetters() {
        User user = new User("initialUser", "initialPass", "initial@email.com");
        
        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setEmail("new@email.com");
        
        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    public void testEqualsWithSameObject() {
        User user = new User("testUser", "password", "test@example.com");
        assertTrue(user.equals(user));
    }

    @Test
    public void testEqualsWithNull() {
        User user = new User("testUser", "password", "test@example.com");
        assertFalse(user.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        User user = new User("testUser", "password", "test@example.com");
        assertFalse(user.equals("not a user"));
    }

    @Test
    public void testEqualsWithSameUsername() {
        User user1 = new User("testUser", "password1", "test1@example.com");
        User user2 = new User("testUser", "password2", "test2@example.com");
        assertTrue(user1.equals(user2));
    }

    @Test
    public void testEqualsWithDifferentUsername() {
        User user1 = new User("user1", "password", "test@example.com");
        User user2 = new User("user2", "password", "test@example.com");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void testHashCode() {
        User user1 = new User("testUser", "password1", "test1@example.com");
        User user2 = new User("testUser", "password2", "test2@example.com");
        User user3 = new User("differentUser", "password", "test@example.com");
        
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
} 