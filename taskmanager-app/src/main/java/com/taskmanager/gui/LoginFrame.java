package com.taskmanager.gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public ModernLoginPanel modernLoginPanel;

    public LoginFrame() {
        setTitle("Task Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        modernLoginPanel = new ModernLoginPanel();
        setContentPane(modernLoginPanel);
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