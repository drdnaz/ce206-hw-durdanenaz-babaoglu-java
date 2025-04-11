package com.naz.taskmanager.service;

import com.naz.taskmanager.model.Reminder;
import com.naz.taskmanager.model.TaskmanagerItem;
import com.naz.taskmanager.repository.ReminderRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Service for reminder management.
 * Implements Observer Pattern for notification of due reminders.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class ReminderService {
    /**
     * Observer Pattern: Interface for reminder observers.
     * Observers receive notifications when reminders are due.
     */
    public interface ReminderObserver {
        /**
         * Called when a reminder is due
         * 
         * @param reminder The due reminder
         * @param taskId ID of the associated task
         */
        void onReminderDue(Reminder reminder, String taskId);
    }
    
    /** Reminder repository for data persistence */
    private final ReminderRepository reminderRepository;
    
    /** List of observers for notification */
    private final List<ReminderObserver> observers;
    
    /**
     * Constructor for ReminderService
     * 
     * @param username Username for repository creation
     */
    public ReminderService(String username) {
        this.reminderRepository = new ReminderRepository(username);
        this.observers = new ArrayList<>();
    }
    
    /**
     * Observer Pattern: Add an observer to receive notifications
     * 
     * @param observer Observer to add
     */
    public void addObserver(ReminderObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Observer Pattern: Remove an observer
     * 
     * @param observer Observer to remove
     */
    public void removeObserver(ReminderObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Observer Pattern: Notify all observers of a due reminder
     * 
     * @param reminder The due reminder
     */
    private void notifyObservers(Reminder reminder) {
        for (ReminderObserver observer : observers) {
            observer.onReminderDue(reminder, reminder.getTaskId());
        }
    }
    
    /**
     * Creates a reminder for a task
     * 
     * @param taskId ID of the task
     * @param reminderTime Time for the reminder
     * @return The created reminder
     */
    public Reminder createReminder(String taskId, Date reminderTime) {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time cannot be null");
        }
        
        Reminder reminder = new Reminder();
        reminder.setTaskId(taskId);
        reminder.setReminderTime(reminderTime);
        
        reminderRepository.save(reminder);
        return reminder;
    }
    
    /**
     * Creates a reminder before a task's deadline
     * 
     * @param task Task to create reminder for
     * @param minutesBefore Minutes before deadline
     * @return The created reminder
     * @throws IllegalArgumentException if task has no deadline
     */
    public Reminder createReminderBeforeDeadline(TaskmanagerItem task, int minutesBefore) {
        Date deadline = task.getDeadline();
        if (deadline == null) {
            throw new IllegalArgumentException("Task does not have a deadline");
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(deadline);
        cal.add(Calendar.MINUTE, -minutesBefore);
        Date reminderTime = cal.getTime();
        
        return createReminder(task.getId(), reminderTime);
    }
    
    /**
     * Gets all reminders
     * 
     * @return List of all reminders
     */
    public List<Reminder> getAllReminders() {
        return reminderRepository.getAll();
    }
    
    /**
     * Gets all reminders for a specific task
     * 
     * @param taskId ID of the task
     * @return List of reminders for the task
     */
    public List<Reminder> getRemindersForTask(String taskId) {
        List<Reminder> reminders = getAllReminders();
        List<Reminder> taskReminders = new ArrayList<>();
        
        for (Reminder reminder : reminders) {
            if (reminder.getTaskId() != null && reminder.getTaskId().equals(taskId)) {
                taskReminders.add(reminder);
            }
        }
        
        return taskReminders;
    }
    
    /**
     * Gets all due reminders (time has passed but not triggered)
     * 
     * @return List of due reminders
     */
    public List<Reminder> getDueReminders() {
        List<Reminder> reminders = getAllReminders();
        List<Reminder> dueReminders = new ArrayList<>();
        Date now = new Date();
        
        for (Reminder reminder : reminders) {
            if (!reminder.isTriggered() && reminder.getReminderTime() != null 
                    && reminder.getReminderTime().before(now)) {
                dueReminders.add(reminder);
            }
        }
        
        return dueReminders;
    }
    
    /**
     * Marks a reminder as triggered
     * 
     * @param reminder Reminder to update
     */
    public void markReminderAsTriggered(Reminder reminder) {
        reminder.setTriggered(true);
        reminderRepository.update(reminder);
    }
    
    /**
     * Deletes a reminder
     * 
     * @param id ID of reminder to delete
     */
    public void deleteReminder(String id) {
        reminderRepository.delete(id);
    }
    
    /**
     * Checks for due reminders and notifies observers
     * 
     * @param taskService TaskService to get task information
     */
    public void checkReminders(TaskService taskService) {
        List<Reminder> dueReminders = getDueReminders();
        
        for (Reminder reminder : dueReminders) {
            markReminderAsTriggered(reminder);
            notifyObservers(reminder);
        }
    }
}