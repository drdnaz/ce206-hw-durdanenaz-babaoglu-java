package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.naz.taskmanager.repository.UserRepository;
import java.awt.Window.Type;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserRepository userRepository;

    public LoginFrame() {
    	setType(Type.UTILITY);
        userRepository = new UserRepository(System.out);
        setTitle("Task Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(561, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 40, 60));
        mainPanel.setLayout(null);
        JPanel innerPanel = new JPanel();
        innerPanel.setBounds(45, 10, 490, 452);
        innerPanel.setOpaque(false);
                innerPanel.setLayout(null);
        
                // Başlık
                JLabel titleLabel = new JLabel("Task Manager ");
                titleLabel.setBounds(-68, 72, 517, 37);
                titleLabel.setBackground(new Color(240, 240, 240));
                titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
                titleLabel.setForeground(Color.WHITE);
                innerPanel.add(titleLabel);
        
                // Slogan
                JLabel sloganLabel = new JLabel("\"Plan smart. Work better.\"");
                sloganLabel.setBounds(253, 119, 180, 22);
                sloganLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                sloganLabel.setForeground(new Color(180, 200, 255));
                sloganLabel.setHorizontalAlignment(SwingConstants.CENTER);
                innerPanel.add(sloganLabel);
        
                // Login başlığı
                JLabel loginLabel = new JLabel("Login");
                loginLabel.setBounds(233, 201, 61, 26);
                loginLabel.setFont(new Font("Arial", Font.BOLD, 22));
                loginLabel.setForeground(Color.WHITE);
                loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
                innerPanel.add(loginLabel);

        // Form paneli
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBounds(92, 237, 341, 74);
        formPanel.setOpaque(false);

        // Username label
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        GridBagConstraints gbcUserLabel = new GridBagConstraints();
        gbcUserLabel.gridx = 0;
        gbcUserLabel.gridy = 0;
        gbcUserLabel.insets = new Insets(5, 5, 5, 5);
        gbcUserLabel.anchor = GridBagConstraints.EAST;
        formPanel.add(userLabel, gbcUserLabel);
        
                // Username field
                usernameField = new JTextField(16);
                GridBagConstraints gbc_usernameField = new GridBagConstraints();
                gbc_usernameField.insets = new Insets(0, 0, 5, 0);
                gbc_usernameField.gridx = 1;
                gbc_usernameField.gridy = 0;
                formPanel.add(usernameField, gbc_usernameField);

        // Password label
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        GridBagConstraints gbcPassLabel = new GridBagConstraints();
        gbcPassLabel.gridx = 0;
        gbcPassLabel.gridy = 1;
        gbcPassLabel.insets = new Insets(5, 5, 0, 5);
        gbcPassLabel.anchor = GridBagConstraints.EAST;
        formPanel.add(passLabel, gbcPassLabel);

        // Password field
        passwordField = new JPasswordField(16);
        GridBagConstraints gbcPassField = new GridBagConstraints();
        gbcPassField.gridx = 1;
        gbcPassField.gridy = 1;
        gbcPassField.insets = new Insets(5, 5, 0, 0);
        gbcPassField.anchor = GridBagConstraints.WEST;
        gbcPassField.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbcPassField);
        innerPanel.add(formPanel);
        mainPanel.add(innerPanel);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\tugba\\OneDrive\\Desktop\\ce206-hw-durdanenaz-babaoglu-java\\taskmanager-app\\src\\main\\resources\\logoolan.png"));
        lblNewLabel.setBounds(0, 59, 226, 156);
        innerPanel.add(lblNewLabel);
        
                // Login butonu
                loginButton = new JButton("Login");
                loginButton.setBounds(178, 321, 79, 30);
                innerPanel.add(loginButton);
                loginButton.setFont(new Font("Arial", Font.BOLD, 20));
                loginButton.setBackground(new Color(70, 130, 220));
                loginButton.setForeground(new Color(30, 80, 180));
                loginButton.setFocusPainted(false);
                loginButton.setBorder(BorderFactory.createLineBorder(new Color(30, 80, 180), 3, true));
                loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String password = new String(passwordField.getPassword());
                        if (username.isEmpty() || password.isEmpty()) {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (userRepository.validateUser(username, password)) {
                            JFrame mainFrame = new JFrame("Task Manager");
                            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            mainFrame.setSize(900, 600);
                            mainFrame.setLocationRelativeTo(null);
                            mainFrame.setVisible(true);
                            LoginFrame.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                
                        // Register butonu
                        registerButton = new JButton("Register");
                        registerButton.setBounds(269, 321, 106, 30);
                        innerPanel.add(registerButton);
                        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
                        registerButton.setBackground(new Color(60, 200, 120));
                        registerButton.setForeground(new Color(20, 120, 60));
                        registerButton.setFocusPainted(false);
                        registerButton.setBorder(BorderFactory.createLineBorder(new Color(20, 120, 60), 3, true));
                        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        registerButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                showRegisterDialog();
                            }
                        });

        return mainPanel;
    }

    private void showRegisterDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 40, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Register başlığı
        JLabel registerTitle = new JLabel("Register");
        registerTitle.setFont(new Font("Arial", Font.BOLD, 24));
        registerTitle.setForeground(Color.WHITE);
        registerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerTitle, gbc);
        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        JTextField regUserField = new JTextField(16);
        panel.add(regUserField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        JPasswordField regPassField = new JPasswordField(16);
        panel.add(regPassField, gbc);

        // Confirm
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel confLabel = new JLabel("Confirm:");
        confLabel.setForeground(Color.WHITE);
        panel.add(confLabel, gbc);
        gbc.gridx = 1;
        JPasswordField regConfField = new JPasswordField(16);
        panel.add(regConfField, gbc);

        // Dialogu daha büyük aç
        panel.setPreferredSize(new Dimension(420, 300));

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Register", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = regUserField.getText();
            String password = new String(regPassField.getPassword());
            String confirm = new String(regConfField.getPassword());
            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (userRepository.userExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (userRepository.addUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
} 