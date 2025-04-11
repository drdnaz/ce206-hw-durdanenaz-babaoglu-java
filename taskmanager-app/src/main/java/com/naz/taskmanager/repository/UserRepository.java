package com.naz.taskmanager.repository;

import com.naz.taskmanager.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for User entities using SQLite database.
 * Implements the Repository interface for user data persistence.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class UserRepository implements Repository<User> {
    /** Database connection */
    private final Connection connection;
    
    /**
     * Constructor for UserRepository
     */
    public UserRepository() {
        this.connection = DatabaseConnection.getInstance(System.out).getConnection();
    }
    
    /**
     * Saves a new user to the database.
     * Updates the user if it already exists.
     * 
     * @param user User to save
     */
    @Override
    public void save(User user) {
        // First check if user already exists
        if (userExists(user.getUsername())) {
            try {
                // Update user if it exists
                update(user);
                System.out.println("User updated successfully: " + user.getUsername());
                return;
            } catch (Exception e) {
                System.out.println("Error updating user: " + e.getMessage());
                throw new RuntimeException("Error updating user", e);
            }
        }
        
        String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            
            stmt.executeUpdate();
            System.out.println("User saved successfully: " + user.getUsername());
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            throw new RuntimeException("Error saving user", e);
        }
    }
    
    /**
     * Gets a user by username (ID)
     * 
     * @param username Username as ID
     * @return User with matching username, or null if not found
     */
    @Override
    public User getById(String username) {
        String sql = "SELECT username, password, email FROM Users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting user by ID: " + e.getMessage());
            throw new RuntimeException("Error getting user by ID", e);
        }
        
        return null;
    }
    
    /**
     * Gets all users from the database
     * 
     * @return List of all users
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, password, email FROM Users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all users: " + e.getMessage());
            throw new RuntimeException("Error getting all users", e);
        }
        
        return users;
    }
    
    /**
     * Updates an existing user in the database
     * 
     * @param user User to update
     */
    @Override
    public void update(User user) {
        String sql = "UPDATE Users SET password = ?, email = ? WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getUsername());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully: " + user.getUsername());
            } else {
                System.out.println("No user found with username: " + user.getUsername());
                throw new RuntimeException("User not found: " + user.getUsername());
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            throw new RuntimeException("Error updating user", e);
        }
    }
    
    /**
     * Deletes a user by username from the database
     * 
     * @param username Username of user to delete
     */
    @Override
    public void delete(String username) {
        String sql = "DELETE FROM Users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully: " + username);
            } else {
                System.out.println("No user found with username: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException("Error deleting user", e);
        }
    }
    
    /**
     * Authenticates a user with username and password
     * 
     * @param username Username
     * @param password Password
     * @return User if authenticated, null otherwise
     */
    public User authenticateUser(String username, String password) {
        String sql = "SELECT username, password, email FROM Users WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
            throw new RuntimeException("Error authenticating user", e);
        }
        
        return null;
    }
    
    /**
     * Checks if a username exists in the database
     * 
     * @param username Username to check
     * @return true if username exists
     */
    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
            throw new RuntimeException("Error checking if user exists", e);
        }
    }
}