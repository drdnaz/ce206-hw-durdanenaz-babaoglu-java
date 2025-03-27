package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.TaskmanagerItem;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for TaskmanagerItem entities
 * Implements Single Responsibility Principle
 */
public class TaskRepository implements Repository<TaskmanagerItem> {
    private final String fileName;
    
    /**
     * Constructor for TaskRepository
     * @param username Username to create user-specific storage
     */
    public TaskRepository(String username) {
        this.fileName = username + "_tasks.bin";
    }
    
    /**
     * Save a new task
     * @param task Task to save
     */
    @Override
    public void save(TaskmanagerItem task) {
        List<TaskmanagerItem> tasks = getAll();
        tasks.add(task);
        saveAll(tasks);
    }
    
    /**
     * Get a task by its ID
     * @param id Task ID
     * @return Task with the matching ID, or null if not found
     */
    @Override
    public TaskmanagerItem getById(String id) {
        for (TaskmanagerItem task : getAll()) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }
    
    /**
     * Get all tasks
     * @return List of all tasks
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<TaskmanagerItem> getAll() {
        File file = new File(fileName);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<TaskmanagerItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: Could not load tasks. Starting with empty task list.");
            return new ArrayList<>();
        }
    }
    
    /**
     * Update an existing task
     * @param task Task to update
     */
    @Override
    public void update(TaskmanagerItem task) {
        List<TaskmanagerItem> tasks = getAll();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(task.getId())) {
                tasks.set(i, task);
                saveAll(tasks);
                return;
            }
        }
    }
    
    /**
     * Delete a task by ID
     * @param id ID of the task to delete
     */
    @Override
    public void delete(String id) {
        List<TaskmanagerItem> tasks = getAll();
        tasks.removeIf(task -> task.getId().equals(id));
        saveAll(tasks);
    }
    
    /**
     * Save all tasks to storage
     * @param tasks List of tasks to save
     */
    private void saveAll(List<TaskmanagerItem> tasks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}