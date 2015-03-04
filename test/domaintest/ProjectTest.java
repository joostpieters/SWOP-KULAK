package domaintest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Project;
import domain.Status;
import domain.Task;
import static org.junit.Assert.*;

/**
 * Unit tests for Project
 *
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class ProjectTest {
	
	private static final double EPS = 1e-15;
	
	private int id = 0;
	private String name = "Mobile Steps";
	private String descr = "develop mobile app for counting steps using a specialized bracelet";
	private LocalDateTime create = LocalDateTime.of(2015, 2, 9, 0, 0);
	private LocalDateTime due = LocalDateTime.of(2015, 2, 13, 0, 0);

	Project p, pFinished;
	private Task t1, t2, t3, t4, t5;
	
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
    	p = new Project(id, name, descr, create, due);
    	
    	t1 = new Task("bla1", 8, 0);
    	t2 = new Task("bla2", 8, 10);
    	t3 = new Task("bla3", 7, 20, new Task[]{t1, t2});
    	//TODO: alternative task!
    	t4 = new Task("bla4", 9, 30);
    	t5 = new Task("bla5", 3, 10);
    	
    	pFinished = new Project(id, name, descr, create, due);
    	pFinished.addTask(t5);
    	t5.update(t5.getTimeSpan().getStartTime(), t5.getTimeSpan().getEndTime(), Status.FINISHED);
    	pFinished.finish();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test constructor with valid arguments
     */
    @Test
    public void testConstructorValid() {
    	Project x = new Project(id, name, descr, create, due);
    	
    	assertEquals(x.getId(), id);
    	assertEquals(x.getName(), name);
    	assertEquals(x.getDescription(), descr);
    	assertEquals(x.getCreationTime(), create);
    	assertEquals(x.getDueTime(), due);
    	assertTrue(x.getTasks().isEmpty());
    }
    
    /**
     * Test constructor with invalid time interval.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorInvalidTimeInterval() {
    	LocalDateTime create = LocalDateTime.of(2015, 2, 13, 0, 0);
    	LocalDateTime due = LocalDateTime.of(2015, 2, 9, 0, 0);
    	
    	new Project(id, name, descr, create, due);
    }
    
    /**
     * Test constructor with negative id.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNegativeId() {
    	int id = -1;
    	
    	new Project(id, name, descr, create, due);
    }
    
    /**
     * Test constructor with null-name.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
    	String name = null;
    	
    	new Project(id, name, descr, create, due);
    }
    
    /**
     * Test constructor with null-description.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullDescr() {
    	String descr = null;
    	
    	new Project(id, name, descr, create, due);
    }
    
    /**
     * Test addTask method with simple independent tasks.
     */
    @Test
    public void testAddTaskSimple() {
    	//TODO: wat met geclonede tasks? mogelijk? zelfde id?
    	
    	p.addTask(t1);
    	assertTrue(p.getTasks().contains(t1));
    	assertFalse(p.getTasks().contains(t2));
    	assertFalse(p.getTasks().contains(null));
    	
    	p.addTask(t2);
    	assertTrue(p.getTasks().contains(t1));
    	assertTrue(p.getTasks().contains(t2));
    	assertFalse(p.getTasks().contains(null));
    }
    
    /**
     * Test addTask method with null-task.
     */
    @Test (expected = NullPointerException.class)
    public void testAddTaskNull() {
    	p.addTask(null);
    }
    
    /**
     * Test addTask method with existing prereqs in project.
     */
    @Test
    public void testAddTaskPrereqs() {
    	p.addTask(t1);
    	p.addTask(t2);
    	p.addTask(t3);
    	
    	assertTrue(p.getTasks().contains(t3));
    	assertFalse(p.getTasks().contains(null));
    }
    
    /**
     * Test addTask method with alternative for existing task in project.
     */
    @Test
    public void testAddTaskAlternative() {
    	//TODO: implement
    	p.addTask(t4);
    	fail("not implemented yet");
    }
    
    /**
     * Test addTask method with non-existing prereqs in project.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddTaskInvalidPrereqs() {
    	p.addTask(t3);
    }
    
    /**
     * Test addTask method with partially non-existing prereqs in project.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddTaskInvalidPrereqs2() {
    	p.addTask(t1);
    	p.addTask(t3);
    }
    
    /**
     * Test addTask method with alternative for non-existing task in project.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddTaskInvalidAlternative() {
    	//TODO: implement
    	fail("not implemented yet");
    }
    
    /**
     * Test createTask method with valid, simple parameters (no alternatives or prereqs).
     */
    @Test
    public void testCreateTask() {
    	String descr = "design system";
    	int estDur = 8;
    	int accDev = 20;
    	//TODO: how to indicate there are no prereqs?
    	int[] prereq = new int[]{};
    	Status stat = Status.AVAILABLE;
    	
    	p.createTask(descr, estDur, accDev, prereq, stat);
    	
    	Collection<Task> tasks = p.getTasks();
    	assertFalse(tasks.isEmpty());
    	assertEquals(tasks.size(), 1);
    	
    	Task added = (Task) tasks.toArray()[0];
    	assertEquals(added.getDescription(), descr);
    	assertEquals(added.getEstimatedDuration(), estDur);
    	assertEquals(added.getAcceptableDeviation(), (double) accDev / 100, EPS);
    	//TODO: is this the expected behaviour?
    	assertEquals(added.getAlternativeTask(), null);
    	assertEquals(added.getPrerequisiteTasks().length, 0);
    }
    
    /**
     * Test createTask method with valid parameters and prereqs.
     */
    @Test
    public void testCreateTaskPrereqs() {
    	p.addTask(t1);
    	p.addTask(t2);
    	String descr = "design system";
    	int estDur = 8;
    	int accDev = 20;
    	int[] prereq = new int[]{t1.getId(), t2.getId()};
    	Status stat = Status.AVAILABLE;
    	
    	p.createTask(descr, estDur, accDev, prereq, stat);
    	
    	Collection<Task> tasks = p.getTasks();
    	assertFalse(tasks.isEmpty());
    	assertEquals(tasks.size(), 3);
    	
    	Task added = (Task) tasks.toArray()[2];
    	assertEquals(added.getDescription(), descr);
    	assertEquals(added.getEstimatedDuration(), estDur);
    	assertEquals(added.getAcceptableDeviation(), accDev);
    	//TODO: is this the expected behaviour?
    	assertEquals(added.getAlternativeTask(), null);
    	assertEquals(added.getPrerequisiteTasks().length, 0);
    }
    
    /**
     * Test createTask method with invalid prereqs.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testCreateTaskInvalidPrereqs() {
    	String descr = "design system";
    	int estDur = 8;
    	int accDev = 20;
    	int[] prereq = new int[]{t1.getId(), t2.getId()};
    	Status stat = Status.AVAILABLE;
    	
    	p.createTask(descr, estDur, accDev, prereq, stat);
    }
    
    /**
     * Test createTask method with invalid prereqs.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testCreateTaskInvalidPrereqs2() {
    	p.addTask(t1);
    	String descr = "design system";
    	int estDur = 8;
    	int accDev = 20;
    	int[] prereq = new int[]{t1.getId(), t2.getId()};
    	Status stat = Status.AVAILABLE;
    	
    	p.createTask(descr, estDur, accDev, prereq, stat);
    }
    
    /**
     * Test getAvailableTasks method.
     */
    @Test
    public void testGetAvailableTasks() {
    	p.addTask(t1);
    	p.addTask(t2);
    	p.addTask(t3);
    	
    	List<Task> l = p.getAvailableTasks();
    	assertEquals(l.size(), 2);
    	assertTrue(l.contains(t1));
    	assertTrue(l.contains(t2));
    	assertFalse(l.contains(t3));
    	
    	t1.update(t1.getTimeSpan().getStartTime(), t1.getTimeSpan().getEndTime(), Status.FINISHED);
    	t2.update(t2.getTimeSpan().getStartTime(), t2.getTimeSpan().getEndTime(), Status.FINISHED);

    	assertEquals(l.size(), 1);
    	assertFalse(l.contains(t1));
    	assertFalse(l.contains(t2));
    	assertTrue(l.contains(t3));
    }
    
    /**
     * Test finish method, valid case.
     */
    @Test 
    public void testFinishValid() {
    	p.addTask(t1);
    	t1.update(t1.getTimeSpan().getStartTime(), t1.getTimeSpan().getEndTime(), Status.FINISHED);
    	p.finish();
    	
    	assertTrue(p.isFinished());
    }
    
    /**
     * Test finish method with only unfinished tasks.
     */
    @Test 
    public void testFinishUnfinished() {
    	p.addTask(t1);
    	p.addTask(t2);
    	p.finish();
    	
    	assertFalse(p.isFinished());
    }
    
    /**
     * Test finish method with finished and unfinished tasks.
     */
    @Test 
    public void testFinishUnfinished2() {
    	p.addTask(t1);
    	p.addTask(t2);
    	t1.update(t1.getTimeSpan().getStartTime(), t1.getTimeSpan().getEndTime(), Status.FINISHED);
    	p.finish();
    	
    	assertFalse(p.isFinished());
    }
    
    /**
     * Test finish method when no tasks in the project yet.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testFinishNoTasks() {
    	p.finish();
    }
    
    /**
     * Test finish method when already finished
     */
    @Test
    public void testFinishAlreadyFinished() {
    	pFinished.finish();
    	assertTrue(pFinished.isFinished());
    }
    
    /**
     * Test getAvailableTasks method for finished project.
     */
    @Test
    public void testGetAvailableTasksFinished() {
    	assertTrue(pFinished.getAvailableTasks().isEmpty());
    }
    
    /**
     * Test isOnTime method when project not finished yet.
     */
    @Test
    public void testIsOnTimeUnfinished() {
    	p.addTask(t1);
    	p.addTask(t2);
    	p.isOnTime();
    }
    
}
