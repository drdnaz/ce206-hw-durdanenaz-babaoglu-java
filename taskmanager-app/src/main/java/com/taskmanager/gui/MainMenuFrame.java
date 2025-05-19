package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.TaskService;

/**
 * Main Menu Screen
 * Displayed after successful login.
 */
public class MainMenuFrame extends JFrame {
    private JLabel welcomeLabel;
    private JButton addTaskButton;
    private JButton viewTaskButton;
    private JButton deleteTaskButton;
    private JButton editTaskButton;
    private JButton manageCategoriesButton;
    private JButton searchButton;
    private JButton exitButton;
    
    private String username;
    private TaskService taskService;
    
    // Common border color for all buttons
    private final Color BUTTON_BORDER_COLOR = new Color(59, 89, 152); // Navy blue

    /**
     * Creates the Main Menu Frame
     * @param username Name of the logged-in user
     */
    public MainMenuFrame(String username) {
        this.username = username;
        initComponents();
    }
    
    /**
     * Initializes components
     */
    private void initComponents() {
        setTitle("Task Manager - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main content panel with null layout for absolute positioning
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(22, 34, 52));
        mainPanel.setLayout(null);
        setContentPane(mainPanel);
        
        // Title at top left
        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(53, 30, 250, 30);
        mainPanel.add(titleLabel);
        
        // Slogan below title
        JLabel sloganLabel = new JLabel("\"Plan smart. Work better.\"");
        sloganLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        sloganLabel.setForeground(new Color(180, 200, 255));
        sloganLabel.setBounds(53, 60, 250, 25);
        mainPanel.add(sloganLabel);
        
        // Welcome message at top right
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        welcomeLabel.setForeground(new Color(180, 200, 255));
        welcomeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        welcomeLabel.setBounds(400, 30, 230, 30);
        mainPanel.add(welcomeLabel);
        
        // First row of buttons
        // Add Task
        addTaskButton = new JButton("Add Task");
        styleButton(addTaskButton);
        addTaskButton.setBounds(75, 120, 160, 90);
        mainPanel.add(addTaskButton);
        
        // View Tasks
        viewTaskButton = new JButton("View Tasks");
        styleButton(viewTaskButton);
        viewTaskButton.setBounds(265, 120, 160, 90);
        mainPanel.add(viewTaskButton);
        
        // Delete Task
        deleteTaskButton = new JButton("Delete Task");
        styleButton(deleteTaskButton);
        deleteTaskButton.setBounds(455, 120, 160, 90);
        mainPanel.add(deleteTaskButton);
        
        // Second row of buttons
        // Edit Task
        editTaskButton = new JButton("Edit Task");
        styleButton(editTaskButton);
        editTaskButton.setBounds(75, 235, 160, 90);
        mainPanel.add(editTaskButton);
        
        // Manage Categories
        manageCategoriesButton = new JButton("Manage Categories");
        styleButton(manageCategoriesButton);
        manageCategoriesButton.setBounds(265, 235, 160, 90);
        mainPanel.add(manageCategoriesButton);
        
        // Search
        searchButton = new JButton("Search");
        styleButton(searchButton);
        searchButton.setBounds(455, 235, 160, 90);
        mainPanel.add(searchButton);
        
        // Exit button at bottom center
        exitButton = new JButton("Exit");
        styleButton(exitButton);
        exitButton.setBounds(265, 350, 160, 70);
        mainPanel.add(exitButton);
        
        // Add button actions
        addButtonActions();
    }
    
    /**
     * Applies consistent styling to buttons
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(80, 80, 80));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BUTTON_BORDER_COLOR, 3, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Adds button actions
     */
    private void addButtonActions() {
        // Add Task button
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenuFrame.this, 
                    "Add task functionality not implemented yet.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // View Tasks button
        viewTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenuFrame.this, 
                    "View tasks functionality not implemented yet.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Delete Task button
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenuFrame.this, 
                    "Delete task functionality not implemented yet.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Edit Task button
        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenuFrame.this, 
                    "Edit task functionality not implemented yet.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Manage Categories button
        manageCategoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenuFrame.this, 
                    "Category management functionality not implemented yet.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenuFrame.this, 
                    "Search functionality not implemented yet.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(MainMenuFrame.this,
                    "Are you sure you want to exit?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginFrame().setVisible(true);
                }
            }
        });
    }
    
    /**
     * Test method
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainMenuFrame frame = new MainMenuFrame("User");
            frame.setVisible(true);
        });
    }
}
