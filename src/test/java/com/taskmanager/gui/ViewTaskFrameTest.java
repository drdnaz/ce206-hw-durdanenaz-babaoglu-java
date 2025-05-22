import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.taskmanager.gui.ViewTaskFrame;
import com.taskmanager.gui.MainMenuFrame;

public class ViewTaskFrameTest {
    private ViewTaskFrame viewTaskFrame;
    
    @Before
    public void setUp() {
        // Test için MainMenuFrame'in mock versiyonunu oluştur
        MainMenuFrame mainMenuFrame = new MainMenuFrame("test_user");
        viewTaskFrame = new ViewTaskFrame(mainMenuFrame);
    }
    
    @Test
    public void testFrameInitialization() {
        assertNotNull(viewTaskFrame);
        assertNotNull(viewTaskFrame.getTaskTable());
        assertNotNull(viewTaskFrame.getSearchField());
    }

    @Test
    public void testShowTaskDetails_noSelection() {
        // Hiçbir satır seçilmediğinde bilgi mesajı gösterilmeli
        viewTaskFrame.getTaskTable().clearSelection();
        try {
            java.lang.reflect.Method method = ViewTaskFrame.class.getDeclaredMethod("showTaskDetails");
            method.setAccessible(true);
            method.invoke(viewTaskFrame);
        } catch (Exception e) {
            fail("showTaskDetails çağrısı başarısız: " + e.getMessage());
        }
    }

    @Test
    public void testShowTaskDetails_withSelection() {
        JTable table = viewTaskFrame.getTaskTable();
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            try {
                java.lang.reflect.Method method = ViewTaskFrame.class.getDeclaredMethod("showTaskDetails");
                method.setAccessible(true);
                method.invoke(viewTaskFrame);
            } catch (Exception e) {
                fail("showTaskDetails çağrısı başarısız: " + e.getMessage());
            }
        }
    }

    @Test
    public void testFilterTasks() {
        JTextField searchField = viewTaskFrame.getSearchField();
        searchField.setText("Test");
        // Filtreleme işlemini tetiklemek için filtre butonuna tıklama simülasyonu yapılabilir
        // veya doğrudan filterTasks metodu reflection ile çağrılabilir
        try {
            java.lang.reflect.Method method = ViewTaskFrame.class.getDeclaredMethod("filterTasks");
            method.setAccessible(true);
            method.invoke(viewTaskFrame);
        } catch (Exception e) {
            fail("filterTasks çağrısı başarısız: " + e.getMessage());
        }
    }
} 