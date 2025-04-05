package com.naz.taskmanager;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.Priority;
import com.naz.taskmanager.model.TaskmanagerItem;
import com.naz.taskmanager.repository.DatabaseConnection;
import com.naz.taskmanager.service.TaskService;
import com.naz.taskmanager.service.UserService;

public class TaskmanagerAppTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
    
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
   
   
}