package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    // GUI bileşenleri
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private TaskListPanel taskListPanel;
    private AddTaskPanel addTaskPanel;
    private JPanel settingsPanel;
    
    public MainFrame() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        // Ana panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Alt paneller
        taskListPanel = new TaskListPanel();
        addTaskPanel = new AddTaskPanel();
        settingsPanel = new JPanel();
        
        // Tabbed pane'e panelleri ekle
        tabbedPane.addTab("Task List", taskListPanel);
        tabbedPane.addTab("Add Task", addTaskPanel);
        tabbedPane.addTab("Settings", settingsPanel);
        
        // Ana pencere ayarları
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void setupLayout() {
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void setupEventHandlers() {
        // Event handler'lar buraya eklenecek
    }
} 