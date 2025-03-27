package com.naz.taskmanager.service;

import com.naz.taskmanager.User;
import com.naz.taskmanager.repository.UserRepository;
import java.util.List;

/**
 * Service for user management
 * Implements Single Responsibility and Dependency Inversion principles
 */
public class UserService {
    private final UserRepository userRepository;
    
    /**
     * Constructor for UserService
     * @param userDataPath Path to user data file
     */
    public UserService(String userDataPath) {
        this.userRepository = new UserRepository(userDataPath);
    }
    
    /**
     * Authenticate a user
     * @param username Username
     * @param password Password
     * @return User if authenticated, null otherwise
     */
    public User authenticateUser(String username, String password) {
        return userRepository.authenticateUser(username, password);
    }
    
    /**
     * Register a new user
     * @param username Username
     * @param password Password
     * @param email Email
     * @return true if registration successful
     */
    public boolean registerUser(String username, String password, String email) {
        // Check if user already exists
        if (userRepository.userExists(username)) {
            return false;
        }
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Create and save user
        User user = new User(username, password, email);
        userRepository.save(user);
        return true;
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
    
    /**
     * Get a user by username
     * @param username Username
     * @return User with matching username
     */
    public User getUserByUsername(String username) {
        return userRepository.getById(username);
    }
    
    /**
     * Update a user
     * @param user User to update
     */
    public void updateUser(User user) {
        userRepository.update(user);
    }
    
    /**
     * Delete a user
     * @param username Username of user to delete
     */
    public void deleteUser(String username) {
        userRepository.delete(username);
    }
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if username exists
     */
    public boolean usernameExists(String username) {
        return userRepository.userExists(username);
    }
    
    /**
     * Change user password
     * @param username Username
     * @param oldPassword Old password
     * @param newPassword New password
     * @return true if password changed successfully
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = authenticateUser(username, oldPassword);
        if (user == null) {
            return false;
        }
        
        user.setPassword(newPassword);
        userRepository.update(user);
        return true;
    }
}