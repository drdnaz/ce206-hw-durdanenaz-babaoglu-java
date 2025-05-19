package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.naz.taskmanager.repository.UserRepository;

public class ModernLoginPanel extends JPanel {
    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginButton;
    public JButton registerButton;
    public LoginListener loginListener; // Login sonrası açılacak panel için listener
    private UserRepository userRepository;

    public ModernLoginPanel() {
        userRepository = new UserRepository(System.out);
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 40, 60)); // Koyu arka plan

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Paneli ortalamak için iç panel
        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setOpaque(false);
        GridBagConstraints igbc = new GridBagConstraints();
        igbc.insets = new Insets(8, 8, 8, 8);
        igbc.fill = GridBagConstraints.HORIZONTAL;
        igbc.gridx = 0;
        igbc.anchor = GridBagConstraints.CENTER;
        igbc.gridwidth = 2;

        // İkon
        JLabel iconLabel = new JLabel("\u2714");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 44));
        iconLabel.setForeground(new Color(255, 90, 90));
        igbc.gridy = 0;
        innerPanel.add(iconLabel, igbc);

        // TaskManager başlığı (tam ortada)
        JLabel titleLabel = new JLabel("TaskManager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        igbc.gridy = 1;
        innerPanel.add(titleLabel, igbc);

        // Slogan (tam ortada)
        JLabel sloganLabel = new JLabel("Plan smart. Work better.");
        sloganLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        sloganLabel.setForeground(new Color(180, 200, 255));
        sloganLabel.setHorizontalAlignment(SwingConstants.CENTER);
        igbc.gridy = 2;
        innerPanel.add(sloganLabel, igbc);

        // Login başlığı (tam ortada)
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 22));
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        igbc.gridy = 3;
        innerPanel.add(loginLabel, igbc);

        // Form paneli (tam ortada, alt alta)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(6, 6, 6, 6);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.gridx = 0;
        fgbc.gridy = 0;
        fgbc.anchor = GridBagConstraints.EAST;

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(userLabel, fgbc);

        fgbc.gridx = 1; fgbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(16);
        formPanel.add(usernameField, fgbc);

        fgbc.gridy = 1; fgbc.gridx = 0; fgbc.anchor = GridBagConstraints.EAST;
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(passLabel, fgbc);

        fgbc.gridx = 1; fgbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(16);
        formPanel.add(passwordField, fgbc);

        // Butonlar paneli (tam ortada)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.insets = new Insets(0, 8, 0, 8);
        bgbc.gridx = 0; bgbc.gridy = 0;
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBackground(new Color(70, 130, 220));
        loginButton.setForeground(new Color(30, 80, 180));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(30, 80, 180), 3, true));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(90, 150, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(70, 130, 220));
            }
        });
        buttonPanel.add(loginButton, bgbc);

        bgbc.gridx = 1;
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setBackground(new Color(60, 200, 120));
        registerButton.setForeground(new Color(20, 120, 60));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(20, 120, 60), 3, true));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(90, 230, 150));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(60, 200, 120));
            }
        });
        buttonPanel.add(registerButton, bgbc);

        // Register butonuna tıklanınca dialog aç
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });

        // Login butonuna tıklanınca veritabanı kontrolü ve yönlendirme
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(ModernLoginPanel.this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (userRepository.validateUser(username, password)) {
                    // Başarılı girişte TaskListPanel aç
                    JFrame taskFrame = new JFrame("Task List");
                    taskFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    taskFrame.setContentPane(new TaskListPanel());
                    taskFrame.setSize(800, 600);
                    taskFrame.setLocationRelativeTo(null);
                    taskFrame.setVisible(true);
                    SwingUtilities.getWindowAncestor(ModernLoginPanel.this).dispose();
                } else {
                    JOptionPane.showMessageDialog(ModernLoginPanel.this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Form panelini ve buton panelini alt alta ekle
        igbc.gridy = 4; igbc.gridx = 0; igbc.gridwidth = 2;
        innerPanel.add(formPanel, igbc);
        igbc.gridy = 5;
        innerPanel.add(buttonPanel, igbc);

        // Paneli ortalamak için ana panelde tek child olarak ekle
        add(innerPanel, gbc);
    }

    // Register işlemini veritabanına bağladım
    private void showRegisterDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 40, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        JTextField regUserField = new JTextField(14);
        panel.add(regUserField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        JPasswordField regPassField = new JPasswordField(14);
        panel.add(regPassField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel confLabel = new JLabel("Confirm:");
        confLabel.setForeground(Color.WHITE);
        panel.add(confLabel, gbc);
        gbc.gridx = 1;
        JPasswordField regConfField = new JPasswordField(14);
        panel.add(regConfField, gbc);

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

    // Login sonrası başka bir panel açmak için listener arayüzü
    public interface LoginListener {
        void onLogin(String username, String password);
    }
} 