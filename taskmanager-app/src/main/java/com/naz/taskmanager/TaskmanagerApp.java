package com.naz.taskmanager;

import java.util.Scanner;
import com.naz.taskmanager.repository.DatabaseConnection;
import com.taskmanager.gui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application class.
 * Contains the application entry point and initializes the application components.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class TaskmanagerApp {

    /**
     * Application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Initialize database
        System.out.println("Initializing database...");
        DatabaseConnection.getInstance(System.out).initializeDatabase();

        // Check command line arguments
        if (args.length > 0) {
            if (args[0].equals("--console")) {
                startConsoleApp();
            } else if (args[0].equals("--gui")) {
                startGuiApp();
            } else {
                System.out.println("Invalid argument. Usage: --console or --gui");
                System.exit(1);
            }
        } else {
            System.out.println("Please select a mode: --console or --gui");
            System.exit(1);
        }
    }

    /**
     * Start console application
     */
    private static void startConsoleApp() {
        Scanner scanner = new Scanner(System.in);
        Taskmanager taskmanagerApp = new Taskmanager(scanner, System.out);
        taskmanagerApp.mainMenu();
        scanner.close();
        DatabaseConnection.getInstance(System.out).closeConnection();
    }

    /**
     * Start GUI application
     */
    private static void startGuiApp() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
