package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.Reminder;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Reminder entities using SQLite
 */
public class ReminderRepository implements Repository<Reminder> {
    private final Connection connection;
    private final String username;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructor for ReminderRepository
     * @param username Username for user-specific reminders
     */
    public ReminderRepository(String username) {
        this.connection = DatabaseConnection.getInstance(System.out).getConnection();
        this.username = username;
    }
    
    /**
     * Save a new reminder
     * @param reminder Reminder to save
     */
    @Override
    public void save(Reminder reminder) {
        String sql = "INSERT INTO Reminders (task_id, reminder_time, triggered, message) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, Long.parseLong(reminder.getTaskId()));
            
            if (reminder.getReminderTime() != null) {
                stmt.setString(2, dateFormat.format(reminder.getReminderTime()));
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            
            stmt.setInt(3, reminder.isTriggered() ? 1 : 0);
            stmt.setString(4, reminder.getMessage());
            
            stmt.executeUpdate();
            
            // Set the generated ID back to the reminder
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reminder.setId(String.valueOf(rs.getLong(1)));
                }
            }
            
            System.out.println("Reminder saved successfully for task: " + reminder.getTaskId());
        } catch (SQLException e) {
            System.out.println("Error saving reminder: " + e.getMessage());
        }
    }
    
    /**
     * Get a reminder by its ID
     * @param id Reminder ID
     * @return Reminder with the matching ID, or null if not found
     */
    @Override
    public Reminder getById(String id) {
        String sql = "SELECT r.id, r.task_id, r.reminder_time, r.triggered, r.message " +
                     "FROM Reminders r JOIN Tasks t ON r.task_id = t.id " +
                     "WHERE r.id = ? AND t.username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(id));
            stmt.setString(2, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createReminderFromResultSet(rs);
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println("Error getting reminder by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create Reminder object from ResultSet
     * @param rs ResultSet containing reminder data
     * @return Reminder object
     */
    private Reminder createReminderFromResultSet(ResultSet rs) throws SQLException, ParseException {
        Reminder reminder = new Reminder();
        
        reminder.setId(String.valueOf(rs.getLong("id")));
        reminder.setTaskId(String.valueOf(rs.getLong("task_id")));
        
        String reminderTimeStr = rs.getString("reminder_time");
        if (reminderTimeStr != null) {
            reminder.setReminderTime(dateFormat.parse(reminderTimeStr));
        }
        
        reminder.setTriggered(rs.getInt("triggered") == 1);
        reminder.setMessage(rs.getString("message"));
        
        return reminder;
    }
    
    /**
     * Get all reminders
     * @return List of all reminders
     */
    @Override
    public List<Reminder> getAll() {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT r.id, r.task_id, r.reminder_time, r.triggered, r.message " +
                     "FROM Reminders r JOIN Tasks t ON r.task_id = t.id " +
                     "WHERE t.username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reminders.add(createReminderFromResultSet(rs));
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println("Error getting all reminders: " + e.getMessage());
        }
        
        return reminders;
    }
    
    /**
     * Update an existing reminder
     * @param reminder Reminder to update
     */
    @Override
    public void update(Reminder reminder) {
        String sql = "UPDATE Reminders SET reminder_time = ?, triggered = ?, message = ? " +
                     "WHERE id = ? AND task_id IN (SELECT id FROM Tasks WHERE username = ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (reminder.getReminderTime() != null) {
                stmt.setString(1, dateFormat.format(reminder.getReminderTime()));
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }
            
            stmt.setInt(2, reminder.isTriggered() ? 1 : 0);
            stmt.setString(3, reminder.getMessage());
            stmt.setLong(4, Long.parseLong(reminder.getId()));
            stmt.setString(5, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reminder updated successfully: ID " + reminder.getId());
            } else {
                System.out.println("No reminder found with ID: " + reminder.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating reminder: " + e.getMessage());
        }
    }
    
    /**
     * Delete a reminder by ID
     * @param id ID of the reminder to delete
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Reminders WHERE id = ? AND task_id IN (SELECT id FROM Tasks WHERE username = ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(id));
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reminder deleted successfully: ID " + id);
            } else {
                System.out.println("No reminder found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting reminder: " + e.getMessage());
        }
    }
    
    /**
     * Get reminders for a specific task
     * @param taskId ID of the task
     * @return List of reminders for the task
     */
    public List<Reminder> getRemindersForTask(String taskId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT r.id, r.task_id, r.reminder_time, r.triggered, r.message " +
                     "FROM Reminders r JOIN Tasks t ON r.task_id = t.id " +
                     "WHERE r.task_id = ? AND t.username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(taskId));
            stmt.setString(2, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reminders.add(createReminderFromResultSet(rs));
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println("Error getting reminders for task: " + e.getMessage());
        }
        
        return reminders;
    }
}