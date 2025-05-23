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
 * @version 1.1
 */
public class TaskmanagerApp {

    /**
     * Application entry point
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Initializing database...");
        DatabaseConnection.getInstance(System.out).initializeDatabase();

        String mode = parseArguments(args);
        if ("console".equals(mode)) {
            startConsoleApp();
        } else if ("gui".equals(mode)) {
            startGuiApp();
        } else {
            System.out.println("Invalid or missing argument. Usage: --console or --gui");
        }
    }

    /**
     * Parses the command-line arguments.
     *
     * @param args Command-line arguments
     * @return "console", "gui", or "invalid"
     */
    public static String parseArguments(String[] args) {
        if (args.length > 0) {
            if ("--console".equals(args[0])) {
                return "console";
            } else if ("--gui".equals(args[0])) {
                return "gui";
            }
        }
        return "invalid";
    }

    /**
     * Start console application
     */
    public static void startConsoleApp() {
        Scanner scanner = new Scanner(System.in);
        Taskmanager taskmanagerApp = new Taskmanager(scanner, System.out);
        taskmanagerApp.mainMenu();
        scanner.close();
        DatabaseConnection.getInstance(System.out).closeConnection();
    }

    /**
     * Start GUI application
     */
    public static void startGuiApp() {
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
