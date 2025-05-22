import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.service.CategoryService;
import org.junit.*;
import static org.junit.Assert.*;

public class CategoryServiceTest {
    private CategoryService service;

    @Before
    public void setUp() {
        service = new CategoryService();
    }

    @Test
    public void testAddAndGetCategory() {
        Category cat = new Category("Work", "desc", "#fff");
        service.addCategory(cat);
        assertTrue(service.getAllCategories().contains(cat));
    }

    @Test
    public void testRemoveCategory() {
        Category cat = new Category("Work", "desc", "#fff");
        service.addCategory(cat);
        assertTrue(service.removeCategory(cat));
        assertFalse(service.removeCategory(cat));
    }

    @Test
    public void testFindCategoryByName() {
        Category cat = new Category("Work", "desc", "#fff");
        service.addCategory(cat);
        assertEquals(cat, service.findCategoryByName("Work"));
        assertNull(service.findCategoryByName("Personal"));
    }
} 