// TaskmanagerAppTest.java (düzeltilmiş)
package com.naz.taskmanager;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;  // Eksik import eklendi
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.naz.taskmanager.repository.DatabaseConnection;

public class TaskmanagerAppTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private ByteArrayInputStream inContent;
    private InputStream originalIn;  // Doğru tip: InputStream

    @Before
    public void setUp() throws Exception {
        // Redirect standard output to capture it
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        // Redirect standard input to provide test input
        String input = "3\n"; // Input to exit the application
        inContent = new ByteArrayInputStream(input.getBytes());
        originalIn = System.in;  // System.in zaten bir InputStream nesnesidir
        System.setIn(inContent);
    }

    @After
    public void tearDown() throws Exception {
        // Restore original standard output and input
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testMainMethodExecution() {
        // Call main method which should initialize the database and start the Taskmanager
        TaskmanagerApp.main(new String[]{});
        
        // Check if expected output messages are present
        String output = outContent.toString();
        assertTrue("Should initialize database", output.contains("Initializing database"));
        assertTrue("Should start application", output.contains("Starting TaskManager application"));
        assertTrue("Should close application", output.contains("TaskManager application closed"));
    }
    
    @Test
    public void testDatabaseInitialization() {
        // Create test instance of DatabaseConnection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance(System.out);
        
        // Initialize database
        dbConnection.initializeDatabase();
        
        // Check if connection is successful by getting connection
        assertNotNull(dbConnection.getConnection());
        
        // Close connection
        dbConnection.closeConnection();
    }
    
    @Test
    public void testSingletonPattern() {
        // Get two instances of the database connection
        DatabaseConnection instance1 = DatabaseConnection.getInstance(System.out);
        DatabaseConnection instance2 = DatabaseConnection.getInstance(System.out);
        
        // Verify they are the same instance
        assertSame("getInstance should return the same instance", instance1, instance2);
    }
    
    @Test
    public void testMainWithArguments() {
        // Call main method with arguments
        TaskmanagerApp.main(new String[]{"test_arg"});
        
        // Check if expected output messages are present
        String output = outContent.toString();
        assertTrue("Should initialize database", output.contains("Initializing database"));
        assertTrue("Should start application", output.contains("Starting TaskManager application"));
    }
    
    @Test
    public void testMainFlow() {
        // Create test input for a more complex flow
        String input = "2\ntestuser\ntestpass\ntest@example.com\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Call main method which should register a user and then exit
        TaskmanagerApp.main(new String[]{});
        
        // Check if expected output messages are present
        String output = outContent.toString();
        assertTrue("Should initialize database", output.contains("Initializing database"));
        assertTrue("Should handle register user menu", output.contains("REGISTER NEW USER"));
    }
}