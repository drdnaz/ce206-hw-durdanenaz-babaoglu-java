package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TaskListPanel extends JPanel {
    private JTable taskTable;
    private JScrollPane scrollPane;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton editButton;
    
    public TaskListPanel() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        // Tablo modeli
        String[] columnNames = {"ID", "Task Name", "Description", "Deadline", "Priority", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        // Tablo
        taskTable = new JTable(model);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Scroll pane
        scrollPane = new JScrollPane(taskTable);
        
        // Butonlar
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete");
        editButton = new JButton("Edit");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Buton paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Ana panele bileşenleri ekle
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Tablo modelini güncellemek için metod
    public void updateTaskTable(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        model.setRowCount(0); // Mevcut verileri temizle
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
} 