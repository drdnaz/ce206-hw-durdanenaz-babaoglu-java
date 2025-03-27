package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.Reminder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Reminder entities
 * Implements Single Responsibility Principle
 */
public class ReminderRepository implements Repository<Reminder> {
    private final String fileName;
    
    /**
     * Constructor for ReminderRepository
     * @param username Username to create user-specific storage
     */
    public ReminderRepository(String username) {
        this.fileName = username + "_reminders.bin";
    }
    
    /**
     * Save a new reminder
     * @param reminder Reminder to save
     */
    @Override
    public void save(Reminder reminder) {
        List<Reminder> reminders = getAll();
        reminders.add(reminder);
        saveAll(reminders);
    }
    
    /**
     * Get a reminder by its ID
     * @param id Reminder ID
     * @return Reminder with the matching ID, or null if not found
     */
    @Override
    public Reminder getById(String id) {
        for (Reminder reminder : getAll()) {
            if (reminder.getId().equals(id)) {
                return reminder;
            }
        }
        return null;
    }
    
    /**
     * Get all reminders
     * @return List of all reminders
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Reminder> getAll() {
        File file = new File(fileName);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Reminder>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: Could not load reminders. Starting with empty reminder list.");
            return new ArrayList<>();
        }
    }
    
    /**
     * Update an existing reminder
     * @param reminder Reminder to update
     */
    @Override
    public void update(Reminder reminder) {
        List<Reminder> reminders = getAll();
        for (int i = 0; i < reminders.size(); i++) {
            if (reminders.get(i).getId().equals(reminder.getId())) {
                reminders.set(i, reminder);
                saveAll(reminders);
                return;
            }
        }
    }
    
    /**
     * Delete a reminder by ID
     * @param id ID of the reminder to delete
     */
    @Override
    public void delete(String id) {
        List<Reminder> reminders = getAll();
        reminders.removeIf(reminder -> reminder.getId().equals(id));
        saveAll(reminders);
    }
    
    /**
     * Get reminders for a specific task
     * @param taskId ID of the task
     * @return List of reminders for the task
     */
    public List<Reminder> getRemindersForTask(String taskId) {
        List<Reminder> taskReminders = new ArrayList<>();
        for (Reminder reminder : getAll()) {
            if (reminder.getTaskId().equals(taskId)) {
                taskReminders.add(reminder);
            }
        }
        return taskReminders;
    }
    
    /**
     * Save all reminders to storage
     * @param reminders List of reminders to save
     */
    private void saveAll(List<Reminder> reminders) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(reminders);
        } catch (IOException e) {
            System.out.println("Error saving reminders: " + e.getMessage());
        }
    }
}