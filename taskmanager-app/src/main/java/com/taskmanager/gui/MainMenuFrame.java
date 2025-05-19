package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private String username;

    public MainMenuFrame(String username) {
        this.username = username;
        setTitle("Task Manager - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(555, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        // İçerik paneli: başlık, welcome, slogan ve butonlar için beyaz arka plan
        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(400, 650));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(18, 18, 0, 18);
        int row = 0;

        // Title
        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setForeground(new Color(30, 40, 60));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 1;
        gbcTitle.anchor = GridBagConstraints.CENTER;
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        gbcTitle.insets = new Insets(18, 18, 0, 18);
        contentPanel.add(titleLabel, gbcTitle);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(241, 196, 15));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcWelcome = new GridBagConstraints();
        gbcWelcome.gridx = 0;
        gbcWelcome.gridy = 1;
        gbcWelcome.gridwidth = 1;
        gbcWelcome.anchor = GridBagConstraints.CENTER;
        gbcWelcome.fill = GridBagConstraints.HORIZONTAL;
        gbcWelcome.insets = new Insets(0, 18, 0, 18);
        contentPanel.add(welcomeLabel, gbcWelcome);

        // Slogan
        JLabel sloganLabel = new JLabel("Plan smart. Work better.");
        sloganLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        sloganLabel.setForeground(new Color(52, 73, 94));
        sloganLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcSlogan = new GridBagConstraints();
        gbcSlogan.gridx = 0;
        gbcSlogan.gridy = 2;
        gbcSlogan.gridwidth = 1;
        gbcSlogan.anchor = GridBagConstraints.CENTER;
        gbcSlogan.fill = GridBagConstraints.HORIZONTAL;
        gbcSlogan.insets = new Insets(0, 18, 0, 18);
        contentPanel.add(sloganLabel, gbcSlogan);

        Dimension buttonSize = new Dimension(260, 48);
        Insets buttonInsets = new Insets(12, 0, 12, 0);

        // Add Task button
        JButton addTaskButton = new JButton("Add Task");
        styleMenuButton(addTaskButton, buttonSize, buttonInsets);
        GridBagConstraints gbcAddTask = new GridBagConstraints();
        gbcAddTask.gridx = 0;
        gbcAddTask.gridy = 3;
        gbcAddTask.gridwidth = 1;
        gbcAddTask.anchor = GridBagConstraints.CENTER;
        gbcAddTask.fill = GridBagConstraints.HORIZONTAL;
        gbcAddTask.insets = buttonInsets;
        contentPanel.add(addTaskButton, gbcAddTask);

        // View Task button
        JButton viewTaskButton = new JButton("View Task");
        styleMenuButton(viewTaskButton, buttonSize, buttonInsets);
        GridBagConstraints gbcViewTask = new GridBagConstraints();
        gbcViewTask.gridx = 0;
        gbcViewTask.gridy = 4;
        gbcViewTask.gridwidth = 1;
        gbcViewTask.anchor = GridBagConstraints.CENTER;
        gbcViewTask.fill = GridBagConstraints.HORIZONTAL;
        gbcViewTask.insets = buttonInsets;
        contentPanel.add(viewTaskButton, gbcViewTask);

        // Delete Task button
        JButton deleteTaskButton = new JButton("Delete Task");
        styleMenuButton(deleteTaskButton, buttonSize, buttonInsets);
        GridBagConstraints gbcDeleteTask = new GridBagConstraints();
        gbcDeleteTask.gridx = 0;
        gbcDeleteTask.gridy = 5;
        gbcDeleteTask.gridwidth = 1;
        gbcDeleteTask.anchor = GridBagConstraints.CENTER;
        gbcDeleteTask.fill = GridBagConstraints.HORIZONTAL;
        gbcDeleteTask.insets = buttonInsets;
        contentPanel.add(deleteTaskButton, gbcDeleteTask);

        // Edit Task button
        JButton editTaskButton = new JButton("Edit Task");
        styleMenuButton(editTaskButton, buttonSize, buttonInsets);
        GridBagConstraints gbcEditTask = new GridBagConstraints();
        gbcEditTask.gridx = 0;
        gbcEditTask.gridy = 6;
        gbcEditTask.gridwidth = 1;
        gbcEditTask.anchor = GridBagConstraints.CENTER;
        gbcEditTask.fill = GridBagConstraints.HORIZONTAL;
        gbcEditTask.insets = buttonInsets;
        contentPanel.add(editTaskButton, gbcEditTask);

        // Manage Categories button
        JButton manageCategoriesButton = new JButton("Manage Categories");
        styleMenuButton(manageCategoriesButton, buttonSize, buttonInsets);
        GridBagConstraints gbcManageCategories = new GridBagConstraints();
        gbcManageCategories.gridx = 0;
        gbcManageCategories.gridy = 7;
        gbcManageCategories.gridwidth = 1;
        gbcManageCategories.anchor = GridBagConstraints.CENTER;
        gbcManageCategories.fill = GridBagConstraints.HORIZONTAL;
        gbcManageCategories.insets = buttonInsets;
        contentPanel.add(manageCategoriesButton, gbcManageCategories);

        // Search button
        JButton searchButton = new JButton("Search");
        styleMenuButton(searchButton, buttonSize, buttonInsets);
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.gridx = 0;
        gbcSearch.gridy = 8;
        gbcSearch.gridwidth = 1;
        gbcSearch.anchor = GridBagConstraints.CENTER;
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;
        gbcSearch.insets = buttonInsets;
        contentPanel.add(searchButton, gbcSearch);

        // View Product button
        JButton viewProductButton = new JButton("View Product");
        styleMenuButton(viewProductButton, buttonSize, buttonInsets);
        GridBagConstraints gbcViewProduct = new GridBagConstraints();
        gbcViewProduct.gridx = 0;
        gbcViewProduct.gridy = 9;
        gbcViewProduct.gridwidth = 1;
        gbcViewProduct.anchor = GridBagConstraints.CENTER;
        gbcViewProduct.fill = GridBagConstraints.HORIZONTAL;
        gbcViewProduct.insets = buttonInsets;
        contentPanel.add(viewProductButton, gbcViewProduct);

        // Exit button
        JButton exitButton = new JButton("Exit");
        styleMenuButton(exitButton, buttonSize, new Insets(30, 0, 20, 0));
        GridBagConstraints gbcExit = new GridBagConstraints();
        gbcExit.gridx = 0;
        gbcExit.gridy = 10;
        gbcExit.gridwidth = 1;
        gbcExit.anchor = GridBagConstraints.CENTER;
        gbcExit.fill = GridBagConstraints.HORIZONTAL;
        gbcExit.insets = new Insets(30, 0, 20, 0);
        contentPanel.add(exitButton, gbcExit);

        // Ana panel: koyu arka plan ve içerik panelini ortala
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(30, 40, 60));
        GridBagConstraints gbcContainer = new GridBagConstraints();
        gbcContainer.gridx = 0;
        gbcContainer.gridy = 0;
        gbcContainer.weightx = 1.0;
        gbcContainer.weighty = 1.0;
        gbcContainer.anchor = GridBagConstraints.CENTER;
        containerPanel.add(contentPanel, gbcContainer);
        return containerPanel;
    }

    private void styleMenuButton(JButton button, Dimension size, Insets insets) {
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 3, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setEnabled(false);
        button.setPreferredSize(size);
        button.setMargin(insets);
    }
}
