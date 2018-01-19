package dao;

import models.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Guest on 1/18/18.
 */
public class Sql2oCategoryDaoTest {

    private Sql2oCategoryDao categoryDao;
    private Sql2oTaskDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString,"","");
        categoryDao = new Sql2oCategoryDao(sql2o);
        taskDao = new Sql2oTaskDao(sql2o);

        conn = sql2o.open();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Category category = setupNewCategory();
        int originalCategoryId = category.getId();
        categoryDao.add(category);
        assertNotEquals(originalCategoryId, category.getId());
    }

    @Test
    public void existingCategoriesCanBeFoundById() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        Category foundCategory = categoryDao.findById(category.getId());
        assertEquals(category, foundCategory);
    }

    @Test
    public void allCategoriesAreFound() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        assertEquals(1, categoryDao.getAll().size());
    }
    @Test
    public void noCategoriesReturnsEmptyList() throws Exception {
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void updatesChangeCategoryName() throws Exception {
        String initialDescription = "Yardwork";
        Category category = new Category(initialDescription);
        categoryDao.add(category);

        categoryDao.update(category.getId(),"Cleaning");
        Category updatedCategory = categoryDao.findById(category.getId()); //have to find again because changed in DAO?
        assertNotEquals(initialDescription, updatedCategory.getName());
    }

    @Test
    public void categoryCanBeDeleted() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        categoryDao.deleteById(category.getId());
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Category category = setupNewCategory();
        Category otherCategory = new Category("Cleaning");
        categoryDao.add(category);
        categoryDao.add(otherCategory);
        int daoSize = categoryDao.getAll().size();
        categoryDao.clearAllCategories();
        assertTrue(daoSize > 0 && daoSize > categoryDao.getAll().size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        int categoryId = category.getId();
        Task newTask = new Task("mow the lawn", categoryId);
        Task otherTask = new Task("pull weeds", categoryId);
        Task thirdTask = new Task("trim hedge", categoryId);
        taskDao.add(newTask);
        taskDao.add(otherTask);

        assertTrue(categoryDao.getAllTasksByCategory(categoryId).size() == 2);
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(newTask));
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(otherTask));
        assertFalse(categoryDao.getAllTasksByCategory(categoryId).contains(thirdTask));
    }


    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Category setupNewCategory(){
        return new Category("Yardwork");
    }
}
