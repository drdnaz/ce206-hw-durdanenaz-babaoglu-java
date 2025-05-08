package com.naz.taskmanager.ui.gui;

import com.naz.taskmanager.model.*;
import com.naz.taskmanager.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main application window for Task Manager.
 * Provides GUI interface for task management operations.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class MainFrame extends JFrame {
    private final User currentUser;
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final DeadlineService deadlineService;
    
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JComboBox<Category> categoryFilter;
    private JComboBox<Priority> priorityFilter;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private boolean sortAscending = true;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public MainFrame(User user) {
        this.currentUser = user;
        this.taskService = new TaskService(user.getUsername());
        this.categoryService = new CategoryService();
        this.deadlineService = new DeadlineService(taskService);
        
        initializeUI();
        loadTasks();
    }
    
    private void initializeUI() {
        setTitle("Task Manager - " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create toolbar
        JToolBar toolBar = createToolBar();
        mainPanel.add(toolBar, BorderLayout.NORTH);
        
        // Create task table
        createTaskTable();
        JScrollPane scrollPane = new JScrollPane(taskTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton completeButton = new JButton("Mark Complete");
        JButton statsButton = new JButton("Statistics");
        JButton categoryButton = new JButton("Manage Categories");
        
        // Add action listeners
        addButton.addActionListener(e -> showAddTaskDialog());
        editButton.addActionListener(e -> editSelectedTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        completeButton.addActionListener(e -> markTaskComplete());
        statsButton.addActionListener(e -> showStatisticsDialog());
        categoryButton.addActionListener(e -> showCategoryManagementDialog());
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(completeButton);
        toolBar.add(statsButton);
        toolBar.add(categoryButton);
        toolBar.addSeparator();
        
        // Add search field
        toolBar.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTasks(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTasks(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTasks(); }
        });
        toolBar.add(searchField);
        
        // Add sort options
        toolBar.addSeparator();
        toolBar.add(new JLabel("Sort by:"));
        sortComboBox = new JComboBox<>(new String[]{
            "Name", "Deadline", "Priority", "Category", "Status"
        });
        sortComboBox.addActionListener(e -> sortTasks());
        toolBar.add(sortComboBox);
        
        JButton sortDirectionButton = new JButton("↑");
        sortDirectionButton.addActionListener(e -> {
            sortAscending = !sortAscending;
            sortDirectionButton.setText(sortAscending ? "↑" : "↓");
            sortTasks();
        });
        toolBar.add(sortDirectionButton);
        
        toolBar.addSeparator();
        
        // Add filters
        toolBar.add(new JLabel("Category:"));
        categoryFilter = new JComboBox<>(new Category[]{
            new Category("All"),
            new Category("Work"),
            new Category("Personal"),
            new Category("Study"),
            new Category("Health")
        });
        categoryFilter.addActionListener(e -> filterTasks());
        toolBar.add(categoryFilter);
        
        toolBar.add(new JLabel("Priority:"));
        priorityFilter = new JComboBox<>(new Priority[]{
            null,
            Priority.HIGH,
            Priority.MEDIUM,
            Priority.LOW
        });
        priorityFilter.addActionListener(e -> filterTasks());
        toolBar.add(priorityFilter);
        
        return toolBar;
    }
    
    private void createTaskTable() {
        String[] columnNames = {"Name", "Description", "Category", "Priority", "Deadline", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.getTableHeader().setReorderingAllowed(false);
        
        // Add double-click listener for editing
        taskTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedTask();
                }
            }
        });
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        JLabel statusLabel = new JLabel("Ready");
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        // Add task count
        JLabel taskCountLabel = new JLabel("Total Tasks: 0");
        statusBar.add(taskCountLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    private void loadTasks() {
        tableModel.setRowCount(0);
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        for (TaskmanagerItem task : tasks) {
            Object[] row = {
                task.getName(),
                task.getDescription(),
                task.getCategory().getName(),
                task.getPriority(),
                task.getDeadline() != null ? dateFormat.format(task.getDeadline()) : "No deadline",
                task.isCompleted() ? "Completed" : "Pending"
            };
            tableModel.addRow(row);
        }
        
        updateStatusBar();
    }
    
    private void filterTasks() {
        String searchText = searchField.getText().toLowerCase();
        Category selectedCategory = (Category) categoryFilter.getSelectedItem();
        Priority selectedPriority = (Priority) priorityFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        for (TaskmanagerItem task : tasks) {
            boolean matchesSearch = searchText.isEmpty() ||
                task.getName().toLowerCase().contains(searchText) ||
                task.getDescription().toLowerCase().contains(searchText) ||
                task.getCategory().getName().toLowerCase().contains(searchText);
                
            boolean matchesCategory = selectedCategory == null || 
                selectedCategory.getName().equals("All") || 
                task.getCategory().getName().equals(selectedCategory.getName());
                
            boolean matchesPriority = selectedPriority == null || 
                task.getPriority() == selectedPriority;
            
            if (matchesSearch && matchesCategory && matchesPriority) {
                Object[] row = {
                    task.getName(),
                    task.getDescription(),
                    task.getCategory().getName(),
                    task.getPriority(),
                    task.getDeadline() != null ? dateFormat.format(task.getDeadline()) : "No deadline",
                    task.isCompleted() ? "Completed" : "Pending"
                };
                tableModel.addRow(row);
            }
        }
        
        sortTasks();
        updateStatusBar();
    }
    
    private void sortTasks() {
        String sortBy = (String) sortComboBox.getSelectedItem();
        final int columnIndex;
        switch (sortBy) {
            case "Name": columnIndex = 0; break;
            case "Deadline": columnIndex = 4; break;
            case "Priority": columnIndex = 3; break;
            case "Category": columnIndex = 2; break;
            case "Status": columnIndex = 5; break;
            default: columnIndex = -1;
        }
        if (columnIndex != -1) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            sorter.setComparator(columnIndex, (java.util.Comparator<Object>) (o1, o2) -> {
                if (o1 == null && o2 == null) return 0;
                if (o1 == null) return sortAscending ? -1 : 1;
                if (o2 == null) return sortAscending ? 1 : -1;
                if (columnIndex == 4) { // Deadline column
                    try {
                        Date date1 = dateFormat.parse(o1.toString());
                        Date date2 = dateFormat.parse(o2.toString());
                        return sortAscending ? date1.compareTo(date2) : date2.compareTo(date1);
                    } catch (Exception e) {
                        return sortAscending ? o1.toString().compareTo(o2.toString()) 
                                           : o2.toString().compareTo(o1.toString());
                    }
                }
                return sortAscending ? o1.toString().compareTo(o2.toString()) 
                                   : o2.toString().compareTo(o1.toString());
            });
            taskTable.setRowSorter(sorter);
        }
    }
    
    private void updateStatusBar() {
        try {
            Container contentPane = getContentPane();
            if (contentPane != null && contentPane.getLayout() instanceof BorderLayout) {
                Component comp = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
                if (comp != null && comp instanceof JPanel) {
                    JPanel statusBar = (JPanel) comp;
                    if (statusBar.getLayout() instanceof BorderLayout) {
                        Component taskCountComp = ((BorderLayout) statusBar.getLayout()).getLayoutComponent(BorderLayout.EAST);
                        if (taskCountComp != null && taskCountComp instanceof JLabel) {
                            JLabel taskCountLabel = (JLabel) taskCountComp;
                            taskCountLabel.setText("Total Tasks: " + tableModel.getRowCount());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Silently handle any errors with status bar update
            System.err.println("Error updating status bar: " + e.getMessage());
        }
    }
    
    private void showAddTaskDialog() {
        JDialog dialog = new JDialog(this, "Add New Task", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        // Description field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        panel.add(descScroll, gbc);
        
        // Category field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JComboBox<Category> categoryCombo = new JComboBox<>(new Category[]{
            new Category("Work"),
            new Category("Personal"),
            new Category("Study"),
            new Category("Health")
        });
        panel.add(categoryCombo, gbc);
        
        // Priority field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Priority:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
        panel.add(priorityCombo, gbc);
        
        // Deadline field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Deadline:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);
        panel.add(dateSpinner, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descArea.getText();
            Category category = (Category) categoryCombo.getSelectedItem();
            Priority priority = (Priority) priorityCombo.getSelectedItem();
            Date deadline = (Date) dateSpinner.getValue();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter a task name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Use the createTask method from TaskService
                TaskmanagerItem task = taskService.createTask(name, description, category);
                task.setPriority(priority);
                task.setDeadline(deadline);
                // Update after setting priority and deadline
                taskService.updateTask(task);
                
                // Reset filters to show all tasks
                searchField.setText("");
                categoryFilter.setSelectedIndex(0); // Select "All" category
                priorityFilter.setSelectedIndex(0); // Select null priority
                
                // Now load tasks without filtering
                loadTasks();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error creating task: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    // Helper method to find a task by name
    private TaskmanagerItem findTaskByName(String name) {
        for (TaskmanagerItem task : taskService.getAllTasks()) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }
    
    private void editSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a task to edit",
                "No Task Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Get the task from the service
        String taskName = (String) tableModel.getValueAt(selectedRow, 0);
        TaskmanagerItem task = findTaskByName(taskName);
        if (task == null) {
            JOptionPane.showMessageDialog(this,
                "Task not found",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit Task", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField nameField = new JTextField(task.getName(), 20);
        panel.add(nameField, gbc);
        
        // Description field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextArea descArea = new JTextArea(task.getDescription(), 3, 20);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        panel.add(descScroll, gbc);
        
        // Category field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JComboBox<Category> categoryCombo = new JComboBox<>(new Category[]{
            new Category("Work"),
            new Category("Personal"),
            new Category("Study"),
            new Category("Health")
        });
        categoryCombo.setSelectedItem(task.getCategory());
        panel.add(categoryCombo, gbc);
        
        // Priority field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Priority:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setSelectedItem(task.getPriority());
        panel.add(priorityCombo, gbc);
        
        // Deadline field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Deadline:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(task.getDeadline() != null ? task.getDeadline() : new Date());
        panel.add(dateSpinner, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descArea.getText();
            Category category = (Category) categoryCombo.getSelectedItem();
            Priority priority = (Priority) priorityCombo.getSelectedItem();
            Date deadline = (Date) dateSpinner.getValue();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter a task name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            task.setName(name);
            task.setDescription(description);
            task.setCategory(category);
            task.setPriority(priority);
            task.setDeadline(deadline);
            
            taskService.updateTask(task);
            loadTasks();
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a task to delete",
                "No Task Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this task?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String taskName = (String) tableModel.getValueAt(selectedRow, 0);
            TaskmanagerItem task = findTaskByName(taskName);
            if (task != null) {
                taskService.deleteTask(task.getId());
                loadTasks();
            }
        }
    }
    
    private void markTaskComplete() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a task to mark as complete",
                "No Task Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String taskName = (String) tableModel.getValueAt(selectedRow, 0);
        TaskmanagerItem task = findTaskByName(taskName);
        if (task != null) {
            task.setCompleted(true);
            taskService.updateTask(task);
            loadTasks();
        }
    }
    
    private void showStatisticsDialog() {
        JDialog dialog = new JDialog(this, "Task Statistics", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        // Total tasks
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Total Tasks:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(tasks.size())), gbc);
        
        // Completed tasks
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Completed Tasks:"), gbc);
        
        gbc.gridx = 1;
        long completedTasks = tasks.stream().filter(TaskmanagerItem::isCompleted).count();
        panel.add(new JLabel(String.valueOf(completedTasks)), gbc);
        
        // Pending tasks
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Pending Tasks:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(tasks.size() - completedTasks)), gbc);
        
        // Category distribution
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Tasks by Category:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        JPanel categoryPanel = new JPanel(new GridLayout(0, 1));
        Map<Category, Long> categoryCount = tasks.stream()
            .collect(Collectors.groupingBy(TaskmanagerItem::getCategory, Collectors.counting()));
        categoryCount.forEach((category, count) -> 
            categoryPanel.add(new JLabel(category.getName() + ": " + count)));
        panel.add(categoryPanel, gbc);
        
        // Priority distribution
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Tasks by Priority:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        JPanel priorityPanel = new JPanel(new GridLayout(0, 1));
        Map<Priority, Long> priorityCount = tasks.stream()
            .collect(Collectors.groupingBy(TaskmanagerItem::getPriority, Collectors.counting()));
        priorityCount.forEach((priority, count) -> 
            priorityPanel.add(new JLabel(priority + ": " + count)));
        panel.add(priorityPanel, gbc);
        
        // Upcoming deadlines
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Upcoming Deadlines:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        JPanel deadlinePanel = new JPanel(new GridLayout(0, 1));
        Date now = new Date();
        tasks.stream()
            .filter(task -> !task.isCompleted() && task.getDeadline() != null && task.getDeadline().after(now))
            .sorted(Comparator.comparing(TaskmanagerItem::getDeadline))
            .limit(5)
            .forEach(task -> deadlinePanel.add(new JLabel(
                task.getName() + " - " + dateFormat.format(task.getDeadline()))));
        panel.add(deadlinePanel, gbc);
        
        // Overdue tasks
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Overdue Tasks:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        JPanel overduePanel = new JPanel(new GridLayout(0, 1));
        tasks.stream()
            .filter(task -> !task.isCompleted() && task.getDeadline() != null && task.getDeadline().before(now))
            .forEach(task -> overduePanel.add(new JLabel(
                task.getName() + " - " + dateFormat.format(task.getDeadline()))));
        panel.add(overduePanel, gbc);
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(closeButton, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showCategoryManagementDialog() {
        JDialog dialog = new JDialog(this, "Manage Categories", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        // Category list
        DefaultListModel<Category> listModel = new DefaultListModel<>();
        JList<Category> categoryList = new JList<>(listModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(categoryList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Load existing categories
        List<Category> categories = categoryService.getAllCategories();
        categories.forEach(listModel::addElement);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton closeButton = new JButton("Close");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add category
        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(dialog, "Enter category name:");
            if (name != null && !name.trim().isEmpty()) {
                Category newCategory = new Category(name.trim());
                categoryService.addCategory(newCategory);
                listModel.addElement(newCategory);
                updateCategoryFilters();
            }
        });
        
        // Edit category
        editButton.addActionListener(e -> {
            Category selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                String newName = JOptionPane.showInputDialog(dialog, 
                    "Enter new name for category:", 
                    selectedCategory.getName());
                    
                if (newName != null && !newName.trim().isEmpty()) {
                    selectedCategory.setName(newName.trim());
                    categoryService.updateCategory(selectedCategory);
                    listModel.set(categoryList.getSelectedIndex(), selectedCategory);
                    updateCategoryFilters();
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Please select a category to edit",
                    "No Category Selected",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Delete category
        deleteButton.addActionListener(e -> {
            Category selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to delete this category?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    categoryService.deleteCategory(selectedCategory);
                    listModel.removeElement(selectedCategory);
                    updateCategoryFilters();
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Please select a category to delete",
                    "No Category Selected",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Close dialog
        closeButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void updateCategoryFilters() {
        List<Category> categories = categoryService.getAllCategories();
        Category[] categoryArray = new Category[categories.size() + 1];
        categoryArray[0] = new Category("All");
        for (int i = 0; i < categories.size(); i++) {
            categoryArray[i + 1] = categories.get(i);
        }
        
        categoryFilter.removeAllItems();
        for (Category category : categoryArray) {
            categoryFilter.addItem(category);
        }
    }
} 