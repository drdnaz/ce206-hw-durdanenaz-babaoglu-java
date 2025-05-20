package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.naz.taskmanager.repository.UserRepository;
import java.awt.Window.Type;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;

/**
 * Login Frame for the application
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserRepository userRepository;
    
    // UI Color scheme
    private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
    private final Color ACCENT_COLOR = Color.PINK;
    private final Color TEXT_COLOR = new Color(180, 200, 255);
    private final Color FIELD_TEXT_COLOR = new Color(80, 80, 80);
    
    /**
     * Creates the Login Frame
     */
    public LoginFrame() {
    	setType(Type.UTILITY);
        userRepository = new UserRepository(System.out);
        initComponents();
    }
    
    /**
     * Initialize the components
     */
    private void initComponents() {
        setTitle("Task Manager - Login");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        
        // Title panel with logo
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(76, 40, 233, 40);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setOpaque(false);
        
        // Logo image
        JLabel logoLabel = new JLabel();
        try {
            // Load image from resources
            ClassLoader classLoader = getClass().getClassLoader();
            ImageIcon originalIcon = new ImageIcon(classLoader.getResource("logo.png"));
            // Resize the image to appropriate size
            Image scaledImg = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
        mainPanel.setLayout(null);
        titlePanel.add(logoLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spacing
        
        // Title label
        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel);
        
        // Slogan label
        JLabel sloganLabel = new JLabel("Smart Task Management at Your Fingertips");
        sloganLabel.setBounds(19, 116, 337, 23);
        sloganLabel.setFont(new Font("Bell MT", Font.ITALIC, 20));
        sloganLabel.setForeground(Color.PINK);
        mainPanel.add(sloganLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setBounds(116, 170, 146, 20);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(TEXT_COLOR);
        mainPanel.add(subtitleLabel);
        
        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 204, 72, 20);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        mainPanel.add(usernameLabel);
        
        // Username Field
        usernameField = new JTextField(20);
        usernameField.setBounds(30, 229, 326, 24);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setForeground(FIELD_TEXT_COLOR);
        usernameField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        mainPanel.add(usernameField);
        
        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 283, 68, 20);
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        mainPanel.add(passwordLabel);
        
        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setBounds(30, 308, 326, 24);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(FIELD_TEXT_COLOR);
        passwordField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
        mainPanel.add(passwordField);
        
        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(70, 362, 246, 40);
        styleButton(loginButton);
        mainPanel.add(loginButton);
        
        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(70, 422, 246, 21);
        styleButton(registerButton);
        mainPanel.add(registerButton);
        
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
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    login();
                }
            }
        });
        
        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
    }
    
    /**
     * Validate the form fields
     * @return true if validation passes, false otherwise
     */
    private boolean validateForm() {
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your username.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return false;
        }
        
        if (passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your password.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Login to the application
     */
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // For demonstration, any non-empty username/password combination is valid
        if (!username.isEmpty() && !password.isEmpty()) {
            dispose();
            MainMenuFrame mainMenuFrame = new MainMenuFrame(username);
            mainMenuFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Main method to run the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }

    private void showRegisterDialog() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }
    public JPasswordField getPasswordField() {
        return passwordField;
    }
} 