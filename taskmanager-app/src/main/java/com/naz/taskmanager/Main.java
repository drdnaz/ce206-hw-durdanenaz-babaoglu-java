// Main.java (düzeltilmiş)
package com.naz.taskmanager;

import com.naz.taskmanager.repository.DatabaseConnection;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance(System.out);
        dbConnection.initializeDatabase(); // SQLite tablolarını oluştur
    }
}