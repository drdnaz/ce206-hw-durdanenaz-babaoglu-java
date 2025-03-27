package com.naz.taskmanager;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.naz.taskmanager.model.*;
import com.naz.taskmanager.service.*;
import com.naz.taskmanager.ui.menu.*;

/**
 * Main application class that implements the Singleton pattern
 */
public class Taskmanager {
    private static Taskmanager instance;
    
    private final Scanner in;
    private final PrintStream out;
    private User currentUser;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    // Services
    private TaskService taskService;
    private ReminderService reminderService;
    private UserService userService;
    
    /**
     * Private constructor for Singleton pattern
     * @param in Scanner for user input
     * @param out PrintStream for output
     */
    private Taskmanager(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    /**
     * Get singleton instance
     * @param in Scanner for user input
     * @param out PrintStream for output
     * @return Singleton instance
     */
    public static synchronized Taskmanager getInstance(Scanner in, PrintStream out) {
        if (instance == null) {
            instance = new Taskmanager(in, out);
        }
        return instance;
    }
    
    /**
     * Clear console screen
     */
    public void clearScreen() {
        out.print("\033[H\033[2J");
        out.flush();
        // Alternative for Windows
        for (int i = 0; i < 50; i++) {
            out.println();
        }
    }
    
    /**
     * Wait for user to press Enter
     */
    public void enterToContinue() {
        out.print("Press Enter to continue...");
        in.nextLine();
    }
    
    /**
     * Get integer input from user
     * @return User input as integer, -2 if error
     */
    public int getInput() {
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            return -2;
        }
    }
    
    /**
     * Handle input error
     */
    public void handleInputError() {
        out.println("Invalid input. Please enter a number.");
    }
    
    /**
     * Main menu flow
     * @param pathFileUsers Path to user data file
     */
    public void mainMenu(String pathFileUsers) {
        userService = new UserService(pathFileUsers);
        int choice;
        
        while (true) {
            clearScreen();
            openingScreenMenu();
            choice = getInput();
            
            if (choice == -2) {
                handleInputError();
                enterToContinue();
                continue;
            }
            
            switch (choice) {
                case 1:
                    clearScreen();
                    if (loginUserMenu()) {
                        userOptionsMenu();
                    }
                    break;
                case 2:
                    clearScreen();
                    registerUserMenu();
                    break;
                case 3:
                    out.println("Exit Program");
                    return;
                default:
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    
    /**
     * Display opening screen menu
     */
    public void openingScreenMenu() {
        out.println("*****************************************");
        out.println("*                                       *");
        out.println("*      WELCOME TO TASKMANAGER MANAGER!  *");
        out.println("*                                       *");
        out.println("***************************************\n");
        out.println("=============== MAIN MENU ===============");
        out.println("1. Login");
        out.println("2. Register");
        out.println("3. Exit Program");
        out.println("=========================================");
        out.print("Please enter a number: ");
    }
    
    /**
     * Login user menu
     * @return true if login successful
     */
    private boolean loginUserMenu() {
        out.println("========================================");
        out.println("               USER LOGIN              ");
        out.println("========================================");
        
        out.print("Enter username: ");
        String username = in.nextLine().trim();
        
        out.print("Enter password: ");
        String password = in.nextLine().trim();
        
        // Authenticate user using service
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            initializeServices();
            out.println("Login successful. Welcome, " + username + "!");
            enterToContinue();
            return true;
        } else {
            out.println("Invalid username or password.");
            enterToContinue();
            return false;
        }
    }
    
    /**
     * Register user menu
     */
    private void registerUserMenu() {
        out.println("========================================");
        out.println("           REGISTER NEW USER           ");
        out.println("========================================");
        
        out.print("Enter username: ");
        String username = in.nextLine().trim();
        
        // Check if username already exists
        if (userService.usernameExists(username)) {
            out.println("Username already exists. Please choose another one.");
            enterToContinue();
            return;
        }
        
        out.print("Enter password: ");
        String password = in.nextLine().trim();
        
        out.print("Enter email: ");
        String email = in.nextLine().trim();
        
        // Register user using service
        boolean success = userService.registerUser(username, password, email);
        
        if (success) {
            out.println("User registered successfully!");
        } else {
            out.println("Failed to register user. Please try again.");
        }
        
        enterToContinue();
    }
    
    /**
     * Initialize services for current user
     */
    private void initializeServices() {
        if (currentUser != null) {
            taskService = new TaskService(currentUser.getUsername());
            reminderService = new ReminderService(currentUser.getUsername());
            
            // Observer pattern: Add observer for reminders
            reminderService.addObserver((reminder, taskId) -> {
                TaskmanagerItem task = taskService.getTask(taskId);
                if (task != null) {
                    showNotification("Reminder: " + task.getName(), 
                        "Due: " + (task.getDeadline() != null ? 
                            dateFormat.format(task.getDeadline()) : "No deadline"));
                }
            });
            
            // Check for reminders
            checkReminders();
        }
    }
    
    /**
     * User options menu
     */
    public void userOptionsMenu() {
        int choice;
        while (true) {
            printMainMenu();
            choice = getInput();
            if (choice == -2) {
                handleInputError();
                enterToContinue();
                continue;
            }
            switch (choice) {
                case 1:
                    createTaskmanagerMenu();
                    break;
                case 2:
                    deadlineSettingsMenu();
                    break;
                case 3:
                    reminderSystemMenu();
                    break;
                case 4:
                    TaskmanagerPrioritizationMenu();
                    break;
                case 5:
                    logout();
                    return;
                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    
    /**
     * Logout current user
     */
    private void logout() {
        currentUser = null;
        taskService = null;
        reminderService = null;
    }
    
    /**
     * Display main menu
     */
    public void printMainMenu() {
        clearScreen();
        out.println("========================================");
        out.println("        MAIN MENU - TASKMANAGER MANAGER");
        out.println("========================================");
        out.println("1. Manage Tasks");
        out.println("2. Deadline Settings");
        out.println("3. Reminder System");
        out.println("4. Task Prioritization");
        out.println("5. Logout");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * Create task menu
     */
    public void createTaskmanagerMenu() {
        int choice;
        while (true) {
            printCreateTaskmanagerMenu();
            choice = getInput();
            if (choice == -2) {
                handleInputError();
                enterToContinue();
                continue;
            }
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    enterToContinue();
                    break;
                case 3:
                    categorizeTasks();
                    break;
                case 4:
                    deleteTasks();
                    break;
                case 5:
                    return;
                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    
    /**
     * Display create task menu
     */
    public void printCreateTaskmanagerMenu() {
        clearScreen();
        out.println("========================================");
        out.println("           MANAGE TASKS MENU            ");
        out.println("========================================");
        out.println("1. Add Task");
        out.println("2. View Tasks");
        out.println("3. Categorize Tasks");
        out.println("4. Delete Tasks");
        out.println("5. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * Add a new task
     */
    private void addTask() {
        clearScreen();
        out.println("========================================");
        out.println("               ADD TASK                ");
        out.println("========================================");
        
        out.print("Enter task name: ");
        String name = in.nextLine().trim();
        
        out.print("Enter task description: ");
        String description = in.nextLine().trim();
        
        // Select category
        Category category = selectCategory();
        
        // Create task using service
        TaskmanagerItem task = taskService.createTask(name, description, category);
        
        out.println("Task added successfully!");
        
        // Ask if user wants to add a deadline
        out.print("Do you want to add a deadline for this task? (y/n): ");
        String addDeadline = in.nextLine().trim().toLowerCase();
        if (addDeadline.equals("y") || addDeadline.equals("yes")) {
            assignDeadlineToTask(task);
        }
        
        enterToContinue();
    }
    
    /**
     * Select a category
     * @return Selected category
     */
    private Category selectCategory() {
        List<Category> categories = getCategories();
        
        out.println("Select category:");
        for (int i = 0; i < categories.size(); i++) {
            out.println((i + 1) + ". " + categories.get(i).getName());
        }
        out.println((categories.size() + 1) + ". Create new category");
        
        int choice;
        while (true) {
            out.print("Enter your choice: ");
            choice = getInput();
            
            if (choice >= 1 && choice <= categories.size()) {
                return categories.get(choice - 1);
            } else if (choice == categories.size() + 1) {
                out.print("Enter new category name: ");
                String categoryName = in.nextLine().trim();
                Category newCategory = new Category(categoryName);
                categories.add(newCategory);
                return newCategory;
            } else {
                out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Get all categories
     * @return List of categories
     */
    private List<Category> getCategories() {
        // In a real implementation, this would come from a repository
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Work"));
        categories.add(new Category("Personal"));
        categories.add(new Category("Study"));
        categories.add(new Category("Health"));
        categories.add(new Category("Other"));
        return categories;
    }
    
    /**
     * View all tasks
     */
    private void viewTasks() {
        clearScreen();
        out.println("========================================");
        out.println("              VIEW TASKS               ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            out.println("No tasks found.");
            return;
        }
        
        for (int i = 0; i < tasks.size(); i++) {
            TaskmanagerItem task = tasks.get(i);
            out.println((i + 1) + ". " + task.getName() + " [" + task.getCategory().getName() + "]");
            out.println("   Description: " + task.getDescription());
            if (task.getDeadline() != null) {
                out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
            }
            out.println("   Priority: " + task.getPriority());
            out.println("   Completed: " + (task.isCompleted() ? "Yes" : "No"));
            out.println("----------------------------------------");
        }
    }
    
    /**
     * Categorize tasks
     */
    private void categorizeTasks() {
        clearScreen();
        out.println("========================================");
        out.println("            CATEGORIZE TASKS           ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            out.println("No tasks to categorize.");
            enterToContinue();
            return;
        }
        
        // Display tasks
        viewTasks();
        
        out.print("Enter task number to recategorize (0 to cancel): ");
        int taskIndex = getInput() - 1;
        
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            out.println("Invalid task number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem task = tasks.get(taskIndex);
        out.println("Current category: " + task.getCategory().getName());
        
        // Select new category
        Category newCategory = selectCategory();
        task.setCategory(newCategory);
        
        // Update task
        taskService.updateTask(task);
        
        out.println("Task recategorized successfully!");
        enterToContinue();
    }
    
    /**
     * Delete tasks
     */
    private void deleteTasks() {
        clearScreen();
        out.println("========================================");
        out.println("             DELETE TASKS              ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            out.println("No tasks to delete.");
            enterToContinue();
            return;
        }
        
        // Display tasks
        viewTasks();
        
        out.print("Enter task number to delete (0 to cancel): ");
        int taskIndex = getInput() - 1;
        
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            out.println("Invalid task number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem task = tasks.get(taskIndex);
        out.print("Are you sure you want to delete task '" + task.getName() + "'? (y/n): ");
        String confirm = in.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            taskService.deleteTask(task.getId());
            out.println("Task deleted successfully!");
        } else {
            out.println("Delete operation cancelled.");
        }
        
        enterToContinue();
    }
    
    /**
     * Display deadline settings menu
     */
    public void printDeadlineSettingsMenu() {
        clearScreen();
        out.println("========================================");
        out.println("       DEADLINE SETTINGS MENU          ");
        out.println("========================================");
        out.println("1. Assign Deadline");
        out.println("2. View Deadlines");
        out.println("3. View Deadlines In Range");
        out.println("4. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * Deadline settings menu
     */
    public void deadlineSettingsMenu() {
        int choice;
        while (true) {
            printDeadlineSettingsMenu();
            choice = getInput();
            if (choice == -2) {
                handleInputError();
                enterToContinue();
                continue;
            }
            switch (choice) {
                case 1:
                    assignDeadline();
                    break;
                case 2:
                    viewDeadlines();
                    enterToContinue();
                    break;
                case 3:
                    viewDeadlinesInRange();
                    enterToContinue();
                    break;
                case 4:
                    return;
                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    
    /**
     * Assign deadline to a task
     */
    private void assignDeadline() {
        clearScreen();
        out.println("========================================");
        out.println("           ASSIGN DEADLINE             ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            out.println("No tasks available to assign deadlines.");
            enterToContinue();
            return;
        }
        
        // Display tasks
        viewTasks();
        
        out.print("Enter task number to assign deadline (0 to cancel): ");
        int taskIndex = getInput() - 1;
        
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            out.println("Invalid task number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem task = tasks.get(taskIndex);
        assignDeadlineToTask(task);
        
        enterToContinue();
    }
    
    /**
     * Assign deadline to a specific task
     * @param task Task to assign deadline to
     */
    private void assignDeadlineToTask(TaskmanagerItem task) {
        out.println("Current task: " + task.getName());
        if (task.getDeadline() != null) {
            out.println("Current deadline: " + dateFormat.format(task.getDeadline()));
        }
        
        out.println("Enter new deadline (format: dd/MM/yyyy HH:mm): ");
        String deadlineStr = in.nextLine().trim();
        
        try {
            Date deadline = dateFormat.parse(deadlineStr);
            task.setDeadline(deadline);
            
            // Update task
            taskService.updateTask(task);
            
            out.println("Deadline assigned successfully!");
            
            // Ask if user wants to set a reminder
            out.print("Do you want to set a reminder for this task? (y/n): ");
            String setReminder = in.nextLine().trim().toLowerCase();
            if (setReminder.equals("y") || setReminder.equals("yes")) {
                createReminderForTask(task);
            }
            
        } catch (Exception e) {
            out.println("Invalid date format. Please use dd/MM/yyyy HH:mm");
        }
    }
    
    /**
     * View all deadlines
     */
    private void viewDeadlines() {
        clearScreen();
        out.println("========================================");
        out.println("             VIEW DEADLINES            ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.sortTasksByDeadline();
        List<TaskmanagerItem> tasksWithDeadlines = new ArrayList<>();
        
        for (TaskmanagerItem task : tasks) {
            if (task.getDeadline() != null) {
                tasksWithDeadlines.add(task);
            }
        }
        
        if (tasksWithDeadlines.isEmpty()) {
            out.println("No tasks with deadlines found.");
            return;
        }
        
        for (int i = 0; i < tasksWithDeadlines.size(); i++) {
            TaskmanagerItem task = tasksWithDeadlines.get(i);
            out.println((i + 1) + ". " + task.getName() + " [" + task.getCategory().getName() + "]");
            out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
            out.println("   Priority: " + task.getPriority());
            out.println("   Completed: " + (task.isCompleted() ? "Yes" : "No"));
            out.println("----------------------------------------");
        }
    }
    
    /**
     * View deadlines in a date range
     */
    private void viewDeadlinesInRange() {
        clearScreen();
        out.println("========================================");
        out.println("        VIEW DEADLINES IN RANGE        ");
        out.println("========================================");
        
        out.println("Enter start date (format: dd/MM/yyyy): ");
        String startDateStr = in.nextLine().trim();
        
        out.println("Enter end date (format: dd/MM/yyyy): ");
        String endDateStr = in.nextLine().trim();
        
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = simpleDateFormat.parse(startDateStr);
            Date endDate = simpleDateFormat.parse(endDateStr);
            
            // Add 23:59:59 to end date to include the entire day
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            endDate = cal.getTime();
            
            List<TaskmanagerItem> tasksInRange = taskService.getTasksInDateRange(startDate, endDate);
            
            out.println("\nTasks with deadlines between " + 
                    simpleDateFormat.format(startDate) + " and " + 
                    simpleDateFormat.format(cal.getTime()) + ":");
            
            if (tasksInRange.isEmpty()) {
                out.println("No tasks found in the specified date range.");
                return;
            }
            
            for (int i = 0; i < tasksInRange.size(); i++) {
                TaskmanagerItem task = tasksInRange.get(i);
                out.println((i + 1) + ". " + task.getName() + " [" + task.getCategory().getName() + "]");
                out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
                out.println("   Priority: " + task.getPriority());
                out.println("----------------------------------------");
            }
            
        } catch (Exception e) {
            out.println("Invalid date format. Please use dd/MM/yyyy");
        }
    }
    
    /**
     * Display reminder system menu
     */
    public void printReminderSystemMenu() {
        clearScreen();
        out.println("========================================");
        out.println("        REMINDER SYSTEM MENU            ");
        out.println("========================================");
        out.println("1. Set Reminders");
        out.println("2. View Reminders");
        out.println("3. Notification Settings");
        out.println("4. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * Reminder system menu
     */
    public void reminderSystemMenu() {
        int choice;
        while (true) {
            printReminderSystemMenu();
            choice = getInput();
            if (choice == -2) {
                handleInputError();
                enterToContinue();
                continue;
            }
            switch (choice) {
                case 1:
                    setReminders();
                    break;
                case 2:
                    viewReminders();
                    enterToContinue();
                    break;
                case 3:
                    notificationSettings();
                    break;
                case 4:
                    return;
                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    
    /**
     * Set reminders for tasks
     */
    private void setReminders() {
        clearScreen();
        out.println("========================================");
        out.println("             SET REMINDERS             ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        List<TaskmanagerItem> tasksWithDeadlines = new ArrayList<>();
        
        for (TaskmanagerItem task : tasks) {
            if (task.getDeadline() != null) {
                tasksWithDeadlines.add(task);
            }
        }
        
        if (tasksWithDeadlines.isEmpty()) {
            out.println("No tasks with deadlines to set reminders for.");
            enterToContinue();
            return;
        }
        
        // Display tasks with deadlines
        for (int i = 0; i < tasksWithDeadlines.size(); i++) {
            TaskmanagerItem task = tasksWithDeadlines.get(i);
            out.println((i + 1) + ". " + task.getName());
            out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
            out.println("----------------------------------------");
        }
        
        out.print("Enter task number to set reminder for (0 to cancel): ");
        int choice = getInput();
        
        if (choice <= 0 || choice > tasksWithDeadlines.size()) {
            out.println("Invalid choice or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem selectedTask = tasksWithDeadlines.get(choice - 1);
        createReminderForTask(selectedTask);
        
        enterToContinue();
    }
    
    /**
     * View all reminders
     */
    private void viewReminders() {
        clearScreen();
        out.println("========================================");
        out.println("             VIEW REMINDERS            ");
        out.println("========================================");
        
        // This is a simplified implementation
        out.println("Reminder functionality needs to be implemented in the ReminderService");
    }
    
    /**
     * Create a reminder for a specific task
     * @param task Task to create reminder for
     */
    private void createReminderForTask(TaskmanagerItem task) {
        Date deadline = task.getDeadline();
        
        if (deadline == null) {
            out.println("This task doesn't have a deadline set. Please set a deadline first.");
            return;
        }
        
        out.println("Task: " + task.getName());
        out.println("Deadline: " + dateFormat.format(deadline));
        
        out.println("\nHow many minutes before the deadline do you want to be reminded?");
        out.println("1. 15 minutes");
        out.println("2. 30 minutes");
        out.println("3. 1 hour");
        out.println("4. 1 day");
        out.println("5. Custom time");
        
        int choice = getInput();
        int minutesBefore;
        
        switch (choice) {
            case 1:
                minutesBefore = 15;
                break;
            case 2:
                minutesBefore = 30;
                break;
            case 3:
                minutesBefore = 60;
                break;
            case 4:
                minutesBefore = 24 * 60; // 1 day
                break;
            case 5:
                out.print("Enter custom minutes before deadline: ");
                minutesBefore = getInput();
                if (minutesBefore <= 0) {
                    out.println("Invalid input. Setting to 30 minutes by default.");
                    minutesBefore = 30;
                }
                break;
            default:
                out.println("Invalid choice. Setting to 30 minutes by default.");
                minutesBefore = 30;
        }
        
        // Create reminder using service
        try {
            Reminder reminder = reminderService.createReminderBeforeDeadline(task, minutesBefore);
            out.println("Reminder set successfully for " + dateFormat.format(reminder.getReminderTime()));
        } catch (Exception e) {
            out.println("Error setting reminder: " + e.getMessage());
        }
    }
    
    /**
     * Notification settings
     */
    private void notificationSettings() {
        clearScreen();
        out.println("========================================");
        out.println("        NOTIFICATION SETTINGS          ");
        out.println("========================================");
        
        // This is a simplified implementation
        out.println("Notification settings functionality needs to be implemented");
        enterToContinue();
    }
    
    /**
     * Display task prioritization menu
     */
    public void printTaskmanagerPrioritizationMenu() {
        clearScreen();
        out.println("========================================");
        out.println("       TASK PRIORITIZATION MENU         ");
        out.println("========================================");
        out.println("1. Mark Task Importance");
        out.println("2. View Tasks by Priority");
        out.println("3. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * Task prioritization menu
     */
    public void TaskmanagerPrioritizationMenu() {
        int choice;
        while (true) {
            printTaskmanagerPrioritizationMenu();
            choice = getInput();
            if (choice == -2) {
                handleInputError();
                enterToContinue();
                continue;
            }
            switch (choice) {
                case 1:
                    markTaskPriority();
                    break;
                case 2:
                    viewTasksByPriority();
                    enterToContinue();
                    break;
                case 3:
                    return;
                default:
                    clearScreen();
                    out.println("Invalid choice. Please try again.");
                    enterToContinue();
                    break;
            }
        }
    }
    /**
     * View tasks by priority
     */
    private void viewTasksByPriority() {
        clearScreen();
        out.println("========================================");
        out.println("           TASKS BY PRIORITY           ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.sortTasksByPriority();
        
        if (tasks.isEmpty()) {
            out.println("No tasks found.");
            return;
        }
        
        out.println("--- HIGH PRIORITY TASKS ---");
        printTasksByPriority(tasks, Priority.HIGH);
        
        out.println("\n--- MEDIUM PRIORITY TASKS ---");
        printTasksByPriority(tasks, Priority.MEDIUM);
        
        out.println("\n--- LOW PRIORITY TASKS ---");
        printTasksByPriority(tasks, Priority.LOW);
    }

    /**
     * Print tasks with specific priority
     * @param tasks List of all tasks
     * @param priority Priority to filter by
     */
    private void printTasksByPriority(List<TaskmanagerItem> tasks, Priority priority) {
        boolean found = false;
        
        for (int i = 0; i < tasks.size(); i++) {
            TaskmanagerItem task = tasks.get(i);
            if (task.getPriority() == priority) {
                found = true;
                out.println((i + 1) + ". " + task.getName() + " [" + task.getCategory().getName() + "]");
                if (task.getDeadline() != null) {
                    out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
                }
                out.println("   Completed: " + (task.isCompleted() ? "Yes" : "No"));
                out.println("----------------------------------------");
            }
        }
        
        if (!found) {
            out.println("No tasks with " + priority + " priority.");
        }
    }

    /**
     * Mark task priority
     */
    private void markTaskPriority() {
        clearScreen();
        out.println("========================================");
        out.println("         MARK TASK IMPORTANCE          ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            out.println("No tasks available to mark importance.");
            enterToContinue();
            return;
        }
        
        // Display tasks
        viewTasks();
        
        out.print("Enter task number to mark importance (0 to cancel): ");
        int taskIndex = getInput() - 1;
        
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            out.println("Invalid task number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem task = tasks.get(taskIndex);
        out.println("Task: " + task.getName());
        out.println("Current priority: " + task.getPriority());
        
        out.println("\nSelect new priority:");
        out.println("1. HIGH");
        out.println("2. MEDIUM");
        out.println("3. LOW");
        
        int priorityChoice = getInput();
        
        switch (priorityChoice) {
            case 1:
                task.setPriority(Priority.HIGH);
                break;
            case 2:
                task.setPriority(Priority.MEDIUM);
                break;
            case 3:
                task.setPriority(Priority.LOW);
                break;
            default:
                out.println("Invalid choice. Priority not changed.");
                enterToContinue();
                return;
        }
        
        // Update task
        taskService.updateTask(task);
        
        out.println("Task priority updated successfully!");
        enterToContinue();
    }

    /**
     * Check for due reminders
     */
    private void checkReminders() {
        if (reminderService != null) {
            // Here we are using the ReminderService to check for due reminders
            // The observer we registered in initializeServices will be notified of any due reminders
            reminderService.checkReminders(taskService);
        }
    }

    /**
     * Show notification to user
     * @param title Notification title
     * @param message Notification message
     */
    private void showNotification(String title, String message) {
        clearScreen();
        out.println("!!! NOTIFICATION !!!");
        out.println("Title: " + title);
        out.println("Message: " + message);
        out.println("----------------------");
        enterToContinue();
    }

    /**
     * Main method for testing
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Taskmanager taskmanagerApp = new Taskmanager(scanner, System.out);
        taskmanagerApp.mainMenu("users.bin");
    }


}
    
   