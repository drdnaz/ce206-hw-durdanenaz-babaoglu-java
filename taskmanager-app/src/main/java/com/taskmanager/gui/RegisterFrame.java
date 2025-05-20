package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.naz.taskmanager.repository.UserRepository;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton registerButton;
    private UserRepository userRepository;
    
    // UI Color scheme
    private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
    private final Color ACCENT_COLOR = Color.PINK;
    private final Color TEXT_COLOR = new Color(220, 220, 255);
    private final Color FIELD_BACKGROUND = Color.WHITE;
    private final Color BUTTON_BACKGROUND = new Color(0, 150, 50);
    private final Color BUTTON_TEXT = Color.WHITE;

    public RegisterFrame() {
        userRepository = new UserRepository(System.out);
        setTitle("Register");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        // Main panel with navy blue background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Title panel at the top
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(20, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel in the center
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        
        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.EAST;
        c1.insets = new Insets(10, 5, 10, 15);
        c1.weightx = 0.3;
        formPanel.add(usernameLabel, c1);
        
        // Username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 1;
        c2.gridy = 0;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(10, 5, 10, 5);
        c2.weightx = 0.7;
        formPanel.add(usernameField, c2);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridx = 0;
        c3.gridy = 1;
        c3.anchor = GridBagConstraints.EAST;
        c3.insets = new Insets(10, 5, 10, 15);
        c3.weightx = 0.3;
        formPanel.add(passwordLabel, c3);
        
        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        GridBagConstraints c4 = new GridBagConstraints();
        c4.gridx = 1;
        c4.gridy = 1;
        c4.fill = GridBagConstraints.HORIZONTAL;
        c4.insets = new Insets(10, 5, 10, 5);
        c4.weightx = 0.7;
        formPanel.add(passwordField, c4);
        
        // Confirm label
        JLabel confirmLabel = new JLabel("Confirm:");
        confirmLabel.setForeground(TEXT_COLOR);
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        GridBagConstraints c5 = new GridBagConstraints();
        c5.gridx = 0;
        c5.gridy = 2;
        c5.anchor = GridBagConstraints.EAST;
        c5.insets = new Insets(10, 5, 10, 15);
        c5.weightx = 0.3;
        formPanel.add(confirmLabel, c5);
        
        // Confirm field
        confirmField = new JPasswordField(20);
        confirmField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        GridBagConstraints c6 = new GridBagConstraints();
        c6.gridx = 1;
        c6.gridy = 2;
        c6.fill = GridBagConstraints.HORIZONTAL;
        c6.insets = new Insets(10, 5, 10, 5);
        c6.weightx = 0.7;
        formPanel.add(confirmField, c6);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        registerButton = new JButton("Register");
        registerButton.setEnabled(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(BUTTON_BACKGROUND);
        registerButton.setForeground(BUTTON_TEXT);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BUTTON_BACKGROUND.darker(), 2),
            new EmptyBorder(8, 30, 8, 30)
        ));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userRepository.userExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username already exists!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userRepository.addUser(username, password)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Registration failed!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            RegisterFrame frame = new RegisterFrame();
            frame.setVisible(true);
        });
    }
} 