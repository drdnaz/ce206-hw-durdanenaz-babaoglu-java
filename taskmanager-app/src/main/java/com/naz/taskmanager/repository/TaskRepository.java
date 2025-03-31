package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.Priority;
import com.naz.taskmanager.model.TaskmanagerItem;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repository for TaskmanagerItem entities using SQLite
 */
public class TaskRepository implements Repository<TaskmanagerItem> {
    private final Connection connection;
    private final String username;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructor for TaskRepository
     * @param username Username for user-specific tasks
     */
    public TaskRepository(String username) {
        this.connection = DatabaseConnection.getInstance(System.out).getConnection();
        this.username = username;
    }
    
    /**
     * Get or create category ID for a category
     * @param category Category object
     * @return ID of the category in the database
     */
    private int getOrCreateCategoryId(Category category) throws SQLException {
        if (category == null) {
            return -1;
        }
        
        // First try to find the existing category
        String selectSql = "SELECT id FROM Categories WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setString(1, category.getName());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        
        // If not found, create new category
        String insertSql = "INSERT INTO Categories (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category object
     */
    private Category getCategoryById(int categoryId) throws SQLException {
        if (categoryId < 0) {
            return new Category("Uncategorized");
        }
        
        String sql = "SELECT name FROM Categories WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getString("name"));
                }
            }
        }
        
        return new Category("Uncategorized");
    }
    
    /**
     * Save a new task
     * @param task Task to save
     */
    @Override
    public void save(TaskmanagerItem task) {
        String sql = "INSERT INTO Tasks (username, name, description, category_id, deadline, " +
                     "priority, completed, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int categoryId = getOrCreateCategoryId(task.getCategory());
            
            stmt.setString(1, username);
            stmt.setString(2, task.getName());
            stmt.setString(3, task.getDescription());
            stmt.setInt(4, categoryId);
            
            if (task.getDeadline() != null) {
                stmt.setString(5, dateFormat.format(task.getDeadline()));
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            
            stmt.setInt(6, task.getPriority().ordinal());
            stmt.setInt(7, task.isCompleted() ? 1 : 0);
            stmt.setString(8, dateFormat.format(task.getCreationDate()));
            
            stmt.executeUpdate();
            
            // Set the generated ID back to the task
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setId(String.valueOf(rs.getLong(1)));
                }
            }
            
            System.out.println("Task saved successfully: " + task.getName());
        } catch (SQLException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }
    
    /**
     * Get a task by its ID
     * @param id Task ID
     * @return Task with the matching ID, or null if not found
     */
    @Override
    public TaskmanagerItem getById(String id) {
        String sql = "SELECT id, name, description, category_id, deadline, priority, " +
                     "completed, creation_date FROM Tasks WHERE id = ? AND username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(id));
            stmt.setString(2, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createTaskFromResultSet(rs);
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println("Error getting task by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create TaskmanagerItem object from ResultSet
     * @param rs ResultSet containing task data
     * @return TaskmanagerItem object
     */
    private TaskmanagerItem createTaskFromResultSet(ResultSet rs) throws SQLException, ParseException {
        Category category = getCategoryById(rs.getInt("category_id"));
        TaskmanagerItem task = new TaskmanagerItem(
            rs.getString("name"),
            rs.getString("description"),
            category
        );
        
        task.setId(String.valueOf(rs.getLong("id")));
        
        String deadlineStr = rs.getString("deadline");
        if (deadlineStr != null) {
            task.setDeadline(dateFormat.parse(deadlineStr));
        }
        
        int priorityOrdinal = rs.getInt("priority");
        task.setPriority(Priority.values()[priorityOrdinal]);
        
        task.setCompleted(rs.getInt("completed") == 1);
        
        String creationDateStr = rs.getString("creation_date");
        if (creationDateStr != null) {
            Date creationDate = dateFormat.parse(creationDateStr);
            // We need a method to set the creation date which doesn't exist in the current model
            // For now, we'll leave it as is
        }
        
        return task;
    }
    
    /**
     * Get all tasks
     * @return List of all tasks
     */
    @Override
    public List<TaskmanagerItem> getAll() {
        List<TaskmanagerItem> tasks = new ArrayList<>();
        String sql = "SELECT id, name, description, category_id, deadline, priority, " + 
                     "completed, creation_date FROM Tasks WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(createTaskFromResultSet(rs));
                }
            }
        } catch (SQLException | ParseException e) {
            System.out.println("Error getting all tasks: " + e.getMessage());
        }
        
        return tasks;
    }
    
    /**
     * Update an existing task
     * @param task Task to update
     */
    @Override
    public void update(TaskmanagerItem task) {
        String sql = "UPDATE Tasks SET name = ?, description = ?, category_id = ?, " +
                     "deadline = ?, priority = ?, completed = ? WHERE id = ? AND username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int categoryId = getOrCreateCategoryId(task.getCategory());
            
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, categoryId);
            
            if (task.getDeadline() != null) {
                stmt.setString(4, dateFormat.format(task.getDeadline()));
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setInt(5, task.getPriority().ordinal());
            stmt.setInt(6, task.isCompleted() ? 1 : 0);
            stmt.setLong(7, Long.parseLong(task.getId()));
            stmt.setString(8, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task updated successfully: " + task.getName());
            } else {
                System.out.println("No task found with ID: " + task.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }
    
    /**
     * Delete a task by ID
     * @param id ID of the task to delete
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Tasks WHERE id = ? AND username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(id));
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task deleted successfully: ID " + id);
            } else {
                System.out.println("No task found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }
}