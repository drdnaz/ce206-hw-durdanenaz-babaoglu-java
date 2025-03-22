package com.naz.taskmanager;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Taskmanager Manager Application
 * A console-based application for managing Taskmanagers with deadlines and reminders.
 */
public class Taskmanager {
    /** Scanner for user input */
    private final Scanner in;
    /** PrintStream for output */
    private final PrintStream out;
    /** Currently logged in user */
    private User currentUser;
    /** List of Taskmanagers for the current user */
    private List<TaskmanagerItem> Taskmanagers;
    /** List of categories */
    private List<Category> categories;
    /** List of reminders */
    private List<Reminder> reminders;
    /** User notification preferences */
    private NotificationSettings userNotificationSettings;
    /** Date format for displaying dates */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Constructor for Taskmanager class
     * @param in Scanner for user input
     * @param out PrintStream for output
     */
    public Taskmanager(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
        this.Taskmanagers = new ArrayList<>();
        this.categories = initializeCategories();
        this.reminders = new ArrayList<>();
        this.userNotificationSettings = new NotificationSettings();
    }

    /**
     * Initialize default categories
     * @return List of default categories
     */
    private List<Category> initializeCategories() {
        List<Category> defaultCategories = new ArrayList<>();
        defaultCategories.add(new Category("Work"));
        defaultCategories.add(new Category("Personal"));
        defaultCategories.add(new Category("Study"));
        defaultCategories.add(new Category("Health"));
        defaultCategories.add(new Category("Other"));
        return defaultCategories;
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
     * Display opening screen menu
     */
    public void openingScreenMenu() {
        clearScreen();
        out.println("***************************************");
        out.println("*                                     *");
        out.println("*      WELCOME TO Taskmanager MANAGER!       *");
        out.println("*                                     *");
        out.println("***************************************\n");
        out.println("=============== MAIN MENU ===============");
        out.println("1. Login");
        out.println("2. Register");
        out.println("3. Exit Program");
        out.println("=========================================");
        out.print("Please enter a number: ");
    }

    /**
     * Display main menu
     */
    public void printMainMenu() {
        clearScreen();
        out.println("========================================");
        out.println("        MAIN MENU - Taskmanager MANAGER       ");
        out.println("========================================");
        out.println("1. Create Taskmanager");
        out.println("2. Deadline Settings");
        out.println("3. Reminder System");
        out.println("4. Taskmanager Prioritization");
        out.println("5. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }

    /**
     * Display create Taskmanager menu
     */
    public void printCreateTaskmanagerMenu() {
        clearScreen();
        out.println("========================================");
        out.println("           CREATE Taskmanager MENU            ");
        out.println("========================================");
        out.println("1. Add Taskmanager");
        out.println("2. View Taskmanagers");
        out.println("3. Categorize Taskmanagers");
        out.println("4. Delete Taskmanagers");
        out.println("5. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
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
     * Display reminder system menu
     */
    public void printReminderSystemMenu() {
        clearScreen();
        out.println("========================================");
        out.println("        REMINDER SYSTEM MENU            ");
        out.println("========================================");
        out.println("1. Set Reminders");
        out.println("2. Notification Settings");
        out.println("3. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }

    /**
     * Display Taskmanager prioritization menu
     */
    public void printTaskmanagerPrioritizationMenu() {
        clearScreen();
        out.println("========================================");
        out.println("       Taskmanager PRIORITIZATION MENU         ");
        out.println("========================================");
        out.println("1. Mark Taskmanager Importance");
        out.println("2. Importance Ordering");
        out.println("3. Exit");
        out.println("========================================");
        out.print("Please enter your choice: ");
    }

    /**
     * Main menu flow
     * @param pathFileUsers Path to user data file
     */
    public void mainMenu(String pathFileUsers) {
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
                    if (loginUserMenu(pathFileUsers)) {
                        userOptionsMenu();
                    }
                    break;
                case 2:
                    clearScreen();
                    registerUserMenu(pathFileUsers);
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
     * User options menu flow
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
                    saveTaskmanagers(); // Save Taskmanagers before exiting
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
     * Create Taskmanager menu flow
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
                    addTaskmanager();
                    break;
                case 2:
                    viewTaskmanagers();
                    enterToContinue();
                    break;
                case 3:
                    categorizeTaskmanagers();
                    break;
                case 4:
                    deleteTaskmanagers();
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
     * Deadline settings menu flow
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
     * Reminder system menu flow
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
                    notificationSettings();
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
     * Taskmanager prioritization menu flow
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
                    markTaskmanagerImportance();
                    break;
                case 2:
                    reorderTaskmanagersByImportance();
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
     * Pause until user presses Enter
     */
    public void enterToContinue() {
        out.print("Press Enter to continue...");
        in.nextLine();
    }

    /**
     * Register new user
     * @param pathFileUsers Path to user data file
     */
    private void registerUserMenu(String pathFileUsers) {
        out.println("========================================");
        out.println("           REGISTER NEW USER           ");
        out.println("========================================");
        
        out.print("Enter username: ");
        String username = in.nextLine().trim();
        
        // Check if username already exists
        if (userExists(username, pathFileUsers)) {
            out.println("Username already exists. Please choose another one.");
            enterToContinue();
            return;
        }
        
        out.print("Enter password: ");
        String password = in.nextLine().trim();
        
        out.print("Enter email: ");
        String email = in.nextLine().trim();
        
        // Create new user
        User newUser = new User(username, password, email);
        
        // Save user to file
        saveUser(newUser, pathFileUsers);
        
        out.println("User registered successfully!");
        enterToContinue();
    }

    /**
     * Check if user exists
     * @param username Username to check
     * @param pathFileUsers Path to user data file
     * @return true if user exists
     */
    private boolean userExists(String username, String pathFileUsers) {
        List<User> users = loadUsers(pathFileUsers);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Save user to file
     * @param user User to save
     * @param pathFileUsers Path to user data file
     */
    private void saveUser(User user, String pathFileUsers) {
        List<User> users = loadUsers(pathFileUsers);
        users.add(user);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFileUsers))) {
            oos.writeObject(users);
        } catch (IOException e) {
            out.println("Error saving user: " + e.getMessage());
        }
    }

    /**
     * Load users from file
     * @param pathFileUsers Path to user data file
     * @return List of users
     */
    @SuppressWarnings("unchecked")
    private List<User> loadUsers(String pathFileUsers) {
        List<User> users = new ArrayList<>();
        File file = new File(pathFileUsers);
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // If file is corrupted or empty, return empty list
                out.println("Warning: Could not load user data. Starting with empty user list.");
            }
        }
        
        return users;
    }

    /**
     * Login user
     * @param pathFileUsers Path to user data file
     * @return true if login successful
     */
    private boolean loginUserMenu(String pathFileUsers) {
        out.println("========================================");
        out.println("               USER LOGIN              ");
        out.println("========================================");
        
        out.print("Enter username: ");
        String username = in.nextLine().trim();
        
        out.print("Enter password: ");
        String password = in.nextLine().trim();
        
        // Authenticate user
        User user = authenticateUser(username, password, pathFileUsers);
        if (user != null) {
            currentUser = user;
            loadUserData(); // Load user-specific data
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
     * Authenticate user
     * @param username Username
     * @param password Password
     * @param pathFileUsers Path to user data file
     * @return User object if authenticated, null otherwise
     */
    private User authenticateUser(String username, String password, String pathFileUsers) {
        List<User> users = loadUsers(pathFileUsers);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Load user-specific data
     */
    private void loadUserData() {
        if (currentUser == null) return;
        
        // Load Taskmanagers, reminders, and preferences for the current user
        Taskmanagers = loadTaskmanagers();
        reminders = loadReminders();
        userNotificationSettings = loadNotificationSettings();
        
        // Check for upcoming reminders
        checkReminders();
    }

    /**
     * Load Taskmanagers for current user
     * @return List of Taskmanagers
     */
    @SuppressWarnings("unchecked")
    private List<TaskmanagerItem> loadTaskmanagers() {
        if (currentUser == null) return new ArrayList<>();
        
        String fileName = currentUser.getUsername() + "_Taskmanagers.bin";
        File file = new File(fileName);
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<TaskmanagerItem>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                out.println("Warning: Could not load Taskmanagers. Starting with empty Taskmanager list.");
            }
        }
        
        return new ArrayList<>();
    }

    /**
     * Save Taskmanagers for current user
     */
    private void saveTaskmanagers() {
        if (currentUser == null || Taskmanagers == null) return;
        
        String fileName = currentUser.getUsername() + "_Taskmanagers.bin";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(Taskmanagers);
        } catch (IOException e) {
            out.println("Error saving Taskmanagers: " + e.getMessage());
        }
    }

    /**
     * Load reminders for current user
     * @return List of reminders
     */
    @SuppressWarnings("unchecked")
    private List<Reminder> loadReminders() {
        if (currentUser == null) return new ArrayList<>();
        
        String fileName = currentUser.getUsername() + "_reminders.bin";
        File file = new File(fileName);
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Reminder>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                out.println("Warning: Could not load reminders. Starting with empty reminder list.");
            }
        }
        
        return new ArrayList<>();
    }

    /**
     * Save reminders for current user
     */
    private void saveReminders() {
        if (currentUser == null || reminders == null) return;
        
        String fileName = currentUser.getUsername() + "_reminders.bin";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(reminders);
        } catch (IOException e) {
            out.println("Error saving reminders: " + e.getMessage());
        }
    }

    /**
     * Load notification settings for current user
     * @return Notification settings
     */
    private NotificationSettings loadNotificationSettings() {
        if (currentUser == null) return new NotificationSettings();
        
        String fileName = currentUser.getUsername() + "_settings.bin";
        File file = new File(fileName);
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (NotificationSettings) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                out.println("Warning: Could not load notification settings. Using defaults.");
            }
        }
        
        return new NotificationSettings();
    }

    /**
     * Save notification settings for current user
     */
    private void saveNotificationSettings() {
        if (currentUser == null || userNotificationSettings == null) return;
        
        String fileName = currentUser.getUsername() + "_settings.bin";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(userNotificationSettings);
        } catch (IOException e) {
            out.println("Error saving notification settings: " + e.getMessage());
        }
    }

    /**
     * Add a new Taskmanager
     */
    private void addTaskmanager() {
        clearScreen();
        out.println("========================================");
        out.println("               ADD Taskmanager                ");
        out.println("========================================");
        
        out.print("Enter Taskmanager name: ");
        String name = in.nextLine().trim();
        
        out.print("Enter Taskmanager description: ");
        String description = in.nextLine().trim();
        
        // Select category
        Category category = selectCategory();
        
    
        TaskmanagerItem Taskmanager = new TaskmanagerItem(name, description, category);
        Taskmanagers.add(Taskmanager);
        
        out.println("Taskmanager added successfully!");
        saveTaskmanagers();
        
        // Ask if user wants to add a deadline
        out.print("Do you want to add a deadline for this Taskmanager? (y/n): ");
        String addDeadline = in.nextLine().trim().toLowerCase();
        if (addDeadline.equals("y") || addDeadline.equals("yes")) {
            assignDeadlineToTaskmanager(Taskmanager);
        }
        
        enterToContinue();
    }

    /**
     * Select a category
     * @return Selected category
     */
    private Category selectCategory() {
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
     * View all Taskmanagers
     */
    private void viewTaskmanagers() {
        clearScreen();
        out.println("========================================");
        out.println("              VIEW TaskmanagerS               ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers found.");
            return;
        }
        
        for (int i = 0; i < Taskmanagers.size(); i++) {
            TaskmanagerItem Taskmanager = Taskmanagers.get(i);
            out.println((i + 1) + ". " + Taskmanager.getName() + " [" + Taskmanager.getCategory().getName() + "]");
            out.println("   Description: " + Taskmanager.getDescription());
            if (Taskmanager.getDeadline() != null) {
                out.println("   Deadline: " + dateFormat.format(Taskmanager.getDeadline()));
            }
            out.println("   Priority: " + Taskmanager.getPriority());
            out.println("   Completed: " + (Taskmanager.isCompleted() ? "Yes" : "No"));
            out.println("----------------------------------------");
        }
    }

    /**
     * Categorize Taskmanagers
     */
    private void categorizeTaskmanagers() {
        clearScreen();
        out.println("========================================");
        out.println("            CATEGORIZE TaskmanagerS           ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers to categorize.");
            enterToContinue();
            return;
        }
        
        // Display Taskmanagers
        viewTaskmanagers();
        
        out.print("Enter Taskmanager number to recategorize (0 to cancel): ");
        int TaskmanagerIndex = getInput() - 1;
        
        if (TaskmanagerIndex < 0 || TaskmanagerIndex >= Taskmanagers.size()) {
            out.println("Invalid Taskmanager number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem Taskmanager = Taskmanagers.get(TaskmanagerIndex);
        out.println("Current category: " + Taskmanager.getCategory().getName());
        
        // Select new category
        Category newCategory = selectCategory();
        Taskmanager.setCategory(newCategory);
        
        out.println("Taskmanager recategorized successfully!");
        saveTaskmanagers();
        enterToContinue();
    }

    /**
     * Delete Taskmanagers
     */
    private void deleteTaskmanagers() {
        clearScreen();
        out.println("========================================");
        out.println("             DELETE TaskmanagerS              ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers to delete.");
            enterToContinue();
            return;
        }
        
        // Display Taskmanagers
        viewTaskmanagers();
        
        out.print("Enter Taskmanager number to delete (0 to cancel): ");
        int TaskmanagerIndex = getInput() - 1;
        
        if (TaskmanagerIndex < 0 || TaskmanagerIndex >= Taskmanagers.size()) {
            out.println("Invalid Taskmanager number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem Taskmanager = Taskmanagers.get(TaskmanagerIndex);
        out.print("Are you sure you want to delete Taskmanager '" + Taskmanager.getName() + "'? (y/n): ");
        String confirm = in.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            Taskmanagers.remove(TaskmanagerIndex);
            
            // Also remove any reminders for this Taskmanager
            reminders.removeIf(r -> r.getTaskmanagerId() == TaskmanagerIndex);
            
            out.println("Taskmanager deleted successfully!");
            saveTaskmanagers();
            saveReminders();
        } else {
            out.println("Delete operation cancelled.");
        }
        
        enterToContinue();
    }

    /**
     * Assign deadline to a Taskmanager
     */
    private void assignDeadline() {
        clearScreen();
        out.println("========================================");
        out.println("           ASSIGN DEADLINE             ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers available to assign deadlines.");
            enterToContinue();
            return;
        }
        
        // Display Taskmanagers
        viewTaskmanagers();
        
        out.print("Enter Taskmanager number to assign deadline (0 to cancel): ");
        int TaskmanagerIndex = getInput() - 1;
        
        if (TaskmanagerIndex < 0 || TaskmanagerIndex >= Taskmanagers.size()) {
            out.println("Invalid Taskmanager number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem Taskmanager = Taskmanagers.get(TaskmanagerIndex);
        assignDeadlineToTaskmanager(Taskmanager);
        
        enterToContinue();
    }

    /**
     * Assign deadline to a specific Taskmanager
     * @param Taskmanager Taskmanager to assign deadline to
     */
    private void assignDeadlineToTaskmanager(TaskmanagerItem Taskmanager) {
        out.println("Current Taskmanager: " + Taskmanager.getName());
        if (Taskmanager.getDeadline() != null) {
            out.println("Current deadline: " + dateFormat.format(Taskmanager.getDeadline()));
        }
        
        out.println("Enter new deadline (format: dd/MM/yyyy HH:mm): ");
        String deadlineStr = in.nextLine().trim();
        
        try {
            Date deadline = dateFormat.parse(deadlineStr);
            Taskmanager.setDeadline(deadline);
            
            out.println("Deadline assigned successfully!");
            saveTaskmanagers();
            
            // Ask if user wants to set a reminder
            out.print("Do you want to set a reminder for this Taskmanager? (y/n): ");
            String setReminder = in.nextLine().trim().toLowerCase();
            if (setReminder.equals("y") || setReminder.equals("yes")) {
                createReminderForTaskmanager(Taskmanagers.indexOf(Taskmanager));
            }
            
        } catch (ParseException e) {
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
        
        List<TaskmanagerItem> TaskmanagersWithDeadlines = new ArrayList<>();
        for (TaskmanagerItem Taskmanager : Taskmanagers) {
            if (Taskmanager.getDeadline() != null) {
                TaskmanagersWithDeadlines.add(Taskmanager);
            }
        }
        
        if (TaskmanagersWithDeadlines.isEmpty()) {
            out.println("No Taskmanagers with deadlines found.");
            return;
        }
        
        // Sort Taskmanagers by deadline
        TaskmanagersWithDeadlines.sort(Comparator.comparing(TaskmanagerItem::getDeadline));
        
        for (int i = 0; i < TaskmanagersWithDeadlines.size(); i++) {
            TaskmanagerItem Taskmanager = TaskmanagersWithDeadlines.get(i);
            out.println((i + 1) + ". " + Taskmanager.getName() + " [" + Taskmanager.getCategory().getName() + "]");
            out.println("   Deadline: " + dateFormat.format(Taskmanager.getDeadline()));
            out.println("   Priority: " + Taskmanager.getPriority());
            out.println("   Completed: " + (Taskmanager.isCompleted() ? "Yes" : "No"));
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
            
            out.println("\nTaskmanagers with deadlines between " + 
                    simpleDateFormat.format(startDate) + " and " + 
                    simpleDateFormat.format(endDate) + ":");
            
            List<TaskmanagerItem> TaskmanagersInRange = new ArrayList<>();
            for (TaskmanagerItem Taskmanager : Taskmanagers) {
                if (Taskmanager.getDeadline() != null && 
                    Taskmanager.getDeadline().after(startDate) && 
                    Taskmanager.getDeadline().before(endDate)) {
                    TaskmanagersInRange.add(Taskmanager);
                }
            }
            
            if (TaskmanagersInRange.isEmpty()) {
                out.println("No Taskmanagers found in the specified date range.");
                return;
            }
            
            // Sort Taskmanagers by deadline
            TaskmanagersInRange.sort(Comparator.comparing(TaskmanagerItem::getDeadline));
            
            for (int i = 0; i < TaskmanagersInRange.size(); i++) {
                TaskmanagerItem Taskmanager = TaskmanagersInRange.get(i);
                out.println((i + 1) + ". " + Taskmanager.getName() + " [" + Taskmanager.getCategory().getName() + "]");
                out.println("   Deadline: " + dateFormat.format(Taskmanager.getDeadline()));
                out.println("   Priority: " + Taskmanager.getPriority());
                out.println("----------------------------------------");
            }
            
        } catch (ParseException e) {
            out.println("Invalid date format. Please use dd/MM/yyyy");
        }
    }

    /**
     * Set reminders for Taskmanagers
     */
    private void setReminders() {
        clearScreen();
        out.println("========================================");
        out.println("             SET REMINDERS             ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers available to set reminders for.");
            enterToContinue();
            return;
        }
        
     // Display Taskmanagers with deadlines
        List<TaskmanagerItem> TaskmanagersWithDeadlines = new ArrayList<>();
        for (int i = 0; i < Taskmanagers.size(); i++) {
            TaskmanagerItem Taskmanager = Taskmanagers.get(i);
            if (Taskmanager.getDeadline() != null) {
                TaskmanagersWithDeadlines.add(Taskmanager);
                out.println((TaskmanagersWithDeadlines.size()) + ". " + Taskmanager.getName());
                out.println("   Deadline: " + dateFormat.format(Taskmanager.getDeadline()));
                out.println("----------------------------------------");
            }
        }

        if (TaskmanagersWithDeadlines.isEmpty()) {
            out.println("No Taskmanagers with deadlines to set reminders for.");
            enterToContinue();
            return;
        }
        
        out.print("Enter Taskmanager number to set reminder for (0 to cancel): ");
        int choice = getInput();
        
        if (choice <= 0 || choice > TaskmanagersWithDeadlines.size()) {
            out.println("Invalid choice or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem selectedTaskmanager = TaskmanagersWithDeadlines.get(choice - 1);
        int TaskmanagerIndex = Taskmanagers.indexOf(selectedTaskmanager);
        
        createReminderForTaskmanager(TaskmanagerIndex);
        enterToContinue();
    }
    
    /**
     * Create a reminder for a specific Taskmanager
     * @param TaskmanagerIndex Index of Taskmanager in the Taskmanagers list
     */
    private void createReminderForTaskmanager(int TaskmanagerIndex) {
        TaskmanagerItem Taskmanager = Taskmanagers.get(TaskmanagerIndex);
        Date deadline = Taskmanager.getDeadline();
        
        if (deadline == null) {
            out.println("This Taskmanager doesn't have a deadline set. Please set a deadline first.");
            return;
        }
        
        out.println("Taskmanager: " + Taskmanager.getName());
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
        
        // Calculate reminder time
        Calendar cal = Calendar.getInstance();
        cal.setTime(deadline);
        cal.add(Calendar.MINUTE, -minutesBefore);
        Date reminderTime = cal.getTime();
        
        // Check if reminder time is in the past
        if (reminderTime.before(new Date())) {
            out.println("Warning: The reminder time is in the past.");
            out.print("Do you still want to create this reminder? (y/n): ");
            String confirm = in.nextLine().trim().toLowerCase();
            if (!confirm.equals("y") && !confirm.equals("yes")) {
                out.println("Reminder creation cancelled.");
                return;
            }
        }
        
        Reminder reminder = new Reminder(TaskmanagerIndex, reminderTime);
        reminders.add(reminder);
        saveReminders();
        
        out.println("Reminder set successfully for " + dateFormat.format(reminderTime));
    }
    
    /**
     * Manage notification settings
     */
    private void notificationSettings() {
        clearScreen();
        out.println("========================================");
        out.println("        NOTIFICATION SETTINGS          ");
        out.println("========================================");
        
        out.println("Current settings:");
        out.println("1. Email notifications: " + (userNotificationSettings.isEmailEnabled() ? "Enabled" : "Disabled"));
        out.println("2. App notifications: " + (userNotificationSettings.isAppNotificationsEnabled() ? "Enabled" : "Disabled"));
        out.println("3. Default reminder time: " + userNotificationSettings.getDefaultReminderMinutes() + " minutes before deadline");
        out.println("4. Back to main menu");
        
        out.print("Enter option to change (1-4): ");
        int choice = getInput();
        
        switch (choice) {
            case 1:
                userNotificationSettings.setEmailEnabled(!userNotificationSettings.isEmailEnabled());
                out.println("Email notifications: " + (userNotificationSettings.isEmailEnabled() ? "Enabled" : "Disabled"));
                break;
            case 2:
                userNotificationSettings.setAppNotificationsEnabled(!userNotificationSettings.isAppNotificationsEnabled());
                out.println("App notifications: " + (userNotificationSettings.isAppNotificationsEnabled() ? "Enabled" : "Disabled"));
                break;
            case 3:
                out.print("Enter new default reminder time (minutes before deadline): ");
                int minutes = getInput();
                if (minutes > 0) {
                    userNotificationSettings.setDefaultReminderMinutes(minutes);
                    out.println("Default reminder time set to " + minutes + " minutes before deadline");
                } else {
                    out.println("Invalid input. Setting not changed.");
                }
                break;
            case 4:
                return;
            default:
                out.println("Invalid choice.");
        }
        
        saveNotificationSettings();
        enterToContinue();
    }
    
    /**
     * Mark Taskmanager importance (priority)
     */
    private void markTaskmanagerImportance() {
        clearScreen();
        out.println("========================================");
        out.println("         MARK Taskmanager IMPORTANCE          ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers available to mark importance.");
            enterToContinue();
            return;
        }
        
        // Display Taskmanagers
        viewTaskmanagers();
        
        out.print("Enter Taskmanager number to mark importance (0 to cancel): ");
        int TaskmanagerIndex = getInput() - 1;
        
        if (TaskmanagerIndex < 0 || TaskmanagerIndex >= Taskmanagers.size()) {
            out.println("Invalid Taskmanager number or operation cancelled.");
            enterToContinue();
            return;
        }
        
        TaskmanagerItem Taskmanager = Taskmanagers.get(TaskmanagerIndex);
        out.println("Taskmanager: " + Taskmanager.getName());
        out.println("Current priority: " + Taskmanager.getPriority());
        
        out.println("\nSelect new priority:");
        out.println("1. HIGH");
        out.println("2. MEDIUM");
        out.println("3. LOW");
        
        int priorityChoice = getInput();
        
        switch (priorityChoice) {
            case 1:
                Taskmanager.setPriority(Priority.HIGH);
                break;
            case 2:
                Taskmanager.setPriority(Priority.MEDIUM);
                break;
            case 3:
                Taskmanager.setPriority(Priority.LOW);
                break;
            default:
                out.println("Invalid choice. Priority not changed.");
                enterToContinue();
                return;
        }
        
        out.println("Taskmanager priority updated successfully!");
        saveTaskmanagers();
        enterToContinue();
    }
    
    /**
     * Reorder Taskmanagers by importance (priority)
     */
    private void reorderTaskmanagersByImportance() {
        clearScreen();
        out.println("========================================");
        out.println("       REORDER TaskmanagerS BY IMPORTANCE     ");
        out.println("========================================");
        
        if (Taskmanagers.isEmpty()) {
            out.println("No Taskmanagers available to reorder.");
            enterToContinue();
            return;
        }
        
        // Sort Taskmanagers by priority (HIGH -> MEDIUM -> LOW)
        Taskmanagers.sort((t1, t2) -> {
            // First compare by priority
            int priorityCompare = t1.getPriority().compareTo(t2.getPriority());
            if (priorityCompare != 0) return priorityCompare;
            
            // If same priority, compare by deadline
            if (t1.getDeadline() != null && t2.getDeadline() != null) {
                return t1.getDeadline().compareTo(t2.getDeadline());
            } else if (t1.getDeadline() != null) {
                return -1; // t1 has deadline, t2 doesn't
            } else if (t2.getDeadline() != null) {
                return 1;  // t2 has deadline, t1 doesn't
            }
            
            // If neither has deadline, compare by name
            return t1.getName().compareTo(t2.getName());
        });
        
        out.println("Taskmanagers have been reordered by priority: HIGH -> MEDIUM -> LOW");
        out.println("Within the same priority level, Taskmanagers are ordered by deadline.");
        
        // Display reordered Taskmanagers
        viewTaskmanagers();
        
        saveTaskmanagers();
        enterToContinue();
    }
    
    /**
     * Check for due reminders
     */
    private void checkReminders() {
        Date now = new Date();
        List<Reminder> dueReminders = new ArrayList<>();
        
        for (Reminder reminder : reminders) {
            if (!reminder.isTriggered() && reminder.getReminderTime().before(now)) {
                // Reminder is due and not triggered yet
                reminder.setTriggered(true);
                
                if (reminder.getTaskmanagerId() >= 0 && reminder.getTaskmanagerId() < Taskmanagers.size()) {
                    TaskmanagerItem Taskmanager = Taskmanagers.get(reminder.getTaskmanagerId());
                    dueReminders.add(reminder);
                    showNotification("Reminder: " + Taskmanager.getName(), 
                                    "Due: " + (Taskmanager.getDeadline() != null ? 
                                            dateFormat.format(Taskmanager.getDeadline()) : "No deadline"));
                }
            }
        }
        
        // Save changes to reminders
        if (!dueReminders.isEmpty()) {
            saveReminders();
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
        Taskmanager TaskmanagerApp = new Taskmanager(scanner, System.out);
        TaskmanagerApp.mainMenu("users.bin");
    }}