package com.naz.taskmanager;

import com.naz.taskmanager.repository.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database bağlantı testi için örnek sınıf.
 */
public class DatabaseTest {

    /**
     * Ana metod - veritabanı bağlantısını test eder.
     *
     * @param args komut satırı argümanları
     */
    public static void main(String[] args) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance(System.out);
        Connection connection = dbConnection.getConnection();
        
        if (connection != null) {
            System.out.println("Veritabanı bağlantısı başarıyla kuruldu.");
            
            try {
                Statement stmt = connection.createStatement();
                System.out.println("Veritabanı işlemleri yapılabilir.");
                stmt.close();
            } catch (SQLException e) {
                System.err.println("SQL hatası: " + e.getMessage());
            } finally {
                dbConnection.closeConnection();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            }
        } else {
            System.err.println("Veritabanı bağlantısı kurulamadı!");
        }
    }
}