import com.taskmanager.model.Category;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.lang.reflect.Field;

public class CategoryTest {
    private List<Category> originalCategoryList;
    
    @Before
    public void setUp() {
        // Mevcut kategorileri yedekle
        originalCategoryList = Category.getAllCategories();
    }
    
    @After
    public void tearDown() throws Exception {
        // Test sırasında eklenen kategorileri temizle
        // allCategories statik alanına reflection ile erişim
        Field allCategoriesField = Category.class.getDeclaredField("allCategories");
        allCategoriesField.setAccessible(true);
        
        List<Category> allCategories = (List<Category>) allCategoriesField.get(null);
        allCategories.clear();
        
        // Orijinal kategorileri geri yükle
        for (Category category : originalCategoryList) {
            allCategories.add(category);
        }
    }
    
    @Test
    public void testConstructorAndGetters() {
        Category cat = new Category("Work", "desc", "#fff");
        assertEquals("Work", cat.getName());
        assertEquals("desc", cat.getDescription());
        assertEquals("#fff", cat.getColor());
    }

    @Test
    public void testSetters() {
        Category cat = new Category("Work", "desc", "#fff");
        cat.setName("Personal");
        cat.setDescription("d2");
        cat.setColor("#000");
        assertEquals("Personal", cat.getName());
        assertEquals("d2", cat.getDescription());
        assertEquals("#000", cat.getColor());
    }
    
    @Test
    public void testGetAllCategories() {
        List<Category> categories = Category.getAllCategories();
        assertNotNull("Kategori listesi null olmamalı", categories);
        assertTrue("Varsayılan kategoriler mevcut olmalı", categories.size() >= 5);
    }
    
    @Test
    public void testGetCategoryNames() {
        String[] categoryNames = Category.getCategoryNames();
        assertNotNull("Kategori isimleri null olmamalı", categoryNames);
        assertTrue("Varsayılan kategorilerin isimleri mevcut olmalı", categoryNames.length >= 5);
        
        // İlk 5 varsayılan kategori isimlerini kontrol et
        boolean hasWork = false;
        boolean hasPersonal = false;
        boolean hasStudy = false;
        boolean hasHealth = false;
        boolean hasOther = false;
        
        for (String name : categoryNames) {
            if (name.equals("Work")) hasWork = true;
            if (name.equals("Personal")) hasPersonal = true;
            if (name.equals("Study")) hasStudy = true;
            if (name.equals("Health")) hasHealth = true;
            if (name.equals("Other")) hasOther = true;
        }
        
        assertTrue("Work kategorisi bulunmalı", hasWork);
        assertTrue("Personal kategorisi bulunmalı", hasPersonal);
        assertTrue("Study kategorisi bulunmalı", hasStudy);
        assertTrue("Health kategorisi bulunmalı", hasHealth);
        assertTrue("Other kategorisi bulunmalı", hasOther);
    }
    
    @Test
    public void testAddCategory() {
        int originalSize = Category.getAllCategories().size();
        
        // Yeni kategori ekle
        Category newCategory = new Category("Test Category", "Test Description", "#123456");
        Category.addCategory(newCategory);
        
        // Listenin büyüklüğünü kontrol et
        assertEquals("Kategori eklemesi listeyi büyütmeli", originalSize + 1, Category.getAllCategories().size());
        
        // Eklenen kategoriyi bul
        Category foundCategory = Category.findCategoryByName("Test Category");
        assertNotNull("Eklenen kategori bulunabilmeli", foundCategory);
        assertEquals("Test Description", foundCategory.getDescription());
        assertEquals("#123456", foundCategory.getColor());
    }
    
    @Test
    public void testAddDuplicateCategory() {
        int originalSize = Category.getAllCategories().size();
        
        // İlk kategoriyi ekle
        Category category1 = new Category("Duplicate Test", "Description 1", "#111111");
        Category.addCategory(category1);
        
        // Aynı isimli ikinci kategoriyi ekle
        Category category2 = new Category("Duplicate Test", "Description 2", "#222222");
        Category.addCategory(category2);
        
        // Listenin büyüklüğünü kontrol et (sadece 1 artmış olmalı)
        assertEquals("Aynı isimli kategori eklenince liste sadece 1 artmalı", originalSize + 1, Category.getAllCategories().size());
        
        // Bulunan kategorinin ilk kategori olduğundan emin ol
        Category foundCategory = Category.findCategoryByName("Duplicate Test");
        assertNotNull(foundCategory);
        assertEquals("Description 1", foundCategory.getDescription());
        assertEquals("#111111", foundCategory.getColor());
    }
    
    @Test
    public void testDeleteCategory() {
        // Silinebilir bir kategori ekle
        Category categoryToDelete = new Category("Category To Delete", "Will be deleted", "#ABCDEF");
        Category.addCategory(categoryToDelete);
        
        int originalSize = Category.getAllCategories().size();
        int categoryId = categoryToDelete.getId();
        
        // Kategoriyi sil
        boolean result = Category.deleteCategory(categoryId);
        
        // Silme sonucunu kontrol et
        assertTrue("Kategori silme başarılı olmalı", result);
        assertEquals("Kategori silme listeyi küçültmeli", originalSize - 1, Category.getAllCategories().size());
        assertNull("Silinen kategori artık bulunamamalı", Category.findCategoryById(categoryId));
    }
    
    @Test
    public void testDeleteDefaultCategory() {
        // Varsayılan kategoriyi silmeye çalış (ID 1-5 arası)
        boolean result = Category.deleteCategory(1);
        
        // Silme sonucunu kontrol et
        assertFalse("Varsayılan kategori silinemez olmalı", result);
        assertNotNull("Varsayılan kategori hala mevcut olmalı", Category.findCategoryById(1));
    }
    
    @Test
    public void testFindCategoryById() {
        // Yeni kategori ekle
        Category newCategory = new Category("Find By ID Test", "Test Description", "#123456");
        Category.addCategory(newCategory);
        int categoryId = newCategory.getId();
        
        // ID ile kategoriyi bul
        Category foundCategory = Category.findCategoryById(categoryId);
        
        // Bulunan kategoriyi kontrol et
        assertNotNull("Kategori ID ile bulunabilmeli", foundCategory);
        assertEquals("Find By ID Test", foundCategory.getName());
        assertEquals("Test Description", foundCategory.getDescription());
        assertEquals("#123456", foundCategory.getColor());
    }
    
    @Test
    public void testFindCategoryByIdNotFound() {
        // Var olmayan bir ID ile arama yap
        Category foundCategory = Category.findCategoryById(99999);
        
        // Sonucu kontrol et
        assertNull("Var olmayan ID ile kategori bulunamaz olmalı", foundCategory);
    }
    
    @Test
    public void testFindCategoryByName() {
        // Yeni kategori ekle
        Category newCategory = new Category("Find By Name Test", "Test Description", "#123456");
        Category.addCategory(newCategory);
        
        // İsim ile kategoriyi bul
        Category foundCategory = Category.findCategoryByName("Find By Name Test");
        
        // Bulunan kategoriyi kontrol et
        assertNotNull("Kategori isim ile bulunabilmeli", foundCategory);
        assertEquals(newCategory.getId(), foundCategory.getId());
        assertEquals("Test Description", foundCategory.getDescription());
        assertEquals("#123456", foundCategory.getColor());
    }
    
    @Test
    public void testFindCategoryByNameCaseInsensitive() {
        // Yeni kategori ekle
        Category newCategory = new Category("Case Insensitive Test", "Test Description", "#123456");
        Category.addCategory(newCategory);
        
        // Farklı büyük/küçük harf kullanımıyla arama yap
        Category foundCategory = Category.findCategoryByName("case INSENSITIVE test");
        
        // Bulunan kategoriyi kontrol et
        assertNotNull("Kategori büyük/küçük harf duyarsız olarak bulunabilmeli", foundCategory);
        assertEquals(newCategory.getId(), foundCategory.getId());
    }
    
    @Test
    public void testFindCategoryByNameNotFound() {
        // Var olmayan bir isim ile arama yap
        Category foundCategory = Category.findCategoryByName("Non-existent Category");
        
        // Sonucu kontrol et
        assertNull("Var olmayan isim ile kategori bulunamaz olmalı", foundCategory);
    }
    
    @Test
    public void testUpdateCategory() {
        // Güncellenecek kategoriyi ekle
        Category categoryToUpdate = new Category("Update Test", "Original Description", "#AAAAAA");
        Category.addCategory(categoryToUpdate);
        int categoryId = categoryToUpdate.getId();
        
        // Kategoriyi güncelle
        boolean result = Category.updateCategory(categoryId, "Updated Name", "Updated Description", "#BBBBBB");
        
        // Güncelleme sonucunu kontrol et
        assertTrue("Kategori güncelleme başarılı olmalı", result);
        
        // Güncellenmiş kategoriyi bul
        Category updatedCategory = Category.findCategoryById(categoryId);
        assertNotNull("Güncellenmiş kategori bulunabilmeli", updatedCategory);
        assertEquals("Updated Name", updatedCategory.getName());
        assertEquals("Updated Description", updatedCategory.getDescription());
        assertEquals("#BBBBBB", updatedCategory.getColor());
    }
    
    @Test
    public void testUpdateCategoryWithDuplicateName() {
        // İlk kategoriyi ekle
        Category category1 = new Category("Original Category", "Description 1", "#111111");
        Category.addCategory(category1);
        
        // İkinci kategoriyi ekle
        Category category2 = new Category("Update Conflict Test", "Description 2", "#222222");
        Category.addCategory(category2);
        
        // İkinci kategoriyi birinci kategori ile aynı isme güncellemeyi dene
        boolean result = Category.updateCategory(category2.getId(), "Original Category", "Updated Description", "#333333");
        
        // Güncelleme sonucunu kontrol et
        assertFalse("Aynı isimli kategoriye güncelleme başarısız olmalı", result);
        
        // Kategori değişmemiş olmalı
        Category notUpdatedCategory = Category.findCategoryById(category2.getId());
        assertEquals("Update Conflict Test", notUpdatedCategory.getName());
        assertEquals("Description 2", notUpdatedCategory.getDescription());
        assertEquals("#222222", notUpdatedCategory.getColor());
    }
    
    @Test
    public void testUpdateCategoryNotFound() {
        // Var olmayan bir ID ile güncelleme
        boolean result = Category.updateCategory(99999, "Non-existent Update", "Description", "#ABCDEF");
        
        // Güncelleme sonucunu kontrol et
        assertFalse("Var olmayan kategori güncellenemez olmalı", result);
    }
    
    @Test
    public void testCategoryIdGeneration() {
        // Test ID oluşturma mekanizmasını
        Category cat1 = new Category("ID Test 1", "Description 1", "#111111");
        Category cat2 = new Category("ID Test 2", "Description 2", "#222222");
        
        // İkinci kategori ID'si ilk kategori ID'sinden büyük olmalı
        assertTrue("Kategori ID'ler artan sırada olmalıdır", cat2.getId() > cat1.getId());
    }
    
    @Test
    public void testStaticInitialization() {
        // Static blok içinde eklenen kategorilerin varlığını kontrol et
        
        // Tüm kategorileri al
        List<Category> categories = Category.getAllCategories();
        
        // Varsayılan kategorilerin varlığını kontrol et
        boolean hasWork = false;
        boolean hasPersonal = false;
        boolean hasStudy = false;
        boolean hasHealth = false;
        boolean hasOther = false;
        
        for (Category category : categories) {
            if (category.getName().equals("Work")) hasWork = true;
            if (category.getName().equals("Personal")) hasPersonal = true;
            if (category.getName().equals("Study")) hasStudy = true;
            if (category.getName().equals("Health")) hasHealth = true;
            if (category.getName().equals("Other")) hasOther = true;
        }
        
        assertTrue("Work kategorisi bulunmalı", hasWork);
        assertTrue("Personal kategorisi bulunmalı", hasPersonal);
        assertTrue("Study kategorisi bulunmalı", hasStudy);
        assertTrue("Health kategorisi bulunmalı", hasHealth);
        assertTrue("Other kategorisi bulunmalı", hasOther);
    }
    
    @Test
    public void testDeleteNonExistentCategory() {
        // Var olmayan kategoriyi silmeye çalış
        boolean result = Category.deleteCategory(99999);
        
        // Silme sonucunu kontrol et
        assertFalse("Var olmayan kategori silinemez olmalı", result);
    }
    
    @Test
    public void testUpdateCategoryWithSameName() {
        // Kategoriyi oluştur ve ekle
        Category category = new Category("Same Name Test", "Description", "#AAAAAA");
        Category.addCategory(category);
        int categoryId = category.getId();
        
        // Aynı isimle güncelleme yap
        boolean result = Category.updateCategory(categoryId, "Same Name Test", "Updated Description", "#BBBBBB");
        
        // Güncelleme sonucunu kontrol et
        assertTrue("Aynı isimle güncelleme başarılı olmalı", result);
        
        // Güncellenmiş kategoriyi bul
        Category updatedCategory = Category.findCategoryById(categoryId);
        assertEquals("Same Name Test", updatedCategory.getName());
        assertEquals("Updated Description", updatedCategory.getDescription());
        assertEquals("#BBBBBB", updatedCategory.getColor());
    }
} 