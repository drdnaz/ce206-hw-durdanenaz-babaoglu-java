import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.Task;
import com.naz.taskmanager.service.TaskService;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;

public class TaskServiceTest {
    private TaskService service;
    private Category category;

    @Before
    public void setUp() {
        service = new TaskService("testUser");
        category = new Category("Work", "desc", "#fff");
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = service.createTask("Başlık", "Açıklama", category);
        assertNotNull(task);
        assertEquals("Başlık", task.getTitle());
        assertTrue(service.getAllTasks().contains(task));
    }

    @Test
    public void testDeleteTask() {
        Task task = service.createTask("Başlık", "Açıklama", category);
        assertTrue(service.deleteTask(task.getId()));
        assertFalse(service.deleteTask(task.getId()));
    }

    @Test
    public void testUpdateTask() {
        Task task = service.createTask("Başlık", "Açıklama", category);
        Date newDate = new Date();
        boolean updated = service.updateTask(task.getId(), "Yeni", "Desc", newDate, category, "High", "Completed");
        assertTrue(updated);
        Task updatedTask = service.getTaskById(task.getId());
        assertEquals("Yeni", updatedTask.getTitle());
        assertEquals("Completed", updatedTask.getStatus());
    }

    @Test
    public void testGetTaskByIdNotFound() {
        assertNull(service.getTaskById(9999));
    }
} 