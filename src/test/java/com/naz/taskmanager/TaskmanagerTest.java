import com.naz.taskmanager.Taskmanager;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.Scanner;

public class TaskmanagerTest {
    @Test
    public void testSingletonInstance() {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        Taskmanager t1 = Taskmanager.getInstance(in, out);
        Taskmanager t2 = Taskmanager.getInstance(in, out);
        assertSame(t1, t2);
    }

    @Test
    public void testGetInputValidAndInvalid() {
        String input = "42\nabc\n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        Taskmanager tm = new Taskmanager(scanner, out);
        assertEquals(42, tm.getInput());
        assertEquals(-2, tm.getInput());
    }

    @Test
    public void testHandleInputError() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        Taskmanager tm = new Taskmanager(new Scanner(""), out);
        tm.handleInputError();
        String output = baos.toString();
        assertTrue(output.contains("Invalid input"));
    }

    @Test
    public void testClearScreen() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        Taskmanager tm = new Taskmanager(new Scanner(""), out);
        tm.clearScreen();
        String output = baos.toString();
        assertTrue(output.contains("\033[H\033[2J"));
    }
} 