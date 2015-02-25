package domain;

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
        GregorianCalendar start = null;
        GregorianCalendar end = null;
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
    
}
