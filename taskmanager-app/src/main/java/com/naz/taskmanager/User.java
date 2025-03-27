package com.naz.taskmanager;

import java.io.Serializable;

/**
 * Represents a user in the task manager application
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String email;
    
    /**
     * Constructor for User
     * @param username Username
     * @param password Password
     * @param email Email address
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    /**
     * Get the username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Set the username
     * @param username new username
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }
    
    /**
     * Get the password
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Set the password
     * @param password new password
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }
    
    /**
     * Get the email
     * @return email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Set the email
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return username.equals(user.username);
    }
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
    
    @Override
    public String toString() {
        return "User: " + username + " (" + email + ")";
    }
}