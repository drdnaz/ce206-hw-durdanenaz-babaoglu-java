package com.naz.taskmanager;

import java.io.Serializable;

/**
 * Represents a user in the task manager application.
 * Contains user authentication and identification information.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class User implements Serializable {
    /** Serial version UID for serialization */
    private static final long serialVersionUID = 1L;
    
    /** Username (unique identifier) */
    private String username;
    
    /** User's password */
    private String password;
    
    /** User's email address */
    private String email;
    
    /**
     * Constructor for creating a new User
     * 
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
     * Gets the username
     * 
     * @return Username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username
     * 
     * @param username New username
     * @throws IllegalArgumentException if username is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }
    
    /**
     * Gets the password
     * 
     * @return Password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password
     * 
     * @param password New password
     * @throws IllegalArgumentException if password is null or empty
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }
    
    /**
     * Gets the email
     * 
     * @return Email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email
     * 
     * @param email New email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Checks if this user is equal to another object
     * Two users are equal if they have the same username
     * 
     * @param obj Object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return username.equals(user.username);
    }
    
    /**
     * Generates hash code for the user
     * Based on the username which is unique
     * 
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }
    
    /**
     * Returns a string representation of the user
     * 
     * @return String representation
     */
    @Override
    public String toString() {
        return "User: " + username + " (" + email + ")";
    }
}