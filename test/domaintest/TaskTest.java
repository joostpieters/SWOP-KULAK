package domaintest;

import domain.Duration;
import domain.Status;
import domain.Task;
import domain.Timespan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class TaskTest {
    
	private Task taskUpdate, t1, t2, t3, t4, t5;
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
    	taskUpdate = new Task("description!", 10, 20);
    	t1 = new Task("t1", 10, 10);
    	t2 = new Task("t2", 20, 10);
    	t2 = new Task("t2", 20, 10);
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
        System.out.println("");
        Task instance = new Task("description", 40, 20);
        Task instance2 = new Task("Other description", 30, 10);
        
        int expResult = 0;
        int result = instance.getId();
        assertNotEquals(instance.getId(), instance2.getId());
        assertTrue(instance.getId() >= 0);
        assertTrue(instance2.getId() > instance.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("1994-10-30 10:40", formatter);
        System.out.println(start.toString());
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
        Task[] expResult = null;
        Task[] result = instance.getPrerequisiteTasks();
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
     
    @Test
    public void testGetDelay() {
        System.out.println("getDelay");
        Task instance = null;
        Duration expResult = null;
        Duration result = instance.getDelay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of update method, of class Task, attempting to set status to UNAVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus1() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	taskUpdate.update(startTime, endTime, Status.UNAVAILABLE);
    }

    /**
     * Test of update method, of class Task, attempting to set status to AVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus2() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	taskUpdate.update(startTime, endTime, Status.AVAILABLE);
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
    public void testCanHaveAsTimeSpan() {
        System.out.println("canHaveAsTimeSpan");
        
        Timespan timeSpan = null;
        Task instance = null;
        boolean expResult = false;
        boolean result = instance.canHaveAsTimeSpan(timeSpan);
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
