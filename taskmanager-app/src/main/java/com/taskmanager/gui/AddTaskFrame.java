package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import com.taskmanager.model.Task;

/**
 * Frame for adding a new task
 */
public class AddTaskFrame extends JFrame {
    private JTextField taskTitleField;
    private JTextArea taskDescriptionArea;
    private JSpinner dateSpinner;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> priorityComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    
    // UI Color scheme
    private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
    private final Color ACCENT_COLOR = Color.PINK;
    private final Color TEXT_COLOR = new Color(180, 200, 255);
    private final Color FIELD_TEXT_COLOR = new Color(80, 80, 80);
    
    private final MainMenuFrame parentFrame;
    
    /**
     * Creates a new AddTaskFrame
     * @param parentFrame The parent MainMenuFrame
     */
    public AddTaskFrame(MainMenuFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Add New Task");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        
        // GridBagConstraints - common settings
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title label
        JLabel titleLabel = new JLabel("Add New Task");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        GridBagConstraints gbcTitleLabel = new GridBagConstraints();
        gbcTitleLabel.gridx = 0;
        gbcTitleLabel.gridy = 0;
        gbcTitleLabel.gridwidth = 2;
        gbcTitleLabel.insets = new Insets(10, 10, 20, 10);
        gbcTitleLabel.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbcTitleLabel);
        
        // Task Title Label
        JLabel taskTitleLabel = new JLabel("Task Title:");
        taskTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        taskTitleLabel.setForeground(TEXT_COLOR);
        GridBagConstraints gbcTaskTitleLabel = new GridBagConstraints();
        gbcTaskTitleLabel.gridx = 0;
        gbcTaskTitleLabel.gridy = 1;
        gbcTaskTitleLabel.anchor = GridBagConstraints.WEST;
        gbcTaskTitleLabel.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(taskTitleLabel, gbcTaskTitleLabel);
        
        // Task Title Field
        taskTitleField = new JTextField(20);
        taskTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskTitleField.setForeground(FIELD_TEXT_COLOR);
        taskTitleField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        GridBagConstraints gbcTaskTitleField = new GridBagConstraints();
        gbcTaskTitleField.gridx = 0;
        gbcTaskTitleField.gridy = 2;
        gbcTaskTitleField.gridwidth = 2;
        gbcTaskTitleField.fill = GridBagConstraints.HORIZONTAL;
        gbcTaskTitleField.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(taskTitleField, gbcTaskTitleField);
        
        // Task Description Label
        JLabel taskDescLabel = new JLabel("Task Description:");
        taskDescLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        taskDescLabel.setForeground(TEXT_COLOR);
        GridBagConstraints gbcTaskDescLabel = new GridBagConstraints();
        gbcTaskDescLabel.gridx = 0;
        gbcTaskDescLabel.gridy = 3;
        gbcTaskDescLabel.anchor = GridBagConstraints.WEST;
        gbcTaskDescLabel.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(taskDescLabel, gbcTaskDescLabel);
        
        // Task Description Area
        taskDescriptionArea = new JTextArea(5, 20);
        taskDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskDescriptionArea.setForeground(FIELD_TEXT_COLOR);
        taskDescriptionArea.setLineWrap(true);
        taskDescriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(taskDescriptionArea);
        scrollPane.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        GridBagConstraints gbcScrollPane = new GridBagConstraints();
        gbcScrollPane.gridx = 0;
        gbcScrollPane.gridy = 4;
        gbcScrollPane.gridwidth = 2;
        gbcScrollPane.fill = GridBagConstraints.BOTH;
        gbcScrollPane.insets = new Insets(0, 10, 10, 10);
        gbcScrollPane.weightx = 1.0;
        gbcScrollPane.weighty = 1.0;
        mainPanel.add(scrollPane, gbcScrollPane);
        
        // Due Date Label
        JLabel dueDateLabel = new JLabel("Due Date:");
        dueDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dueDateLabel.setForeground(TEXT_COLOR);
        GridBagConstraints gbcDueDateLabel = new GridBagConstraints();
        gbcDueDateLabel.gridx = 0;
        gbcDueDateLabel.gridy = 5;
        gbcDueDateLabel.anchor = GridBagConstraints.WEST;
        gbcDueDateLabel.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(dueDateLabel, gbcDueDateLabel);
        
        // Date Spinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateModel.setCalendarField(Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateSpinner.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        GridBagConstraints gbcDateSpinner = new GridBagConstraints();
        gbcDateSpinner.gridx = 0;
        gbcDateSpinner.gridy = 6;
        gbcDateSpinner.gridwidth = 2;
        gbcDateSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbcDateSpinner.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(dateSpinner, gbcDateSpinner);
        
        // Category Label
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryLabel.setForeground(TEXT_COLOR);
        GridBagConstraints gbcCategoryLabel = new GridBagConstraints();
        gbcCategoryLabel.gridx = 0;
        gbcCategoryLabel.gridy = 7;
        gbcCategoryLabel.anchor = GridBagConstraints.WEST;
        gbcCategoryLabel.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(categoryLabel, gbcCategoryLabel);
        
        // Category Combo Box
        String[] categories = {"Work", "Personal", "Study", "Health", "Other"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setForeground(FIELD_TEXT_COLOR);
        categoryComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        GridBagConstraints gbcCategoryCombo = new GridBagConstraints();
        gbcCategoryCombo.gridx = 0;
        gbcCategoryCombo.gridy = 8;
        gbcCategoryCombo.fill = GridBagConstraints.HORIZONTAL;
        gbcCategoryCombo.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(categoryComboBox, gbcCategoryCombo);
        
        // Priority Label
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priorityLabel.setForeground(TEXT_COLOR);
        GridBagConstraints gbcPriorityLabel = new GridBagConstraints();
        gbcPriorityLabel.gridx = 1;
        gbcPriorityLabel.gridy = 7;
        gbcPriorityLabel.anchor = GridBagConstraints.WEST;
        gbcPriorityLabel.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(priorityLabel, gbcPriorityLabel);
        
        // Priority Combo Box
        String[] priorities = {"Low", "Medium", "High", "Urgent"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityComboBox.setForeground(FIELD_TEXT_COLOR);
        priorityComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        GridBagConstraints gbcPriorityCombo = new GridBagConstraints();
        gbcPriorityCombo.gridx = 1;
        gbcPriorityCombo.gridy = 8;
        gbcPriorityCombo.fill = GridBagConstraints.HORIZONTAL;
        gbcPriorityCombo.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(priorityComboBox, gbcPriorityCombo);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        GridBagConstraints gbcButtonPanel = new GridBagConstraints();
        gbcButtonPanel.gridx = 0;
        gbcButtonPanel.gridy = 9;
        gbcButtonPanel.gridwidth = 2;
        gbcButtonPanel.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(buttonPanel, gbcButtonPanel);
        
        // Save Button
        saveButton = new JButton("Save");
        styleButton(saveButton);
        buttonPanel.add(saveButton);
        
        // Cancel Button
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        addActionListeners();
    }
    
    /**
     * Style button with consistent look and feel
     * @param button Button to style
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(80, 80, 80));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(ACCENT_COLOR, 3, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
    }
    
    /**
     * Add action listeners to buttons
     */
    private void addActionListeners() {
        // Save button action
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    saveTask();
                }
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Validate the form fields
     * @return true if validation passes, false otherwise
     */
    private boolean validateForm() {
        if (taskTitleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a task title.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            taskTitleField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Save the task to the database
     */
    private void saveTask() {
        try {
            String title = taskTitleField.getText().trim();
            String description = taskDescriptionArea.getText().trim();
            Date dueDate = (Date) dateSpinner.getValue();
            String category = (String) categoryComboBox.getSelectedItem();
            String priority = (String) priorityComboBox.getSelectedItem();
            
            // Create a new Task object and add it to the static list
            Task newTask = new Task(title, description, dueDate, category, priority);
            Task.addTask(newTask);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dueDateStr = dateFormat.format(dueDate);
            
            JOptionPane.showMessageDialog(this,
                "Task successfully saved:\n" +
                "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Due Date: " + dueDateStr + "\n" +
                "Category: " + category + "\n" +
                "Priority: " + priority,
                "Task Saved",
                JOptionPane.INFORMATION_MESSAGE);
                
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving task: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Getter methods for tests
    public JTextField getTaskTitleField() {
        return taskTitleField;
    }
    
    public JTextArea getTaskDescriptionArea() {
        return taskDescriptionArea;
    }
    
    public JSpinner getDateSpinner() {
        return dateSpinner;
    }
    
    public JComboBox<String> getCategoryComboBox() {
        return categoryComboBox;
    }
    
    public JComboBox<String> getPriorityComboBox() {
        return priorityComboBox;
    }
}
