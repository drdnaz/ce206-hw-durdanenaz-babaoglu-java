package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTaskPanel extends JPanel {
    private JTextField taskNameField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox;
    private JSpinner deadlineSpinner;
    private JButton saveButton;
    private JButton clearButton;
    
    public AddTaskPanel() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        // Form alanları
        taskNameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        // Öncelik seçenekleri
        String[] priorities = {"Low", "Medium", "High"};
        priorityComboBox = new JComboBox<>(priorities);
        
        // Tarih seçici
        SpinnerDateModel dateModel = new SpinnerDateModel();
        deadlineSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(deadlineSpinner, "dd/MM/yyyy HH:mm");
        deadlineSpinner.setEditor(dateEditor);
        
        // Butonlar
        saveButton = new JButton("Save Task");
        clearButton = new JButton("Clear Form");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Form paneli
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form alanlarını ekle
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Task Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(taskNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priorityComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Deadline:"), gbc);
        gbc.gridx = 1;
        formPanel.add(deadlineSpinner, gbc);
        
        // Buton paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        // Ana panele bileşenleri ekle
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Form verilerini almak için metod
    public String getTaskName() {
        return taskNameField.getText();
    }
    
    public String getDescription() {
        return descriptionArea.getText();
    }
    
    public String getPriority() {
        return (String) priorityComboBox.getSelectedItem();
    }
    
    public Date getDeadline() {
        return (Date) deadlineSpinner.getValue();
    }
    
    // Formu temizlemek için metod
    public void clearForm() {
        taskNameField.setText("");
        descriptionArea.setText("");
        priorityComboBox.setSelectedIndex(0);
        deadlineSpinner.setValue(new Date());
    }
} 