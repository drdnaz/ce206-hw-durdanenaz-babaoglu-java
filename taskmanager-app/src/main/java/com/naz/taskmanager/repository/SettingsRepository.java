package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.NotificationSettings;
import java.sql.*;

/**
 * @brief Repository for NotificationSettings using SQLite database.
 * 
 * @details Handles persistence of user notification preferences.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class SettingsRepository {
    /** @brief Database connection */
    private final Connection connection;
    
    /** @brief Username for user-specific settings */
    private final String username;
    
    /**
     * @brief Constructor for SettingsRepository
     * 
     * @param username Username for user-specific settings
     */
    public SettingsRepository(String username) {
        this.connection = DatabaseConnection.getInstance(System.out).getConnection();
        this.username = username;
    }
    
    /**
     * @brief Saves notification settings to the database
     * 
     * @param settings NotificationSettings to save
     */
    public void saveSettings(NotificationSettings settings) {
        String sql = "INSERT OR REPLACE INTO Settings (username, email_enabled, app_notifications_enabled, " +
                     "default_reminder_minutes) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, settings.isEmailEnabled() ? 1 : 0);
            stmt.setInt(3, settings.isAppNotificationsEnabled() ? 1 : 0);
            stmt.setInt(4, settings.getDefaultReminderMinutes());
            
            stmt.executeUpdate();
            System.out.println("Settings saved successfully for user: " + username);
        } catch (SQLException e) {
            System.out.println("Error saving settings: " + e.getMessage());
        }
    }
    
    /**
     * @brief Gets notification settings from the database
     * 
     * @return NotificationSettings for the user
     */
    public NotificationSettings getSettings() {
        String sql = "SELECT email_enabled, app_notifications_enabled, default_reminder_minutes " +
                     "FROM Settings WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NotificationSettings settings = new NotificationSettings();
                    settings.setEmailEnabled(rs.getInt("email_enabled") == 1);
                    settings.setAppNotificationsEnabled(rs.getInt("app_notifications_enabled") == 1);
                    settings.setDefaultReminderMinutes(rs.getInt("default_reminder_minutes"));
                    return settings;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting settings: " + e.getMessage());
        }
        
        // Return default settings if none found
        return new NotificationSettings();
    }
}