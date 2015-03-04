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
    
	private Task t0, t1, t2, t3, t4, t5, t6, t7, t7alternative, t8;
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
    	t0 = new Task("description!", 10, 20);
    	t1 = new Task("t1", 10, 10);
    	t2 = new Task("t2", 20, 10, new Task[] {t0, t1});
    	
    	Timespan t3ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	t3 = new Task("t3 finished", 30, 40, Status.FINISHED, t3ts);
    	t4 = new Task("t4", 30, 10, new Task[] {t3});
    	t5 = new Task("t5", 20, 5, new Task[] {t3, t2});
    	Timespan t6ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	t6 = new Task("t6", 10, 3, Status.FAILED, t6ts);
    	t7 = new Task("t7", 15, 4, new Task[] {t1, t2, t4}, Status.FAILED, t6ts);
    	t7alternative = new Task("alternative for t7!", 10, 2);
    	t7.setAlternativeTask(t7alternative);
    	t8 = new Task("depends on t7", 33, 3, new Task[] { t7} );
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
    	t0.update(new Timespan(startTime, endTime), Status.UNAVAILABLE);
    }

    /**
     * Test of update method, of class Task, attempting to set status to AVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus2() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	t0.update(new Timespan(startTime, endTime), Status.AVAILABLE);
    }
    /**
     * Test of update method, of class Task, attempting to set status to FAILED from FINISHED
     */
    @Test (expected = IllegalStateException.class)
    public void testUpdateInvalidStatus3() {

    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	Timespan timeSpan = new Timespan(startTime, endTime);
    	t3.update(timeSpan, Status.FAILED);
    }
    /*
     * Test of update method, of class Task
     */
    @Test
    public void testUpdate()
    {
    	// AVAILABLE -> FINISHED
    	assertEquals(Status.AVAILABLE, t0.getStatus());
    	LocalDateTime startTime = LocalDateTime.of(2016, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(2016, 11, 30, 0, 0);
    	Timespan timeSpan = new Timespan(startTime, endTime);
    	t0.update(timeSpan, Status.FINISHED);
    	assertEquals(Status.FINISHED, t0.getStatus()); 
    	
    	// AVAILABLE -> FAILED
    	assertEquals(Status.AVAILABLE, t1.getStatus());
    	t1.update(timeSpan, Status.FAILED);
    	assertEquals(Status.FAILED, t1.getStatus());
    	
    	// UNAVAILABLE -> FAILED
    	assertEquals(Status.UNAVAILABLE, t8.getStatus());
    	t8.update(timeSpan, Status.FAILED);
    	assertEquals(Status.FAILED, t8.getStatus());
    }
    /**
     * Test of isAvailable method, of class Task.
     */
    @Test
    public void testStatus() {
        System.out.println("Status");
        assertEquals(Status.AVAILABLE, t0.getStatus());
        assertEquals(Status.AVAILABLE, t1.getStatus());
        assertEquals(Status.UNAVAILABLE, t2.getStatus());
        assertEquals(Status.FINISHED, t3.getStatus());
        assertEquals(Status.AVAILABLE, t4.getStatus());
        assertEquals(Status.UNAVAILABLE, t5.getStatus());
        assertEquals(Status.FAILED, t6.getStatus());
        assertEquals(Status.FAILED, t7.getStatus());
        assertEquals(Status.AVAILABLE, t7alternative.getStatus());
        assertEquals(Status.UNAVAILABLE, t8.getStatus());
        t7alternative.update(
        		new Timespan(
        				LocalDateTime.of(2020, 10, 2, 14, 14), 
        				LocalDateTime.of(2020, 10, 3, 14, 14)),
        				Status.FINISHED);
        assertEquals(Status.FINISHED, t7alternative.getStatus());
        assertEquals(Status.FAILED, t7.getStatus());
        assertEquals(Status.AVAILABLE, t8.getStatus());
    }

    /**
     * Test of isFulfilled method, of class Task.
     */
    @Test
    public void testIsFulfilled() {
        System.out.println("isFulfilled");
        assertTrue(t0.isFulfilled());
        assertTrue(t1.isFulfilled());
        assertFalse(t2.isFulfilled());
    }

    /**
     * Test of endsBefore method, of class Task.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testEndsBeforeException() {
        System.out.println("endsBefore");
        
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
     * Test of getDelay method, of class Task.
     */
    @Test
    public void testGetDelay()
    {
    	// duration 10, acceptable deviation 20 => max duration 12 minutes
    	// checking time span duration == max duration
    	Task someTask = new Task("10, 20", 10, 20);
    	Timespan TS12 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 12, 54),
    			LocalDateTime.of(2015,  3, 4, 13, 6));
    	someTask.update(TS12, Status.FINISHED);
    	assertEquals(0, someTask.getDelay().getMinutes());
    	
    	// duration 20, acceptable deviation 20 => max duration 24 minutes
    	// checking time span duration < max duration
    	Task someTask2 = new Task("20, 20", 20, 20);
    	Timespan TS20 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 12, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 20));
    	someTask2.update(TS20, Status.FINISHED);
    	assertEquals(0, someTask2.getDelay().getMinutes());
    	
    	// duration 10, acceptable deviation 30 => max duration 33 minutes
    	// checking time span duration > max duration
    	Task someTask3 = new Task("30, 10", 30, 10);
    	Timespan TS35 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 15, 0));
    	someTask3.update(TS35, Status.FINISHED);
    	assertEquals(87, someTask3.getDelay().getMinutes());
    	
    }
    /**
     * Test of dependsOn method, of class Task.
     */
    @Test
    public void testDependsOn() {
        System.out.println("dependsOn");
        assertFalse(t0.dependsOn(t1));
        assertFalse(t0.dependsOn(t6));
        
        assertTrue(t2.dependsOn(t0));
        assertTrue(t2.dependsOn(t1));
        assertFalse(t2.dependsOn(t3));

        assertTrue(t5.dependsOn(t3));
        assertTrue(t5.dependsOn(t2));
        assertTrue(t5.dependsOn(t1)); // indirectly
        assertTrue(t5.dependsOn(t0)); // indirectly
        assertFalse(t5.dependsOn(t6));
        
        assertTrue(t7.dependsOn(t7alternative));
        
        assertTrue(t8.dependsOn(t7));
        assertTrue(t8.dependsOn(t7alternative)); // indirectly depends on the alternative task of t7
        
        
        
    }
    
}
