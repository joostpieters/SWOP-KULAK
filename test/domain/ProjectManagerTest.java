package domain;

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
     * Test of getProjects method, of class ProjectManager.
     */
    @Test
    public void testGetProjects() {
        System.out.println("getProjects");
        ProjectManager instance = new ProjectManager();
        ArrayList<Project> expResult = null;
        ArrayList<Project> result = instance.getProjects();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createProject method, of class ProjectManager.
     */
    @Test
    public void testCreateProject() {
        System.out.println("createProject");
        String name = "";
        String description = "";
        GregorianCalendar startTime = null;
        GregorianCalendar dueTime = null;
        ProjectManager instance = new ProjectManager();
        instance.createProject(name, description, startTime, dueTime);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProject method, of class ProjectManager.
     */
    @Test
    public void testAddProject() {
        System.out.println("addProject");
        Project project = null;
        ProjectManager instance = new ProjectManager();
        instance.addProject(project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProject method, of class ProjectManager.
     */
    @Test
    public void testGetProject() {
        System.out.println("getProject");
        int pId = 0;
        ProjectManager instance = new ProjectManager();
        Project expResult = null;
        Project result = instance.getProject(pId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
