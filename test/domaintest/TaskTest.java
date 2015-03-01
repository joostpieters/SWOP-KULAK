package domaintest;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Status;
import domain.Task;
import static org.junit.Assert.*;

/**
 *
 * @author Mathias
 */
public class TaskTest {
    
    public TaskTest() {
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
     * Test of update method, of class Task.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        LocalDateTime start = null;
        LocalDateTime end = null;
        Status status = null;
        Task instance = null;
        instance.update(start, end, status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isAvailable method, of class Task.
     */
    @Test
    public void testIsAvailable() {
        System.out.println("isAvailable");
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.isAvailable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of generation of ids, of class Task?
     */
    @Test
    public void testTaskGeneratedIds()
    {
    	Task instance0 = new Task("Some task", 60, 0.2f, null, Status.AVAILABLE);
    	Task instance1 = new Task("Some other task", 90, 0.3f, null, Status.AVAILABLE);
    	assertNotEquals(instance0.getId(), instance1.getId());
    	assertTrue(instance1.getId() > instance0.getId());
    }
    
}
