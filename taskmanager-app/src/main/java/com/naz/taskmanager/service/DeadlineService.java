// DeadlineService.java
package com.naz.taskmanager.service;

import com.naz.taskmanager.model.TaskmanagerItem;
import java.util.*;

/**
 * Service for deadline management
 */
public class DeadlineService {
    private final TaskService taskService;
    
    /**
     * Constructor for DeadlineService
     * @param taskService Task service for task operations
     */
    public DeadlineService(TaskService taskService) {
        this.taskService = taskService;
    }
    
    /**
     * Get upcoming deadlines within a certain number of days
     * @param days Number of days to look ahead
     * @return List of tasks with deadlines within specified days
     */
    public List<TaskmanagerItem> getUpcomingDeadlines(int days) {
        List<TaskmanagerItem> allTasks = taskService.getAllTasks();
        List<TaskmanagerItem> upcomingTasks = new ArrayList<>();
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date futureDate = calendar.getTime();
        
        for (TaskmanagerItem task : allTasks) {
            if (!task.isCompleted() && task.getDeadline() != null) {
                Date deadline = task.getDeadline();
                if (deadline.after(new Date()) && deadline.before(futureDate)) {
                    upcomingTasks.add(task);
                }
            }
        }
        
        // Sort by deadline
        upcomingTasks.sort(Comparator.comparing(TaskmanagerItem::getDeadline));
        
        return upcomingTasks;
    }
    
    /**
     * Get overdue tasks
     * @return List of overdue tasks
     */
    public List<TaskmanagerItem> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }
    
    /**
     * Get tasks due today
     * @return List of tasks due today
     */
    public List<TaskmanagerItem> getTasksDueToday() {
        List<TaskmanagerItem> allTasks = taskService.getAllTasks();
        List<TaskmanagerItem> todayTasks = new ArrayList<>();
        
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        
        for (TaskmanagerItem task : allTasks) {
            if (!task.isCompleted() && task.getDeadline() != null) {
                cal1.setTime(new Date());
                cal2.setTime(task.getDeadline());
                
                boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
                
                if (sameDay) {
                    todayTasks.add(task);
                }
            }
        }
        
        // Sort by deadline time
        todayTasks.sort(Comparator.comparing(TaskmanagerItem::getDeadline));
        
        return todayTasks;
    }
    
    /**
     * Extend deadline for a task
     * @param taskId Task ID
     * @param days Days to extend
     * @return true if successful
     */
    public boolean extendDeadline(String taskId, int days) {
        TaskmanagerItem task = taskService.getTask(taskId);
        if (task == null) {
            return false;
        }
        
        Date deadline = task.getDeadline();
        if (deadline == null) {
            return false;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(deadline);
        cal.add(Calendar.DAY_OF_MONTH, days);
        task.setDeadline(cal.getTime());
        
        taskService.updateTask(task);
        return true;
    }
    
    /**
     * Get tasks in date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of tasks in date range
     */
    public List<TaskmanagerItem> getTasksInDateRange(Date startDate, Date endDate) {
        return taskService.getTasksInDateRange(startDate, endDate);
    }
    
    /**
     * Sort tasks by deadline
     * @return Sorted list of tasks
     */
    public List<TaskmanagerItem> sortTasksByDeadline() {
        return taskService.sortTasksByDeadline();
    }
    
    /**
     * Check task deadline status
     * @param taskId Task ID
     * @return Status string ("overdue", "upcoming", "today", "no deadline")
     */
    public String checkDeadlineStatus(String taskId) {
        TaskmanagerItem task = taskService.getTask(taskId);
        if (task == null || task.getDeadline() == null) {
            return "no deadline";
        }
        
        if (task.isOverdue()) {
            return "overdue";
        }
        
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal2.setTime(task.getDeadline());
        
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                          cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        
        if (sameDay) {
            return "today";
        }
        
        return "upcoming";
    }
}