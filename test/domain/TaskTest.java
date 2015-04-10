package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class TaskTest {
	
    private Project p;
    private ProjectContainer pm;
	private Task t0, t1, t2, t3, t4, t5, t6, t7, t7alternative, t8;
	
    public TaskTest() {
    }
    
    @Before
    public void setUp() {
    	pm = new ProjectContainer();
    	p = pm.createProject("Name", "Description", LocalDateTime.of(2001, 1, 9, 8, 0), LocalDateTime.of(2072, 10, 9, 8, 0));
    	t0 = p.createTask("description!", new Duration(10), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	t1 = p.createTask("t1", new Duration(10), 10, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	t2 = p.createTask("t2", new Duration(20), 10, Project.NO_ALTERNATIVE, Arrays.asList(t0.getId(), t1.getId()));
    	
    	Timespan t3ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	//pm.advanceSystemTime(t3ts.getEndTime());
    	t3 = p.createTask("t3 finished", new Duration(30), 40, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	p.updateTask(t3.getId(), t3ts.getStartTime(), t3ts.getEndTime(), new Finished());
    	t4 = p.createTask("t4", new Duration(30), 10, Project.NO_ALTERNATIVE, Arrays.asList(t3.getId()));
    	t5 = p.createTask("t5", new Duration(20), 5, Project.NO_ALTERNATIVE, Arrays.asList(t3.getId(), t2.getId()));
    	Timespan t6ts = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	//pm.advanceSystemTime(t6ts.getEndTime());
    	t6 = p.createTask("t6", new Duration(10), 3, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	p.updateTask(t6.getId(), t6ts.getStartTime(), t6ts.getEndTime(), new Failed());
    	t7 = p.createTask("t7", new Duration(15), 4, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	p.updateTask(t7.getId(), t6ts.getStartTime(), t6ts.getEndTime(), new Failed());
    	t7alternative = p.createTask("alternative for t7", new Duration(10), 2, t7.getId(), Project.NO_DEPENDENCIES);
    	t8 = p.createTask("depends on t7", new Duration(33), 3, Project.NO_ALTERNATIVE, Arrays.asList(t7.getId()));
    }
    
    /**
     * Test of constructors of task
     */
    @Test
    public void testConstructorValid()
    {
    	
    	//Task(String description, Duration estDur, int accDev, List<Task> prereq)
    	String description = "task0 description";
    	int estDur = 10;
    	int accDev = 30;
    	ArrayList<Task> prereq = new ArrayList<Task>(Arrays.asList(t3));
    	Task task0 = new Task(description, new Duration(estDur), accDev,  prereq);
    	assertEquals(description, task0.getDescription());
    	assertEquals(estDur, task0.getEstimatedDuration().toMinutes());
    	assertEquals(accDev, task0.getAcceptableDeviation());
    	for(int i = 0; i < prereq.size(); i++)
    		assertEquals(prereq.get(i), task0.getPrerequisiteTasks().get(i));
    	
    	//Task(String description, Duration estDur, int accDev)
    	String description3 = "task3 description!";
    	int estDur3 = 120;
    	int accDev3 = 55;
    	Task task3 = new Task(description3, new Duration(estDur3), accDev3);
    	assertEquals(description3, task3.getDescription());
    	assertEquals(estDur3, task3.getEstimatedDuration().toMinutes());
    	assertEquals(accDev3, task3.getAcceptableDeviation());
    }
    
    /**
     * Test of getId method, of class Task.
     */
    @Test
    public void testGetId() {
        
        Task instance = p.createTask("description", new Duration(40), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        		//new Task("description", new Duration(40), 20);
        Task instance2 = p.createTask("Other description", new Duration(30), 10, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        		//new Task("Other description", new Duration(30), 10);
        
        assertNotEquals(instance.getId(), instance2.getId());
        assertTrue(instance.getId() >= 0);
        assertTrue(instance2.getId() > instance.getId());
    }
    
    /**
     * Test of canHaveAsDescription method, of class Task.
     */
    @Test
    public void testCanHaveAsDescription()
    {
    	assertTrue(t0.canHaveAsDescription("tekst"));
    	assertFalse(t0.canHaveAsDescription(null));
    	assertFalse(t0.canHaveAsDescription(""));
    }
    /**
     * Test of update method, of class Task, attempting to set status to UNAVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus1() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	p.updateTask(t0.getId(), startTime, endTime, new Unavailable());
    	//t0.update(startTime, endTime, new Unavailable());
    }

    /**
     * Test of update method, of class Task, attempting to set status to AVAILABLE
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus2() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	p.updateTask(t0.getId(), startTime, endTime, new Available());
    	//t0.update(startTime, endTime, new Available());
    }
    
    /**
     * Test of update method, of class Task, attempting to set status to FAILED from FINISHED
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStatus3() {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	p.updateTask(t0.getId(), startTime, endTime, new Failed());
    	//t0.update(startTime, endTime, new Available());
    }
    
    /**
     * Test of update method, of class Task, attempting to update with start time uninitialized
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidStart()
    {
    	LocalDateTime endTime = LocalDateTime.of(1994, 11, 30, 0, 0);
    	t0.update(null, endTime, new Failed(), p);
    }

    /**
     * Test of update method, of class Task, attempting to update with end time uninitialized
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateInvalidEnd()
    {
    	LocalDateTime startTime = LocalDateTime.of(1994, 10, 30, 0, 0);
    	t0.update(startTime, null, new Failed(), p);
    }
    
    /**
     * Test of getTimeSpent method, of class Task
     */
    @Test
    public void testGetTimeSpent()
    {
    	LocalDateTime startA = LocalDateTime.of(2014, 10, 30, 10, 10);
    	LocalDateTime endA = LocalDateTime.of(2014, 10, 30, 10, 20);

    	Task taskA = p.createTask("finished, time spent = 10 minutes", new Duration(33), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    			//new Task("finished, time spent = 10 minutes", new Duration(33), 0);
    	assertEquals(0, taskA.getTimeSpent().toMinutes());
    	taskA.update(startA, endA, new Finished(), p);
    	assertEquals(10, taskA.getTimeSpent().toMinutes());

    	Task taskB = p.createTask("failed, time spent = 10 minutes", new Duration(33), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    			//new Task("failed, time spent = 10 minutes", new Duration(33), 0);
    	taskB.update(startA, endA, new Failed(), p);
    	assertEquals(10, taskA.getTimeSpent().toMinutes());
    	
    }
    /*
     * Test of update method, of class Task
     */
    @Test
    public void testUpdate()
    {
    	// AVAILABLE -> FINISHED
    	assertEquals(new Available(), t0.getStatus());
    	LocalDateTime startTime = LocalDateTime.of(2016, 10, 30, 0, 0);
    	LocalDateTime endTime = LocalDateTime.of(2016, 11, 30, 0, 0);
    	t0.update(startTime, endTime, new Finished(), p);
    	assertEquals(new Finished(), t0.getStatus()); 
    	
    	// AVAILABLE -> FAILED
    	assertEquals(new Available(), t1.getStatus());
    	t1.update(startTime, endTime, new Failed(), p);
    	assertEquals(new Failed(), t1.getStatus());
    	// UNAVAILABLE -> FAILED
    	assertEquals(new Unavailable(), t5.getStatus());
    	t5.update(startTime, endTime, new Failed(), p);
    	assertEquals(new Failed(), t5.getStatus());
    }
    
    /**
     * Test of isAvailable method, of class Task.
     */
    @Test
    public void testStatus() {
        assertEquals(new Available(), t0.getStatus());
        assertEquals(new Available(), t1.getStatus());
        assertEquals(new Unavailable(), t2.getStatus());
        assertEquals(new Finished(), t3.getStatus());
        assertEquals(new Available(), t4.getStatus());
        assertEquals(new Unavailable(), t5.getStatus());
        assertEquals(new Failed(), t6.getStatus());
        assertEquals(new Failed(), t7.getStatus());
        assertEquals(new Available(), t7alternative.getStatus());
        assertEquals(new Unavailable(), t8.getStatus());
        t7alternative.update(
        				LocalDateTime.of(2020, 10, 2, 14, 14), 
        				LocalDateTime.of(2020, 10, 3, 14, 14),
        				new Finished(), p);
        assertEquals(new Finished(), t7alternative.getStatus());
        assertEquals(new Failed(), t7.getStatus());
        assertEquals(new Available(), t8.getStatus());
    }

    /**
     * Test of isFulfilled method, of class Task.
     */
    @Test
    public void testIsFulfilled() {
        assertFalse(t0.isFulfilled());
        assertFalse(t1.isFulfilled());
        assertFalse(t2.isFulfilled());
        assertTrue(t3.isFulfilled());
        assertFalse(t8.isFulfilled());
        assertFalse(t7.isFulfilled());
        t7alternative.update(
				LocalDateTime.of(2020, 10, 2, 14, 14), 
				LocalDateTime.of(2020, 10, 3, 14, 14),
				new Finished(), p);
        assertTrue(t7.isFulfilled());
    }

    /**
     * Test of endsBefore method, of class Task.
     */
    @Test (expected = IllegalStateException.class)
    public void testEndsBeforeException() {
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
        assertFalse(t0.hasTimeSpan());
        assertTrue(t3.hasTimeSpan());
    }
    
    
    /**
     * Test of canHaveAsTimeSpan method, of class Task.
     */
//    @Test
//    public void testCanHaveAsTimeSpan()
//    {
//    	Timespan t0timeSpan = new Timespan(
//				LocalDateTime.of(2015, 3, 5, 14, 30),
//				LocalDateTime.of(2015, 3, 5, 15, 22));
//    	assertTrue(t0.canHaveAsTimeSpan(t0timeSpan));
//    	
//    	//edge case where the time spans touch (of prerequisite task t3 and t4)
//    	Timespan t4timeSpan_success = new Timespan(
//    			LocalDateTime.of(2015, 3, 4, 15, 33), 
//    			LocalDateTime.of(2015, 3, 4, 16, 0)
//    			);
//    	assertTrue(t4.canHaveAsTimeSpan(t4timeSpan_success));
//    	
//    	//time spans not touching, success
//    	Timespan t4timeSpan_success2 = new Timespan(
//    			LocalDateTime.of(2015, 3, 4, 15, 55), 
//    			LocalDateTime.of(2015, 3, 4, 16, 0)
//    			);
//    	assertTrue(t4.canHaveAsTimeSpan(t4timeSpan_success2));
//    	
//    	//time span of t4 overlapping time span of prerequisite
//    	Timespan t4timeSpan_fail = new Timespan(
//    			LocalDateTime.of(2015, 3, 4, 15, 30), 
//    			LocalDateTime.of(2015, 3, 4, 15, 35)
//    			);
//    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail));
//    	
//    	//time span of t4 inside time span of prerequisite
//    	Timespan t4timeSpan_fail2 = new Timespan(
//    			LocalDateTime.of(2015, 3, 4, 11, 50), 
//    			LocalDateTime.of(2015, 3, 4, 15, 25)
//    			);
//    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail2));
//    	
//    	//time span of t4 before time span of prerequisite, overlapping
//    	Timespan t4timeSpan_fail3 = new Timespan(
//    			LocalDateTime.of(2015, 3, 4, 10, 40), 
//    			LocalDateTime.of(2015, 3, 4, 15, 25)
//    			);
//    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail3));
//    	
//    	//time span of t4 before time span of prerequisite, not overlapping
//    	Timespan t4timeSpan_fail4 = new Timespan(
//    			LocalDateTime.of(2015, 3, 4, 10, 40), 
//    			LocalDateTime.of(2015, 3, 4, 11, 49)
//    			);
//    	assertFalse(t4.canHaveAsTimeSpan(t4timeSpan_fail4));
//    	
//    	//checking validity of time span between a task and the alternative of a prerequisite of the task
//    	//Time span of alternative of failed prerequisite:
//    	t7alternative.update(
//    			LocalDateTime.of(2015, 3, 5, 11, 48),
//    			LocalDateTime.of(2015, 3, 5, 15, 33), new Finished(), p);
//    	
//    	//successful edge case:
//    	Timespan t8timeSpan_success = new Timespan(
//    			LocalDateTime.of(2015, 3, 5, 15, 33), 
//    			LocalDateTime.of(2015, 3, 5, 15, 45)
//    			);
//    	assertTrue(t8.canHaveAsTimeSpan(t8timeSpan_success));
//    	
//    	//successful non edge case:
//    	Timespan t8timeSpan_success2 = new Timespan(
//    			LocalDateTime.of(2015, 3, 6, 15, 33), 
//    			LocalDateTime.of(2015, 3, 6, 15, 45)
//    			);
//    	assertTrue(t8.canHaveAsTimeSpan(t8timeSpan_success2));
//    	
//    	//non successful overlap:
//    	Timespan t8timeSpan_fail = new Timespan(
//    			LocalDateTime.of(2015, 3, 5, 11, 22), 
//    			LocalDateTime.of(2015, 3, 5, 15, 15)
//    			);
//    	assertFalse(t8.canHaveAsTimeSpan(t8timeSpan_fail));
//    	
//    	//non successful non overlap:
//    	Timespan t8timeSpan_fail2 = new Timespan(
//    			LocalDateTime.of(2015, 3, 1, 11, 22), 
//    			LocalDateTime.of(2015, 3, 2, 15, 15)
//    			);
//    	assertFalse(t8.canHaveAsTimeSpan(t8timeSpan_fail2));
//    	
//    	//successful no prerequisites
//    	assertTrue(new Task("descr", new Duration(10), 20).canHaveAsTimeSpan(t0timeSpan));
//    	
//    }
    
    /**
     * Test of canHaveAsAcceptableDeviation method, of class Task
     */
    @Test
    public void testCanHaveAsAcceptableDeviation()
    {
    	assertTrue(t0.canHaveAsAcceptableDeviation(0));
    	assertTrue(t0.canHaveAsAcceptableDeviation(15));
    	assertTrue(t0.canHaveAsAcceptableDeviation(67));
    	assertTrue(t0.canHaveAsAcceptableDeviation(100));
    	assertFalse(t0.canHaveAsAcceptableDeviation(-1));
    	assertFalse(t0.canHaveAsAcceptableDeviation(-120));
    	assertFalse(t0.canHaveAsAcceptableDeviation(101456));
    }
    
    /*
     * test of setAlternativeTask method, of class Task.
     * Attempt to set alternative task of t0 which is not failed
     */
    @Test (expected = IllegalStateException.class)
    public void testSetAlternativeTaskException1()
    {
    	t0.setAlternativeTask(t1, p);
    }
    
    /*
     * test of setAlternativeTask method, of class Task.
     * Attempt to set alternative task of t7 which already has an alternative task
     */
    @Test (expected = IllegalStateException.class)
    public void testSetAlternativeTaskException2()
    {
    	t7.setAlternativeTask(t1, p);
    }
    
    /*
     * test of setAlternativeTask method, of class Task.
     * Attempt to set alternative task of t7 which already has an alternative task
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetAlternativeTaskException3()
    {
    	t0.update(LocalDateTime.of(2015, 3, 10, 10, 10), LocalDateTime.of(2015, 3, 10, 20, 10), new Failed(), p);
    	t0.setAlternativeTask(t5, p);
    }
    
    /*
     * test of setAlternativeTask method, of class Task.
     * Attempt to set alternative task of t6 to t3 with illegal wrong project argument
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetAlternativeTaskException4()
    {
    	Project p1 = pm.createProject("p1", "not p0", LocalDateTime.of(2000, 11, 12, 13, 14), LocalDateTime.of(2030, 11, 12, 13, 14));
    	t6.setAlternativeTask(t0, p1);
    }
    
    /*
     * test of setAlternativeTask method, of class Task.
     * Attempt to set alternative task of t6 to a task belonging to a different project
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetAlternativeTaskException5()
    {
    	Project p1 = pm.createProject("p1", "not p", LocalDateTime.of(2000, 11, 12, 13, 14), LocalDateTime.of(2030, 11, 12, 13, 14));
    	Task p1t0 = p1.createTask("task belonging to p1", new Duration(120), 33, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	t6.setAlternativeTask(p1t0, p1);
    }
    
    /*
     * test of setAlternativeTask method, of class Task.
     * Attempt to set alternative task of t6 to a task belonging to a different project
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetAlternativeTaskException6()
    {
    	Project p1 = pm.createProject("p1", "not p", LocalDateTime.of(2000, 11, 12, 13, 14), LocalDateTime.of(2030, 11, 12, 13, 14));
    	Task p1t0 = p1.createTask("task belonging to p1", new Duration(120), 33, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	t6.setAlternativeTask(p1t0, p);
    }
    
    /**
     * Test of setAlternative method, of class Task
     */
    @Test
    public void testSetAlternativeTask()
    {
    	t6.setAlternativeTask(t0, p);
    	assertEquals(t0, t6.getAlternativeTask());
    }
    
    /*
     * test of canHaveAsPrerequisiteTasks method, of class Task.
     */
    @Test
    public void testCanHaveAsPrerequisiteTasks()
    {
    	assertFalse(t0.canHaveAsPrerequisiteTasks(null));
    	ArrayList<Task> containsNull = new ArrayList<Task>();
    	containsNull.add(null);
    	assertFalse(t0.canHaveAsPrerequisiteTasks(containsNull));
    	assertFalse(t0.canHaveAsPrerequisiteTasks(Arrays.asList(t0)));
    	assertTrue(t0.canHaveAsPrerequisiteTasks(Arrays.asList(t1)));
    	assertFalse(t0.canHaveAsPrerequisiteTasks(Arrays.asList(t5)));
    }
    
    /*
     * test of canBeFulfilled method, of class Task
     */
    @Test
    public void testCanBeFulfilled()
    {
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
    public void testCanHaveAsAlternativeTask()
    {
    	// null
    	assertFalse(t0.canHaveAsAlternativeTask(null, p));
    	// this
    	assertFalse(t0.canHaveAsAlternativeTask(t0, p));
    	// depends on
    	assertFalse(t0.canHaveAsAlternativeTask(t0, p)); 
    	assertTrue(t5.canHaveAsAlternativeTask(t2, p));
    	assertTrue(t5.canHaveAsAlternativeTask(t3, p));
    	assertTrue(t5.canHaveAsAlternativeTask(t0, p));
    	// success
    	assertTrue(t0.canHaveAsAlternativeTask(t1, p));
    }
    /**
     * Test of getDelay method, of class Task.
     */
    @Test
    public void testGetDelay()
    {
    	// Task with no time span t0
    	assertEquals(0, t0.getDelay().toMinutes());
    	
    	// duration 10, acceptable deviation 20 => max duration 12 minutes
    	// checking time span duration == max duration
    	Task someTask = p.createTask("10, 20", new Duration(10), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	Timespan TS12 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 54),
    			LocalDateTime.of(2015,  3, 4, 14, 6));
    	someTask.update(TS12.getStartTime(), TS12.getEndTime(), new Finished(), p);
    	assertEquals(0, someTask.getDelay().toMinutes());
    	
    	// duration 20, acceptable deviation 20 => max duration 24 minutes
    	// checking time span duration < max duration
    	Task someTask2 = p.createTask("20, 20", new Duration(20), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	Timespan TS20 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 20));
    	someTask2.update(TS20.getStartTime(), TS20.getEndTime(), new Finished(), p);
    	assertEquals(0, someTask2.getDelay().toMinutes());
    	
    	// duration 30, acceptable deviation 10 => max duration 33 minutes
    	// checking time span duration > max duration
    	Task someTask3 = p.createTask("30, 10", new Duration(30), 10, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	Timespan TS35 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 13, 35));
    	someTask3.update(TS35.getStartTime(), TS35.getEndTime(), new Finished(), p);
    	assertEquals(2, someTask3.getDelay().toMinutes());
    	
    	// duration 30, acceptable deviation 20 => max duration 36 minutes
    	// checking time span duration > max duration
    	Task someTask4 = p.createTask("30, 20", new Duration(30), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    			//new Task("30, 20", new Duration(30), 20);
    	Timespan TS123 = new Timespan(
    			LocalDateTime.of(2015,  3, 4, 13, 0),
    			LocalDateTime.of(2015,  3, 4, 15, 3));
    	someTask4.update(TS123.getStartTime(), TS123.getEndTime(), new Finished(), p);
    	assertEquals(87, someTask4.getDelay().toMinutes());
    	
    }
    
    /**
     * Test of method canUpdateStatus, of class Task
     */
//    @Test
//    public void testCanUpdateStatus()
//    {
//    	assertEquals(new Finished(), t3.getStatus());       // t3 is finished
//    	assertFalse(t3.canUpdateStatus(new Failed()));      // finished  -> failed
//    	assertFalse(t3.canUpdateStatus(new Available()));   // finished  -> available
//    	assertFalse(t3.canUpdateStatus(new Unavailable())); // finished  -> unavailable
//
//    	assertEquals(new Failed(), t6.getStatus());         // t6 is failed
//    	assertFalse(t6.canUpdateStatus(new Finished()));    // failed  -> finished
//    	assertFalse(t6.canUpdateStatus(new Available()));   // failed  -> available
//    	assertFalse(t6.canUpdateStatus(new Unavailable())); // failed  -> unavailable
//    	
//    	assertEquals(new Available(), t0.getStatus());      // t0 is available
//    	assertTrue(t0.canUpdateStatus(new Finished()));     // available    -> finished
//    	assertTrue(t0.canUpdateStatus(new Failed()));       // available    -> failed
//    	assertFalse(t0.canUpdateStatus(new Unavailable())); // available    -> unavailable
//
//    	assertEquals(new Unavailable(), t2.getStatus());    // t2 is unavailable
//    	assertFalse(t2.canUpdateStatus(new Finished()));    // unavailable  -> finished
//    	assertTrue(t2.canUpdateStatus(new Failed()));       // unavailable  -> failed
//    	assertFalse(t2.canUpdateStatus(new Finished()));    // unavailable  -> available
//    }
    /**
     * Test of estimatedWorkTimeNeeded method, of class Task
     */
    @Test
    public void testEstimatedWorkTimeNeeded()
    {
    	assertEquals(new Finished(), t3.getStatus());
    	assertEquals(Duration.ZERO.toMinutes(), t3.estimatedWorkTimeNeeded().toMinutes());

    	assertEquals(new Failed(), t6.getStatus());
    	assertEquals(Duration.ZERO.toMinutes(), t6.estimatedWorkTimeNeeded().toMinutes());
    	
    	assertEquals(new Available(), t0.getStatus());
    	assertEquals(t0.getEstimatedDuration().toMinutes(), t0.estimatedWorkTimeNeeded().toMinutes());
    	
    	Task unavailableTask = p.createTask("unavailable", new Duration(33), 22, Project.NO_ALTERNATIVE, Arrays.asList(t0.getId()));
    	assertEquals(new Unavailable(), unavailableTask.getStatus());
    	long estimatedWorkTimeExpected = t0.estimatedWorkTimeNeeded().toMinutes() + unavailableTask.getEstimatedDuration().toMinutes();
    	assertEquals(estimatedWorkTimeExpected, unavailableTask.estimatedWorkTimeNeeded().toMinutes());
    	
    }
    
    /**
     * Test of dependsOn method, of class Task.
     */
    @Test
    public void testDependsOn() {
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
    
    /**
     * Test of getTimeSpent method, of class Task.
     */
    @Test
    public void TestGetTimeSpent()
    {
    	assertEquals(165, t7.getTimeSpent().toMinutes());
    	assertEquals(0, t8.getTimeSpent().toMinutes());
    	assertEquals(0, t1.getTimeSpent().toMinutes());
    	assertEquals(165, t6.getTimeSpent().toMinutes());

    	t0.update(LocalDateTime.of(2015, 3, 10, 10, 0), LocalDateTime.of(2015, 3, 10, 11, 30), new Finished(), p);
    	assertEquals(90, t0.getTimeSpent().toMinutes());
        
    	Task task = p.createTask("task abc", new Duration(33), 54, Project.NO_ALTERNATIVE, Arrays.asList(t0.getId()));
    	task.update(LocalDateTime.of(2015, 3, 11, 10, 0), LocalDateTime.of(2015, 3, 11, 15, 30), new Finished(), p);
    	assertEquals(90+4*60+30, task.getTimeSpent().toMinutes());
    }
}
