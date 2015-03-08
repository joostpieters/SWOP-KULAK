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
    	t3 = new Task("t3 finished", 30, 40, Status.FINISHED, t3ts.getStartTime(), t3ts.getEndTime());
    	t4 = new Task("t4", 30, 10, new Task[] {t3});
    	t5 = new Task("t5", 20, 5, new Task[] {t3, t2});
    	Timespan t6ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	t6 = new Task("t6", 10, 3, Status.FAILED, t6ts.getStartTime(), t6ts.getEndTime());
    	t7 = new Task("t7", 15, 4, Status.FAILED, t6ts.getStartTime(), t6ts.getEndTime());
    	t7alternative = new Task("alternative for t7!", 10, 2);
    	t7.setAlternativeTask(t7alternative);
    	t8 = new Task("depends on t7", 33, 3, new Task[] { t7} );
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of constructors of task
     */
    @Test
    public void testConstructorValid()
    {
    	System.out.println("Task constructor");
    	//Task(String description, long estDur, int accDev, Task[] prereq, Status status, Timespan timeSpan)
    	String description = "task0 description";
    	int estDur = 10;
    	int accDev = 30;
    	Task[] prereq = new Task[] {t3};
    	Status status = Status.FINISHED;
    	Timespan tspan = new Timespan(
    			LocalDateTime.of(2015, 4, 4, 11, 48), 
    			LocalDateTime.of(2015, 4, 4, 15, 33)
    			);
    	Task task0 = new Task(description, estDur, accDev, prereq, status, tspan.getStartTime(), tspan.getEndTime());
    	assertEquals(description, task0.getDescription());
    	assertEquals(estDur, task0.getEstimatedDuration().toMinutes());
    	assertEquals(accDev, task0.getAcceptableDeviation());
    	for(int i = 0; i < prereq.length; i++)
    		assertEquals(prereq[i], task0.getPrerequisiteTasks()[i]);
    	assertEquals(status, task0.getStatus());
    	assertEquals(tspan.getStartTime(), task0.getTimeSpan().getStartTime());
    	assertEquals(tspan.getEndTime(), task0.getTimeSpan().getEndTime());
    	
    	//Task(String description, long estDur, int accDev, Status status, Timespan timeSpan)
    	String description1 = "task1 description";
    	int estDur1 = 55;
    	int accDev1 = 22;
    	Status status1 = Status.FAILED;
    	Timespan tspan1 = new Timespan(
    			LocalDateTime.of(2013, 4, 3, 11, 28), 
    			LocalDateTime.of(2013, 4, 4, 15, 33)
    			);
    	Task task1 = new Task(description1, estDur1, accDev1, status1, tspan1.getStartTime(), tspan1.getEndTime());
    	assertEquals(description1, task1.getDescription());
    	assertEquals(estDur1, task1.getEstimatedDuration().toMinutes());
    	assertEquals(accDev1, task1.getAcceptableDeviation());
    	assertEquals(status1, task1.getStatus());
    	assertEquals(tspan1.getStartTime(), task1.getTimeSpan().getStartTime());
    	assertEquals(tspan1.getEndTime(), task1.getTimeSpan().getEndTime());
    	
    	//Task(String description, long estDur, int accDev, Task[] prereq)
    	String description2 = "task2 description";
    	int estDur2 = 55;
    	int accDev2 = 22;
    	Task[] prereq2 = new Task[] {task0};
    	Task task2 = new Task(description2, estDur2, accDev2, prereq2);
    	assertEquals(description2, task2.getDescription());
    	assertEquals(estDur2, task2.getEstimatedDuration().toMinutes());
    	assertEquals(accDev2, task2.getAcceptableDeviation());
    	for(int i = 0; i < prereq2.length; i++)
    		assertEquals(prereq2[i], task2.getPrerequisiteTasks()[i]);
    	
    	//public Task(String description, long estDur, int accDev)
    	String description3 = "task3 description!";
    	int estDur3 = 120;
    	int accDev3 = 55;
    	Task task3 = new Task(description3, estDur3, accDev3);
    	assertEquals(description3, task3.getDescription());
    	assertEquals(estDur3, task3.getEstimatedDuration().toMinutes());
    	assertEquals(accDev3, task3.getAcceptableDeviation());
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
     * Test of update method, of class Task, attempting to set status to UNAVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus1() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	t0.update(startTime, endTime, Status.UNAVAILABLE);
    }

    /**
     * Test of update method, of class Task, attempting to set status to AVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus2() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	t0.update(startTime, endTime, Status.AVAILABLE);
    }
    /**
     * Test of update method, of class Task, attempting to set status to FAILED from FINISHED
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus3() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	t3.update(startTime, endTime, Status.FAILED);
    }
    
    /**
     * Test of update method, of class Task, attempting to set status to FAILED from UNAVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus4()
    {
    	t8.update(LocalDateTime.of(2016, 10, 30, 0, 0), LocalDateTime.of(2016, 11, 30, 0, 0), Status.FAILED);
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
    	t0.update(startTime, endTime, Status.FINISHED);
    	assertEquals(Status.FINISHED, t0.getStatus()); 
    	
    	// AVAILABLE -> FAILED
    	assertEquals(Status.AVAILABLE, t1.getStatus());
    	t1.update(startTime, endTime, Status.FAILED);
    	assertEquals(Status.FAILED, t1.getStatus());
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
        				LocalDateTime.of(2020, 10, 2, 14, 14), 
        				LocalDateTime.of(2020, 10, 3, 14, 14),
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
        assertFalse(t0.isFulfilled());
        assertFalse(t1.isFulfilled());
        assertFalse(t2.isFulfilled());
        assertTrue(t3.isFulfilled());
        assertFalse(t8.isFulfilled());
        assertFalse(t7.isFulfilled());
        t7alternative.update(
				LocalDateTime.of(2020, 10, 2, 14, 14), 
				LocalDateTime.of(2020, 10, 3, 14, 14),
				Status.FINISHED);
        assertTrue(t7.isFulfilled());
    }

    /**
     * Test of endsBefore method, of class Task.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testEndsBeforeException() {
        System.out.println("endsBefore");
        fail("not implemented");
    }

    /**
     * Test of hasTimeSpan method, of class Task.
     */
    @Test
    public void testHasTimeSpan() {
        assertFalse(t0.hasTimeSpan());
        assertTrue(t3.hasTimeSpan());
    }
    /**
     * Test of canHaveAsTimeSpan method, of class Task.
     */
    @Test
    public void testCanHaveAsTimeSpan()
    {
    	Timespan t0timeSpan = new Timespan(
				LocalDateTime.of(2015, 3, 5, 14, 30),
				LocalDateTime.of(2015, 3, 5, 15, 22));
    	assertTrue(t0.canHaveAsTimeSpan(t0timeSpan));
    	
    	/*Time span of prerequisite of t4 is equal to:
		LocalDateTime.of(2015, 3, 4, 11, 48), 
		LocalDateTime.of(2015, 3, 4, 15, 33)
		*/
    	//edge case where the time spans touch (of prerequisite task t3 and t4)
    	Timespan t4timeSpan_success = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 15, 33), 
    			LocalDateTime.of(2015, 3, 4, 16, 0)
    			);
    	assertTrue(t4.canHaveAsTimeSpan(t4timeSpan_success));
    	//time spans not touching, success
    	Timespan t4timeSpan_success2 = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 15, 55), 
    			LocalDateTime.of(2015, 3, 4, 16, 0)
    			);
    	assertTrue(t4.canHaveAsTimeSpan(t4timeSpan_success2));
    	//time span of t4 overlapping time span of prerequisite
    	Timespan t4timeSpan_fail = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 15, 30), 
    			LocalDateTime.of(2015, 3, 4, 15, 35)
    			);
    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail));
    	//time span of t4 inside time span of prerequisite
    	Timespan t4timeSpan_fail2 = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 50), 
    			LocalDateTime.of(2015, 3, 4, 15, 25)
    			);
    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail2));
    	//time span of t4 before time span of prerequisite, overlapping
    	Timespan t4timeSpan_fail3 = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 10, 40), 
    			LocalDateTime.of(2015, 3, 4, 15, 25)
    			);
    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail3));
    	//time span of t4 before time span of prerequisite, not overlapping
    	Timespan t4timeSpan_fail4 = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 10, 40), 
    			LocalDateTime.of(2015, 3, 4, 11, 49)
    			);
    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail4));
    	
    	//checking validity of time span between a task and the alternative of a prerequisite of the task
    	//Time span of alternative of failed prerequisite:
    	t7alternative.update(
    			LocalDateTime.of(2015, 3, 5, 11, 48),
    			LocalDateTime.of(2015, 3, 5, 15, 33), Status.FINISHED);
    	//succesful edge case:
    	Timespan t8timeSpan_success = new Timespan(
    			LocalDateTime.of(2015, 3, 5, 15, 33), 
    			LocalDateTime.of(2015, 3, 5, 15, 45)
    			);
    	assertTrue(t8.canHaveAsTimeSpan(t8timeSpan_success));
    	//succesful non edge case:
    	Timespan t8timeSpan_success2 = new Timespan(
    			LocalDateTime.of(2015, 3, 6, 15, 33), 
    			LocalDateTime.of(2015, 3, 6, 15, 45)
    			);
    	assertTrue(t8.canHaveAsTimeSpan(t8timeSpan_success2));
    	//non successful overlap:
    	Timespan t8timeSpan_fail = new Timespan(
    			LocalDateTime.of(2015, 3, 5, 11, 22), 
    			LocalDateTime.of(2015, 3, 5, 15, 15)
    			);
    	assertFalse(t8.canHaveAsTimeSpan(t8timeSpan_fail));
    	//non succesful non overlap:
    	Timespan t8timeSpan_fail2 = new Timespan(
    			LocalDateTime.of(2015, 3, 1, 11, 22), 
    			LocalDateTime.of(2015, 3, 2, 15, 15)
    			);
    	assertFalse(t8.canHaveAsTimeSpan(t8timeSpan_fail2));
    	
    }
    /**
     * Test of canHaveAsAlternativeTask method, of class Task.
     */
    @Test
    public void testCanHaveAsAlternativeTask()
    {
    	assertTrue(t0.canHaveAsAlternativeTask(t1));
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
    			LocalDateTime.of(2015,  3, 4, 13, 54),
    			LocalDateTime.of(2015,  3, 4, 14, 6));
    	someTask.update(TS12.getStartTime(), TS12.getEndTime(), Status.FINISHED);
    	assertEquals(0, someTask.getDelay().toMinutes());
    	
    	// duration 20, acceptable deviation 20 => max duration 24 minutes
    	// checking time span duration < max duration
    	Task someTask2 = new Task("20, 20", 20, 20);
    	Timespan TS20 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 20));
    	someTask2.update(TS20.getStartTime(), TS20.getEndTime(), Status.FINISHED);
    	assertEquals(0, someTask2.getDelay().toMinutes());
    	
    	// duration 30, acceptable deviation 10 => max duration 33 minutes
    	// checking time span duration > max duration
    	Task someTask3 = new Task("30, 10", 30, 10);
    	Timespan TS35 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 35));
    	someTask3.update(TS35.getStartTime(), TS35.getEndTime(), Status.FINISHED);
    	assertEquals(2, someTask3.getDelay().toMinutes());
    	
    	// duration 30, acceptable deviation 20 => max duration 36 minutes
    	// checking time span duration > max duration
    	Task someTask4 = new Task("30, 20", 30, 20);
    	Timespan TS123 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 15, 3));
    	someTask4.update(TS123.getStartTime(), TS123.getEndTime(), Status.FINISHED);
    	
    	assertEquals(87, someTask4.getDelay().toMinutes());
    	
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
