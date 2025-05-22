import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;
import javax.swing.JTable;
import com.taskmanager.gui.EditTaskFrame;
import com.taskmanager.gui.MainMenuFrame;

public class EditTaskFrameTest {
    private EditTaskFrame editTaskFrame;
    
    @Before
    public void setUp() {
        // Test için MainMenuFrame'in mock versiyonunu oluştur
        MainMenuFrame mainMenuFrame = new MainMenuFrame("test_user");
        editTaskFrame = new EditTaskFrame(mainMenuFrame);
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull(editTaskFrame);
        assertNotNull(editTaskFrame.getTaskTable());
    }

    @Test
    public void testEditSelectedTask_noSelection() {
        JTable table = editTaskFrame.getTaskTable();
        table.clearSelection();
        try {
            java.lang.reflect.Method method = EditTaskFrame.class.getDeclaredMethod("editSelectedTask");
            method.setAccessible(true);
            method.invoke(editTaskFrame);
        } catch (Exception e) {
            fail("editSelectedTask çağrısı başarısız: " + e.getMessage());
        }
    }

    @Test
    public void testEditSelectedTask_withSelection() {
        JTable table = editTaskFrame.getTaskTable();
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            try {
                java.lang.reflect.Method method = EditTaskFrame.class.getDeclaredMethod("editSelectedTask");
                method.setAccessible(true);
                method.invoke(editTaskFrame);
            } catch (Exception e) {
                fail("editSelectedTask çağrısı başarısız: " + e.getMessage());
            }
        }
    }

    @Test
    public void testSaveTaskChanges() {
        try {
            java.lang.reflect.Method method = EditTaskFrame.class.getDeclaredMethod("saveTaskChanges");
            method.setAccessible(true);
            method.invoke(editTaskFrame);
        } catch (Exception e) {
            fail("saveTaskChanges çağrısı başarısız: " + e.getMessage());
        }
    }

    @Test
    public void testCancelEdit() {
        try {
            java.lang.reflect.Method method = EditTaskFrame.class.getDeclaredMethod("cancelEdit");
            method.setAccessible(true);
            method.invoke(editTaskFrame);
        } catch (Exception e) {
            fail("cancelEdit çağrısı başarısız: " + e.getMessage());
        }
    }
} 