package com.naz.taskmanager.repository;

import com.naz.taskmanager.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for User entities
 */
public class UserRepository implements Repository<User> {
    private final String fileName;
    
    /**
     * Constructor for UserRepository
     * @param userDataPath Path to user data file
     */
    public UserRepository(String userDataPath) {
        this.fileName = userDataPath;
    }
    
    /**
     * Save a new user
     * @param user User to save
     */
    @Override
    public void save(User user) {
        List<User> users = getAll();
        users.add(user);
        saveAll(users);
    }
    
    /**
     * Get a user by username (ID)
     * @param username Username as ID
     * @return User with matching username, or null if not found
     */
    @Override
    public User getById(String username) {
        for (User user : getAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        File file = new File(fileName);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: Could not load users. Starting with empty user list.");
            return new ArrayList<>();
        }
    }
    
    /**
     * Update an existing user
     * @param user User to update
     */
    @Override
    public void update(User user) {
        List<User> users = getAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                saveAll(users);
                return;
            }
        }
    }
    
    /**
     * Delete a user by username
     * @param username Username of user to delete
     */
    @Override
    public void delete(String username) {
        List<User> users = getAll();
        users.removeIf(user -> user.getUsername().equals(username));
        saveAll(users);
    }
    
    /**
     * Save all users to storage
     * @param users List of users to save
     */
    private void saveAll(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * Authenticate a user
     * @param username Username
     * @param password Password
     * @return User if authenticated, null otherwise
     */
    public User authenticateUser(String username, String password) {
        User user = getById(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    /**
     * Check if a username exists
     * @param username Username to check
     * @return true if username exists
     */
    public boolean userExists(String username) {
        return getById(username) != null;
    }
}