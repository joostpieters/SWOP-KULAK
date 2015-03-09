package domaintest;

import domain.Duration;
import domain.Status;
import domain.Task;
import domain.Timespan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

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
    	t0 = new Task("description!", new Duration(10), 20);
    	t1 = new Task("t1", new Duration(10), 10);
    	t2 = new Task("t2", new Duration(20), 10, Arrays.asList(t0, t1));
    	
    	Timespan t3ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	t3 = new Task("t3 finished", new Duration(30), 40);
    	t3.update(t3ts.getStartTime(), t3ts.getEndTime(), Status.FINISHED);
    	t4 = new Task("t4", new Duration(30), 10, Arrays.asList(t3));
    	t5 = new Task("t5", new Duration(20), 5, Arrays.asList(t3, t2));
    	Timespan t6ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	t6 = new Task("t6", new Duration(10), 3);
    	t6.update(t6ts.getStartTime(), t6ts.getEndTime(), Status.FAILED);
    	t7 = new Task("t7", new Duration(15), 4);
    	t7.update(t6ts.getStartTime(), t6ts.getEndTime(), Status.FAILED);
    	t7alternative = new Task("alternative for t7!", new Duration(10), 2, t7);
    	t8 = new Task("depends on t7", new Duration(33), 3, Arrays.asList(t7) );
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
    	
    	//Task(String description, Duration estDur, int accDev, List<Task> prereq)
    	String description = "task0 description";
    	int estDur = 10;
    	int accDev = 30;
    	ArrayList<Task> prereq = new ArrayList<Task>(Arrays.asList(t3));
    	Task task0 = new Task(description, new Duration(estDur), accDev, prereq);
    	assertEquals(description, task0.getDescription());
    	assertEquals(estDur, task0.getEstimatedDuration().toMinutes());
    	assertEquals(accDev, task0.getAcceptableDeviation());
    	for(int i = 0; i < prereq.size(); i++)
    		assertEquals(prereq.get(i), task0.getPrerequisiteTasks().get(i));
    	//Task(String description, Duration estDur, int accDev, List<Task> prereq, altFor)
    	Task task00 = new Task("task00", new Duration(10), 30, Arrays.asList(t0), t6);
    	assertEquals(task00, t6.getAlternativeTask());
    	
    	String description1 = "task1 description";
    	int estDur1 = 55;
    	int accDev1 = 22;
    	Task task1 = new Task(description1, new Duration(estDur1), accDev1);
    	assertEquals(description1, task1.getDescription());
    	assertEquals(estDur1, task1.getEstimatedDuration().toMinutes());
    	assertEquals(accDev1, task1.getAcceptableDeviation());
    	
    	//Task(String description, Duration estDur, int accDev, List<Task> prereq)
    	String description2 = "task2 description";
    	int estDur2 = 55;
    	int accDev2 = 22;
    	ArrayList<Task> prereq2 = new ArrayList<Task>(Arrays.asList(task0));
    	Task task2 = new Task(description2, new Duration(estDur2), accDev2, prereq2);
    	assertEquals(description2, task2.getDescription());
    	assertEquals(estDur2, task2.getEstimatedDuration().toMinutes());
    	assertEquals(accDev2, task2.getAcceptableDeviation());
    	for(int i = 0; i < prereq2.size(); i++)
    		assertEquals(prereq2.get(i), task2.getPrerequisiteTasks().get(i));
    	
    	//Task(String description, Duration estDur, int accDev)
    	String description3 = "task3 description!";
    	int estDur3 = 120;
    	int accDev3 = 55;
    	Task task3 = new Task(description3, new Duration(estDur3), accDev3);
    	assertEquals(description3, task3.getDescription());
    	assertEquals(estDur3, task3.getEstimatedDuration().toMinutes());
    	assertEquals(accDev3, task3.getAcceptableDeviation());
    	
    	//Task(String description, Duration duration, int accDev, Task alternativeFor)
    	task3.update(LocalDateTime.of(2015, 3, 9, 15, 4), LocalDateTime.of(2015, 3, 9, 16, 4), Status.FAILED);
    	
    	String description4 = "descr4";
    	Duration estDur4 = new Duration(222);
    	int accDev4 = 44;
    	Task task4 = new Task(description4, estDur4, accDev4, task3);
    	assertEquals(description4, task4.getDescription());
    	assertEquals(estDur4.toMinutes(), task4.getEstimatedDuration().toMinutes());
    	assertEquals(accDev4, task4.getAcceptableDeviation());
    	assertEquals(task4, task3.getAlternativeTask());
    }
    
    /**
     * Test of getId method, of class Task.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        
        Task instance = new Task("description", new Duration(40), 20);
        Task instance2 = new Task("Other description", new Duration(30), 10);
        
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
    
    /*
     * Test of update method, of class Task
     */
    @Test
    public void testUpdate()
    {
    	System.out.println("Update");
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
    	// UNAVAILABLE -> FAILED
    	assertEquals(Status.UNAVAILABLE, t5.getStatus());
    	t5.update(startTime, endTime, Status.FAILED);
    	assertEquals(Status.FAILED, t5.getStatus());
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
    @Test (expected = IllegalStateException.class)
    public void testEndsBeforeException() {
    	System.out.println("EndsBeforeException");
    	
        Task availableTask = new Task("doesn't have a time span", new Duration(10), 30);
        availableTask.endsBefore(new Timespan(
        		LocalDateTime.of(2015, 5, 4, 10, 2),
        		LocalDateTime.of(2015, 5, 4, 11, 2)));
    }

    /**
     * Test of hasTimeSpan method, of class Task.
     */
    @Test
    public void testHasTimeSpan() {
    	System.out.println("hasTimeSpan");
    	
        assertFalse(t0.hasTimeSpan());
        assertTrue(t3.hasTimeSpan());
    }
    /**
     * Test of canHaveAsTimeSpan method, of class Task.
     */
    @Test
    public void testCanHaveAsTimeSpan()
    {
    	System.out.println("canHaveAsTimeSpan");
    	
    	Timespan t0timeSpan = new Timespan(
				LocalDateTime.of(2015, 3, 5, 14, 30),
				LocalDateTime.of(2015, 3, 5, 15, 22));
    	assertTrue(t0.canHaveAsTimeSpan(t0timeSpan));
    	
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
     * Test of estimatedWorkTimeNeeded method, of class Task
     */
    public void testEstimatedWorkTimeNeeded()
    {
    	System.out.println("estimatedWorkTimeNeeded");
    	
    	assertEquals(t0.getEstimatedDuration().toMinutes(), t0.estimatedWorkTimeNeeded().toMinutes());
    	assertEquals(0, t3.getEstimatedDuration().toMinutes());
    	assertEquals(40, t4.getEstimatedDuration().toMinutes());
    	assertEquals(t7alternative.estimatedWorkTimeNeeded().toMinutes(), t7.estimatedWorkTimeNeeded().toMinutes());
    	
    }
    /*
     * test of canBeFulfilled method, of class Task
     */
    @Test
    public void testCanBeFulfilled()
    {
    	System.out.println("canBeFulfilled");
    	
    	assertTrue(t3.canBeFulfilled());
    	assertTrue(t0.canBeFulfilled());
    	assertFalse(t6.canBeFulfilled());
    	assertTrue(t7.canBeFulfilled());
    	assertTrue(t8.canBeFulfilled());
    	assertFalse(new Task("description", new Duration(10), 30, Arrays.asList(t6)).canBeFulfilled());
    }
    /**
     * Test of canHaveAsAlternativeTask method, of class Task.
     */
    @Test
    public void testCanHaveAsAlternativeTask() // TODO uitgebreider
    {
    	System.out.println("canHaveAsAlternativeTask");
    	
    	// null
    	assertFalse(t0.canHaveAsAlternativeTask(null));
    	// this
    	assertFalse(t0.canHaveAsAlternativeTask(t0));
    	// depends on
    	assertFalse(t0.canHaveAsAlternativeTask(t0)); 
    	assertTrue(t5.canHaveAsAlternativeTask(t2));
    	assertTrue(t5.canHaveAsAlternativeTask(t3));
    	assertTrue(t5.canHaveAsAlternativeTask(t0));
    	// success
    	assertTrue(t0.canHaveAsAlternativeTask(t1));
    }
    /**
     * Test of getDelay method, of class Task.
     */
    @Test
    public void testGetDelay()
    {
    	System.out.println("getDelay");
    	
    	// duration 10, acceptable deviation 20 => max duration 12 minutes
    	// checking time span duration == max duration
    	Task someTask = new Task("10, 20", new Duration(10), 20);
    	Timespan TS12 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 54),
    			LocalDateTime.of(2015,  3, 4, 14, 6));
    	someTask.update(TS12.getStartTime(), TS12.getEndTime(), Status.FINISHED);
    	assertEquals(0, someTask.getDelay().toMinutes());
    	
    	// duration 20, acceptable deviation 20 => max duration 24 minutes
    	// checking time span duration < max duration
    	Task someTask2 = new Task("20, 20", new Duration(20), 20);
    	Timespan TS20 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 20));
    	someTask2.update(TS20.getStartTime(), TS20.getEndTime(), Status.FINISHED);
    	assertEquals(0, someTask2.getDelay().toMinutes());
    	
    	// duration 30, acceptable deviation 10 => max duration 33 minutes
    	// checking time span duration > max duration
    	Task someTask3 = new Task("30, 10", new Duration(30), 10);
    	Timespan TS35 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 35));
    	someTask3.update(TS35.getStartTime(), TS35.getEndTime(), Status.FINISHED);
    	assertEquals(2, someTask3.getDelay().toMinutes());
    	
    	// duration 30, acceptable deviation 20 => max duration 36 minutes
    	// checking time span duration > max duration
    	Task someTask4 = new Task("30, 20", new Duration(30), 20);
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
