package com.naz.taskmanager.util;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Utility class for console operations.
 * Provides helper methods for console input/output operations and UI formatting.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class ConsoleUtils {
    
    /**
     * Clears the console screen.
     * Uses ANSI escape codes and line feeds for different console environments.
     * 
     * @param out PrintStream for output
     */
    public static void clearScreen(PrintStream out) {
        out.print("\033[H\033[2J");
        out.flush();
        
        // Alternative for Windows
        for (int i = 0; i < 50; i++) {
            out.println();
        }
    }
    
    /**
     * Waits for user to press Enter before continuing.
     * 
     * @param in Scanner for input
     * @param out PrintStream for output
     */
    public static void enterToContinue(Scanner in, PrintStream out) {
        out.print("Press Enter to continue...");
        in.nextLine();
    }
    
    /**
     * Gets integer input from user.
     * 
     * @param in Scanner for input
     * @return User input as integer, -2 if error
     */
    public static int getIntInput(Scanner in) {
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            return -2;
        }
    }
    
    /**
     * Displays error message for invalid input.
     * 
     * @param out PrintStream for output
     */
    public static void handleInputError(PrintStream out) {
        out.println("Invalid input. Please enter a number.");
    }
    
    /**
     * Prints a formatted menu header.
     * 
     * @param out PrintStream for output
     * @param title Menu title
     */
    public static void printMenuHeader(PrintStream out, String title) {
        out.println("========================================");
        out.println("           " + title + "           ");
        out.println("========================================");
    }
    
    /**
     * Prints a menu footer.
     * 
     * @param out PrintStream for output
     */
    public static void printMenuFooter(PrintStream out) {
        out.println("========================================");
    }
    
    /**
     * Shows a notification to the user.
     * 
     * @param out PrintStream for output
     * @param title Notification title
     * @param message Notification message
     */
    public static void showNotification(PrintStream out, String title, String message) {
        clearScreen(out);
        out.println("!!! NOTIFICATION !!!");
        out.println("Title: " + title);
        out.println("Message: " + message);
        out.println("----------------------");
    }
    
    /**
     * Prints a progress bar to visualize completion.
     * 
     * @param out PrintStream for output
     * @param current Current progress
     * @param total Total to reach
     * @param barLength Length of the progress bar
     */
    public static void printProgressBar(PrintStream out, int current, int total, int barLength) {
        int progress = (int) (((double) current / total) * barLength);
        out.print("[");
        for (int i = 0; i < barLength; i++) {
            if (i < progress) {
                out.print("=");
            } else if (i == progress) {
                out.print(">");
            } else {
                out.print(" ");
            }
        }
        out.print("] " + current + "/" + total);
    }
    
    /**
     * Formats text with color using ANSI escape codes.
     * 
     * @param text Text to format
     * @param colorCode ANSI color code
     * @return Formatted text
     */
    public static String colorText(String text, String colorCode) {
        return colorCode + text + "\u001B[0m";
    }
    
    /**
     * Gets string input with confirmation to ensure user entered correct data.
     * 
     * @param in Scanner for input
     * @param out PrintStream for output
     * @param prompt Prompt message
     * @return Confirmed input string
     */
    public static String getConfirmedInput(Scanner in, PrintStream out, String prompt) {
        String input;
        while (true) {
            out.print(prompt);
            input = in.nextLine().trim();
            
            out.print("You entered: '" + input + "'. Is this correct? (y/n): ");
            String confirm = in.nextLine().trim().toLowerCase();
            
            if (confirm.equals("y") || confirm.equals("yes")) {
                return input;
            }
            
            out.println("Let's try again.");
        }
    }
}