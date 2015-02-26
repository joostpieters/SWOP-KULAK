package domain;

import exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mathias
 */
public class ProjectManagerTest {
    
    public ProjectManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getProject method, of class ProjectManager.
     */
    @Test (expected=ObjectNotFoundException.class)
    public void testGetProject() throws ObjectNotFoundException{
        System.out.println("getProject");
        ProjectManager instance = new ProjectManager();
        instance.getProject(12);
        
    }

    /**
     * Test of getAvailableTasks method, of class ProjectManager.
     */
    @Test
    public void testGetAvailableTasks() {
        System.out.println("getAvailableTasks");
        ProjectManager instance = new ProjectManager();
        ArrayList<Task> expResult = null;
        ArrayList<Task> result = instance.getAvailableTasks();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
