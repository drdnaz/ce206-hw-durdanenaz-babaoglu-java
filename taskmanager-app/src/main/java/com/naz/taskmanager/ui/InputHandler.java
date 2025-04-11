package com.naz.taskmanager.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Class for handling user input.
 * Provides methods for input validation and conversion.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class InputHandler {
    /** Scanner for user input */
    private final Scanner scanner;
    
    /** Date format for date input/output */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    /**
     * Constructor for InputHandler
     * 
     * @param scanner Scanner for user input
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Gets string input from user
     * 
     * @param prompt Prompt message
     * @return User input as string
     */
    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Gets string input with validation
     * 
     * @param prompt Prompt message
     * @param errorMessage Error message
     * @param validator Validation function
     * @return Valid user input
     */
    public String getValidatedStringInput(String prompt, String errorMessage, 
                                         java.util.function.Predicate<String> validator) {
        while (true) {
            String input = getStringInput(prompt);
            if (validator.test(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
    
    /**
     * Gets non-empty string input
     * 
     * @param prompt Prompt message
     * @return Non-empty user input
     */
    public String getNonEmptyString(String prompt) {
        return getValidatedStringInput(
            prompt,
            "Input cannot be empty. Please try again.",
            input -> !input.isEmpty()
        );
    }
    
    /**
     * Gets integer input from user
     * 
     * @param prompt Prompt message
     * @return User input as integer
     */
    public int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Gets integer input with range validation
     * 
     * @param prompt Prompt message
     * @param min Minimum value
     * @param max Maximum value
     * @return Valid integer input
     */
    public int getIntInputInRange(String prompt, int min, int max) {
        while (true) {
            int input = getIntInput(prompt);
            if (input >= min && input <= max) {
                return input;
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }
    
    /**
     * Gets date input from user
     * 
     * @param prompt Prompt message
     * @return User input as date, null if error
     */
    public Date getDateInput(String prompt) {
        System.out.print(prompt + " (format: dd/MM/yyyy HH:mm): ");
        String dateStr = scanner.nextLine().trim();
        
        if (dateStr.isEmpty()) {
            return null;
        }
        
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm");
            return null;
        }
    }
    
    /**
     * Gets yes/no input from user
     * 
     * @param prompt Prompt message
     * @return true for yes, false for no
     */
    public boolean getYesNoInput(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'.");
        }
    }
    
    /**
     * Gets password input (masked)
     * Note: This is a simple implementation that doesn't actually mask input
     * Console masking requires different techniques based on platform
     * 
     * @param prompt Prompt message
     * @return Password as string
     */
    public String getPasswordInput(String prompt) {
        // In a real implementation, this could use java.io.Console for masking
        // but we're using Scanner for simplicity
        return getStringInput(prompt);
    }
    
    /**
     * Gets email input with basic validation
     * 
     * @param prompt Prompt message
     * @return Valid email address
     */
    public String getEmailInput(String prompt) {
        return getValidatedStringInput(
            prompt,
            "Invalid email format. Please try again.",
            email -> email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        );
    }
    
    /**
     * Gets double input from user
     * 
     * @param prompt Prompt message
     * @return User input as double
     */
    public double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Gets input from a list of options
     * 
     * @param prompt Prompt message
     * @param options Array of options
     * @return Selected option
     */
    public String getOptionInput(String prompt, String[] options) {
        System.out.println(prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        
        int choice = getIntInputInRange("Enter your choice (1-" + options.length + "): ", 1, options.length);
        return options[choice - 1];
    }
}