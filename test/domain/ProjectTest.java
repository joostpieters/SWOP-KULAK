package domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Project;
import domain.Status;
import static org.junit.Assert.*;

/**
 *
 * @author Mathias
 */
public class ProjectTest {
    
    public ProjectTest() {
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
     * Test of getTasks method, of class Project.
     */
    @Test
    public void testGetTasks() {
        System.out.println("getTasks");
        Project instance = null;
        ArrayList<Task> expResult = null;
        ArrayList<Task> result = instance.getTasks();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of createTask method, of class Project.
     */
    @Test
    public void testCreateTask() {
    	Project x = new Project(0, "name", "descr", new GregorianCalendar(), new GregorianCalendar());
    	x.createTask("bla", 5, 10, 2, new int[]{1,4,5}, Status.AVAILABLE);
    	
    }
    
}
