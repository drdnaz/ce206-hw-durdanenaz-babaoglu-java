package com.naz.taskmanager.service;

import com.naz.taskmanager.model.User;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for user management.
 * Implements Singleton pattern for user operations.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class UserManager {
    private static final String USER_DATA_FILE = "data/users.dat";
    private Map<String, User> users;
    private static UserManager instance;
    
    private UserManager() {
        users = new HashMap<>();
        loadUsers();
    }
    
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    public boolean registerUser(String username, String password, String email) {
        if (users.containsKey(username)) {
            return false;
        }
        
        User newUser = new User(username, password, email);
        users.put(username, newUser);
        saveUsers();
        return true;
    }
    
    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    private void loadUsers() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void saveUsers() {
        File file = new File(USER_DATA_FILE);
        file.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 