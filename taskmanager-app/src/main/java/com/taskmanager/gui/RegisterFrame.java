package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.naz.taskmanager.repository.UserRepository;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton registerButton;
    private UserRepository userRepository;

    public RegisterFrame() {
        userRepository = new UserRepository(System.out);
        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 340);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 40, 60));

        // Register title
        JLabel registerTitle = new JLabel("Register");
        registerTitle.setFont(new Font("Arial", Font.BOLD, 24));
        registerTitle.setForeground(Color.WHITE);
        registerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2;
        gbcTitle.insets = new Insets(10, 10, 10, 10);
        gbcTitle.anchor = GridBagConstraints.CENTER;
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        panel.add(registerTitle, gbcTitle);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        GridBagConstraints gbcUserLabel = new GridBagConstraints();
        gbcUserLabel.gridx = 0;
        gbcUserLabel.gridy = 1;
        gbcUserLabel.insets = new Insets(10, 10, 10, 10);
        gbcUserLabel.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbcUserLabel);

        usernameField = new JTextField(16);
        GridBagConstraints gbcUserField = new GridBagConstraints();
        gbcUserField.gridx = 1;
        gbcUserField.gridy = 1;
        gbcUserField.insets = new Insets(10, 10, 10, 10);
        gbcUserField.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbcUserField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        GridBagConstraints gbcPassLabel = new GridBagConstraints();
        gbcPassLabel.gridx = 0;
        gbcPassLabel.gridy = 2;
        gbcPassLabel.insets = new Insets(10, 10, 10, 10);
        gbcPassLabel.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbcPassLabel);

        passwordField = new JPasswordField(16);
        GridBagConstraints gbcPassField = new GridBagConstraints();
        gbcPassField.gridx = 1;
        gbcPassField.gridy = 2;
        gbcPassField.insets = new Insets(10, 10, 10, 10);
        gbcPassField.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbcPassField);

        // Confirm
        JLabel confLabel = new JLabel("Confirm:");
        confLabel.setForeground(Color.WHITE);
        GridBagConstraints gbcConfLabel = new GridBagConstraints();
        gbcConfLabel.gridx = 0;
        gbcConfLabel.gridy = 3;
        gbcConfLabel.insets = new Insets(10, 10, 10, 10);
        gbcConfLabel.anchor = GridBagConstraints.EAST;
        panel.add(confLabel, gbcConfLabel);

        confirmField = new JPasswordField(16);
        GridBagConstraints gbcConfField = new GridBagConstraints();
        gbcConfField.gridx = 1;
        gbcConfField.gridy = 3;
        gbcConfField.insets = new Insets(10, 10, 10, 10);
        gbcConfField.fill = GridBagConstraints.HORIZONTAL;
        panel.add(confirmField, gbcConfField);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        registerButton.setBackground(new Color(60, 200, 120));
        registerButton.setForeground(new Color(20, 120, 60));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(20, 120, 60), 3, true));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        GridBagConstraints gbcRegisterBtn = new GridBagConstraints();
        gbcRegisterBtn.gridx = 0;
        gbcRegisterBtn.gridy = 4;
        gbcRegisterBtn.gridwidth = 2;
        gbcRegisterBtn.insets = new Insets(10, 10, 10, 10);
        gbcRegisterBtn.anchor = GridBagConstraints.CENTER;
        gbcRegisterBtn.fill = GridBagConstraints.HORIZONTAL;
        panel.add(registerButton, gbcRegisterBtn);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirm = new String(confirmField.getPassword());
                if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (userRepository.userExists(username)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (userRepository.addUser(username, password)) {
                        JOptionPane.showMessageDialog(RegisterFrame.this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(RegisterFrame.this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        return panel;
    }
} 