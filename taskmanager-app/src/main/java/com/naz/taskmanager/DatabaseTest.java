package com.naz.taskmanager;

import com.naz.taskmanager.repository.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection(System.out);
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Örnek bir kullanıcı ekleme
            String insertUser = "INSERT INTO Users (username, password, email) VALUES ('testuser', 'testpass', 'test@example.com')";
            stmt.execute(insertUser);
            
            // Kullanıcıları sorgulama
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Email: " + rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}