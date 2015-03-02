package domaintest;

import domain.Duration;
import domain.Status;
import domain.Task;
import domain.Timespan;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * Test of getId method, of class Task.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Task instance = null;
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class Task.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        Task instance = null;
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEstimatedDuration method, of class Task.
     */
    @Test
    public void testGetEstimatedDuration() {
        System.out.println("getEstimatedDuration");
        Task instance = null;
        Duration expResult = null;
        Duration result = instance.getEstimatedDuration();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimeSpan method, of class Task.
     */
    @Test
    public void testGetTimeSpan() {
        System.out.println("getTimeSpan");
        Task instance = null;
        Timespan expResult = null;
        Timespan result = instance.getTimeSpan();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAcceptableDeviation method, of class Task.
     */
    @Test
    public void testGetAcceptableDeviation() {
        System.out.println("getAcceptableDeviation");
        Task instance = null;
        float expResult = 0.0F;
        float result = instance.getAcceptableDeviation();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAlternativeTask method, of class Task.
     */
    @Test
    public void testGetAlternativeTask() {
        System.out.println("getAlternativeTask");
        Task instance = null;
        Task expResult = null;
        Task result = instance.getAlternativeTask();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrerequisiteTasks method, of class Task.
     */
    @Test
    public void testGetPrerequisiteTasks() {
        System.out.println("getPrerequisiteTasks");
        Task instance = null;
        ArrayList<Task> expResult = null;
        ArrayList<Task> result = instance.getPrerequisiteTasks();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class Task.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        Task instance = null;
        Status expResult = null;
        Status result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDelay method, of class Task.
     */
    @Test
    public void testGetDelay() {
        System.out.println("getDelay");
        Task instance = null;
        Duration expResult = null;
        Duration result = instance.getDelay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
     * Test of isFulfilled method, of class Task.
     */
    @Test
    public void testIsFulfilled() {
        System.out.println("isFulfilled");
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.isFulfilled();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endsBefore method, of class Task.
     */
    @Test
    public void testEndsBefore() {
        System.out.println("endsBefore");
        LocalDateTime startTime = null;
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.endsBefore(startTime);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidTimeSpan method, of class Task.
     */
    @Test
    public void testIsValidTimeSpan() {
        System.out.println("isValidTimeSpan");
        Timespan timeSpan = null;
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.isValidTimeSpan(timeSpan);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasTimeSpan method, of class Task.
     */
    @Test
    public void testHasTimeSpan() {
        System.out.println("hasTimeSpan");
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.hasTimeSpan();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidAlternativeTask method, of class Task.
     */
    @Test
    public void testIsValidAlternativeTask() {
        System.out.println("isValidAlternativeTask");
        Task altTask = null;
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.isValidAlternativeTask(altTask);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dependsOn method, of class Task.
     */
    @Test
    public void testDependsOn() {
        System.out.println("dependsOn");
        Task task = null;
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.dependsOn(task);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
