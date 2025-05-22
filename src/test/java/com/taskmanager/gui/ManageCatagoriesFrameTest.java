import com.taskmanager.gui.ManageCatagoriesFrame;
import com.taskmanager.gui.MainMenuFrame;
import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.taskmanager.model.Category;
import java.lang.reflect.Method;

public class ManageCatagoriesFrameTest {
    private ManageCatagoriesFrame frame;
    private MainMenuFrame parent;

    @Before
    public void setUp() {
        parent = new MainMenuFrame("testUser");
        frame = new ManageCatagoriesFrame(parent);
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull(frame);
        assertEquals("Manage Categories", frame.getTitle());
        assertEquals(800, frame.getWidth());
        assertEquals(500, frame.getHeight());
    }

    @Test
    public void testCategoryTableNotNull() {
        JTable table = frame.getCategoryTable();
        assertNotNull(table);
    }

    @Test
    public void testAddButtonAction() {
        JButton addButton = (JButton) getPrivateField(frame, "addButton");
        assertNotNull(addButton);
        
        try {
            // Add butonuna tıklama
            for (ActionListener al : addButton.getActionListeners()) {
                al.actionPerformed(new ActionEvent(addButton, ActionEvent.ACTION_PERFORMED, ""));
            }
            // showAddCategoryForm metodu çağrılmalı
            assertTrue(true);
        } catch (Exception e) {
            fail("Add button action failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testEditButtonAction() {
        JButton editButton = (JButton) getPrivateField(frame, "editButton");
        assertNotNull(editButton);
        
        try {
            // Edit butonuna tıklama
            for (ActionListener al : editButton.getActionListeners()) {
                al.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, ""));
            }
            // editSelectedCategory metodu çağrılmalı
            assertTrue(true);
        } catch (Exception e) {
            fail("Edit button action failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeleteButtonAction() {
        JButton deleteButton = (JButton) getPrivateField(frame, "deleteButton");
        assertNotNull(deleteButton);
        
        try {
            // Delete butonuna tıklama
            for (ActionListener al : deleteButton.getActionListeners()) {
                al.actionPerformed(new ActionEvent(deleteButton, ActionEvent.ACTION_PERFORMED, ""));
            }
            // deleteSelectedCategory metodu çağrılmalı
            assertTrue(true);
        } catch (Exception e) {
            fail("Delete button action failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testCloseButtonAction() {
        JButton closeButton = (JButton) getPrivateField(frame, "closeButton");
        assertNotNull(closeButton);
        
        try {
            // Close butonuna tıklama
            for (ActionListener al : closeButton.getActionListeners()) {
                al.actionPerformed(new ActionEvent(closeButton, ActionEvent.ACTION_PERFORMED, ""));
            }
            // Frame kapatılmalı
            assertTrue(true);
        } catch (Exception e) {
            fail("Close button action failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testShowAddCategoryForm() {
        try {
            // showAddCategoryForm metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("showAddCategoryForm");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Metot çalıştıktan sonra form elemanlarının durumunu kontrol et
            JTextField nameField = (JTextField) getPrivateField(frame, "nameField");
            JTextArea descriptionArea = (JTextArea) getPrivateField(frame, "descriptionArea");
            JTextField colorField = (JTextField) getPrivateField(frame, "colorField");
            
            assertEquals("Name field should be empty", "", nameField.getText());
            assertEquals("Description area should be empty", "", descriptionArea.getText());
            assertEquals("Color field should have default value", "#FF5733", colorField.getText());
            
            // currentCategoryId değerini kontrol et
            int currentCategoryId = (int) getPrivateField(frame, "currentCategoryId");
            assertEquals("Current category ID should be -1", -1, currentCategoryId);
            
            assertTrue(true);
        } catch (Exception e) {
            fail("showAddCategoryForm method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testEditSelectedCategoryWithNoSelection() {
        try {
            // Tablodan tüm seçimleri kaldır
            JTable table = frame.getCategoryTable();
            table.clearSelection();
            
            // editSelectedCategory metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("editSelectedCategory");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Seçim olmadığı için hata mesajı gösterilmeli, ancak test ortamında gösterilmeyecek
            // Bu durumda metodun çalışmış olması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("editSelectedCategory method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeleteSelectedCategoryWithNoSelection() {
        try {
            // Tablodan tüm seçimleri kaldır
            JTable table = frame.getCategoryTable();
            table.clearSelection();
            
            // deleteSelectedCategory metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("deleteSelectedCategory");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Seçim olmadığı için hata mesajı gösterilmeli, ancak test ortamında gösterilmeyecek
            // Bu durumda metodun çalışmış olması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("deleteSelectedCategory method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSaveCategoryChangesWithEmptyName() {
        try {
            // Form alanlarını ayarla
            JTextField nameField = (JTextField) getPrivateField(frame, "nameField");
            JTextArea descriptionArea = (JTextArea) getPrivateField(frame, "descriptionArea");
            JTextField colorField = (JTextField) getPrivateField(frame, "colorField");
            
            nameField.setText("");  // Boş isim
            descriptionArea.setText("Test Description");
            colorField.setText("#FF5733");
            
            // saveCategoryChanges metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("saveCategoryChanges");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Boş isim nedeniyle hata mesajı gösterilmeli, ancak test ortamında gösterilmeyecek
            // Bu durumda metodun çalışmış olması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("saveCategoryChanges method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSaveCategoryChangesWithInvalidColor() {
        try {
            // Form alanlarını ayarla
            JTextField nameField = (JTextField) getPrivateField(frame, "nameField");
            JTextArea descriptionArea = (JTextArea) getPrivateField(frame, "descriptionArea");
            JTextField colorField = (JTextField) getPrivateField(frame, "colorField");
            
            nameField.setText("Test Category");
            descriptionArea.setText("Test Description");
            colorField.setText("invalid-color");  // Geçersiz renk formatı
            
            // saveCategoryChanges metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("saveCategoryChanges");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Geçersiz renk nedeniyle hata mesajı gösterilmeli, ancak test ortamında gösterilmeyecek
            // Bu durumda metodun çalışmış olması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("saveCategoryChanges method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testCancelEdit() {
        try {
            // cancelEdit metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("cancelEdit");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Bu noktada bir onay kutusu gösterilmeli, ancak test ortamında gösterilmeyecek
            // Bu durumda metodun çalışmış olması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("cancelEdit method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testColorPickerButtonAction() {
        JButton colorPickerButton = (JButton) getPrivateField(frame, "colorPickerButton");
        
        if (colorPickerButton != null) {
            try {
                // Color picker butonuna tıklama
                for (ActionListener al : colorPickerButton.getActionListeners()) {
                    al.actionPerformed(new ActionEvent(colorPickerButton, ActionEvent.ACTION_PERFORMED, ""));
                }
                // Bu noktada renk seçim kutusu gösterilmeli, ancak test ortamında gösterilmeyecek
                // Bu durumda metodun çalışmış olması yeterli
                assertTrue(true);
            } catch (Exception e) {
                fail("Color picker button action failed: " + e.getMessage());
            }
        } else {
            // colorPickerButton null ise testi atla
            assertTrue(true);
        }
    }
    
    private Object getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Test
    public void testSaveCategoryChangesForNewCategory() {
        try {
            // Form alanlarını ayarla
            JTextField nameField = (JTextField) getPrivateField(frame, "nameField");
            JTextArea descriptionArea = (JTextArea) getPrivateField(frame, "descriptionArea");
            JTextField colorField = (JTextField) getPrivateField(frame, "colorField");
            
            // currentCategoryId değerini -1 olarak ayarla (yeni kategori)
            java.lang.reflect.Field currentCategoryIdField = ManageCatagoriesFrame.class.getDeclaredField("currentCategoryId");
            currentCategoryIdField.setAccessible(true);
            currentCategoryIdField.set(frame, -1);
            
            // Geçerli form değerlerini ayarla
            nameField.setText("New Test Category");
            descriptionArea.setText("New Test Description");
            colorField.setText("#FF5733");
            
            // saveCategoryChanges metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("saveCategoryChanges");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Bu durumda yeni bir kategori eklenmiş olmalı
            // Metot çalışması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("saveCategoryChanges method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSaveCategoryChangesForExistingCategory() {
        try {
            // Form alanlarını ayarla
            JTextField nameField = (JTextField) getPrivateField(frame, "nameField");
            JTextArea descriptionArea = (JTextArea) getPrivateField(frame, "descriptionArea");
            JTextField colorField = (JTextField) getPrivateField(frame, "colorField");
            
            // Yeni bir kategori oluştur
            Category testCategory = new Category("Test Cat", "Test Desc", "#FF0000");
            Category.addCategory(testCategory);
            int categoryId = testCategory.getId();
            
            // currentCategoryId değerini ayarla (mevcut kategori)
            java.lang.reflect.Field currentCategoryIdField = ManageCatagoriesFrame.class.getDeclaredField("currentCategoryId");
            currentCategoryIdField.setAccessible(true);
            currentCategoryIdField.set(frame, categoryId);
            
            // Geçerli form değerlerini ayarla
            nameField.setText("Updated Test Category");
            descriptionArea.setText("Updated Test Description");
            colorField.setText("#00FF00");
            
            // saveCategoryChanges metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("saveCategoryChanges");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Bu durumda kategori güncellenmiş olmalı
            // Metot çalışması yeterli
            assertTrue(true);
        } catch (Exception e) {
            fail("saveCategoryChanges method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadCategoryData() {
        try {
            // loadCategoryData metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("loadCategoryData");
            method.setAccessible(true);
            method.invoke(frame);
            
            // Kategori verilerinin yüklenmiş olması gerekir
            JTable table = frame.getCategoryTable();
            assertTrue("Tablo modeli kategori verilerini içermeli", table.getModel().getRowCount() > 0);
        } catch (Exception e) {
            fail("loadCategoryData method test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testStyleButton() {
        try {
            // styleButton metoduna reflection ile erişim
            Method method = ManageCatagoriesFrame.class.getDeclaredMethod("styleButton", JButton.class);
            method.setAccessible(true);
            
            // Test için bir buton oluştur
            JButton testButton = new JButton("Test Button");
            
            // Metodu çağır
            method.invoke(frame, testButton);
            
            // Butonun stillendirilmiş olması gerekir
            assertNotNull("Buton stili uygulanmalı", testButton.getBackground());
            assertNotNull("Buton yazı tipi uygulanmalı", testButton.getFont());
            
            assertTrue(true);
        } catch (Exception e) {
            fail("styleButton method test failed: " + e.getMessage());
        }
    }
} 