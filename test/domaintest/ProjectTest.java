package domaintest;

import java.time.LocalDateTime;

import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import exception.ObjectNotFoundException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Project
 *
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class ProjectTest {
	
	private String name = "Mobile Steps";
	private String descr = "develop mobile app for counting steps using a specialized bracelet";
	private LocalDateTime create = LocalDateTime.of(2015, 2, 9, 0, 0);
	private LocalDateTime due = LocalDateTime.of(2015, 2, 13, 0, 0);

	String taskdescr = "act cool";
	int estdurH = 8;
	int estdurM = 0;
	int accdev = 10;
	int altFor = Project.NO_ALTERNATIVE;
	int[] prereqs = Project.NO_DEPENDENCIES;
	
	LocalDateTime start = LocalDateTime.of(2015, 2, 9, 8, 0);
	LocalDateTime end = LocalDateTime.of(2015, 2, 9, 10, 0);
	
	ProjectManager pm;
	Project p0, p1, p2, pFinished;
	Task t1, t2, t3, tFin;
	
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
    	pm = new ProjectManager();
    	
    	p0 = pm.createProject(name, descr, create, due);
    	
    	p1 = pm.createProject(name, descr, create, due);
    	t1 = p1.createTask("design system", 8, 0, 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	
    	p2 = pm.createProject(name, descr, create, due);
    	t2 = p2.createTask("design system", 8, 0, 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	t3 = p2.createTask("implement system in native code", 16, 0, 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	
    	pFinished = pm.createProject(name, descr, create, due);
    	tFin = pFinished.createTask("design system", 8, 0, 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	tFin.update(start, end, Status.FINISHED);
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test constructor with valid arguments
     */
    @Test
    public void testConstructorValid() {
    	Project x = new Project(name, descr, create, due, pm.getSystemClock());
    	
    	assertEquals(x.getName(), name);
    	assertEquals(x.getDescription(), descr);
    	x.getCreationTime().minusHours(2);
    	assertEquals(x.getCreationTime(), create);
    	x.getDueTime().minusHours(2);
    	assertEquals(x.getDueTime(), due);
    	x.getClock().advanceTime(LocalDateTime.of(2015, 12, 31, 23, 59));
    	assertEquals(x.getClock(), pm.getSystemClock());
    	x.getTasks().add(null);
    	assertTrue(x.getTasks().isEmpty());
    }
    
    /**
     * Test constructor with invalid time interval.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorInvalidTimeInterval() {
    	LocalDateTime create = LocalDateTime.of(2015, 2, 13, 0, 0);
    	LocalDateTime due = LocalDateTime.of(2015, 2, 9, 0, 0);
    	
    	new Project(name, descr, create, due, pm.getSystemClock());
    }
    
    /**
     * Test constructor with null-name.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
    	String name = null;
    	
    	new Project(name, descr, create, due, pm.getSystemClock());
    }
    
    /**
     * Test constructor with empty name.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
    	String name = "";
    	
    	new Project(name, descr, create, due, pm.getSystemClock());
    }
    
    /**
     * Test constructor with null-description.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullDescr() {
    	String descr = null;
    	
    	new Project(name, descr, create, due, pm.getSystemClock());
    }
    
    /**
     * Test constructor with insufficient description.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorEmptyDescr() {
    	String descr = "";
    	
    	new Project(name, descr, create, due, pm.getSystemClock());
    }
    
    /**
     * Test createTask method with simple valid parameters (without alternative or dependencies).
     */
    @Test
    public void testCreateTaskSimple() {
    	Task t = p0.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    	
    	assertTrue(p0.getTasks().contains(t));
    	assertEquals(t.getDescription(), taskdescr);
    	assertEquals(t.getEstimatedDuration().getHours(), estdurH);
    	assertEquals(t.getEstimatedDuration().getMinutes(), estdurM);
    	assertEquals(t.getAcceptableDeviation(), accdev);
    	assertEquals(t.getAlternativeTask(), null);
    	assertEquals(t.getPrerequisiteTasks().length, 0);
    }
    
    /**
     * Test createTask method with only alternative (no dependencies).
     */
    @Test
    public void testCreateTaskAlternative() {
    	t1.update(start, end, Status.FAILED);
    	
    	int altFor = t1.getId();
    	Task t = p1.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    	
    	assertTrue(p1.getTasks().contains(t));
    	assertEquals(t.getDescription(), taskdescr);
    	assertEquals(t.getEstimatedDuration().getHours(), estdurH);
    	assertEquals(t.getEstimatedDuration().getMinutes(), estdurM);
    	assertEquals(t.getAcceptableDeviation(), accdev);
    	assertEquals(t1.getAlternativeTask(), t);
    	assertEquals(t.getAlternativeTask(), null);
    	assertEquals(t.getPrerequisiteTasks().length, 0);
    }
    
    /**
     * Test createTask method with only dependencies (no alternative).
     */
    @Test
    public void testCreateTaskPrereqs() {
    	int[] prereqs = new int[]{t2.getId(), t3.getId()};
    	Task t = p2.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    	
    	assertTrue(p2.getTasks().contains(t));
    	assertEquals(t.getDescription(), taskdescr);
    	assertEquals(t.getEstimatedDuration().getHours(), estdurH);
    	assertEquals(t.getEstimatedDuration().getMinutes(), estdurM);
    	assertEquals(t.getAcceptableDeviation(), accdev);
    	assertEquals(t.getAlternativeTask(), null);
    	assertArrayEquals(t.getPrerequisiteTasks(), new Task[]{t2, t3});
    }
    
    /**
     * Test createTask method with false alternative (from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidAlternative() {
    	t1.update(start, end, Status.FAILED);
    	
    	int altFor = t1.getId();
    	p2.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    }
    
    /**
     * Test createTask method with false prereqs (from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidPrereqs() {
    	int[] prereqs = new int[]{t2.getId(), t3.getId()};
    	p1.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    }
    
    /**
     * Test createTask method with partially false prereqs (only one from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidPrereqs2() {
    	int[] prereqs = new int[]{t1.getId(), t2.getId()};
    	p2.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    }
    
    /**
     * Test createTask method when project is finished.
     */
    @Test (expected = IllegalStateException.class)
    public void testCreateTaskFinished() {
    	pFinished.createTask(taskdescr, estdurH, estdurM, accdev, altFor, prereqs);
    }
    
    /**
     * Test getAvailableTasks method in simple cases.
     */
    @Test
    public void testGetAvailableTasksSimple() {
    	assertTrue(p0.getAvailableTasks().isEmpty());
    	
    	assertEquals(p1.getAvailableTasks().size(), 1);
    	assertTrue(p1.getAvailableTasks().contains(t1));
    	
    	assertEquals(p2.getAvailableTasks().size(), 2);
    	assertTrue(p2.getAvailableTasks().contains(t2));
    	assertTrue(p2.getAvailableTasks().contains(t3));
    }
    
    /**
     * Test getAvailableTasks method with alternative.
     */
    @Test
    public void testGetAvailableTasksAlternative() {
    	t1.update(start, end, Status.FAILED);
    	Task alt = p1.createTask(taskdescr, estdurH, estdurM, accdev, t1.getId(), prereqs);
    	assertEquals(p1.getAvailableTasks().size(), 1);
    	assertFalse(p1.getAvailableTasks().contains(t1));
    	assertTrue(p1.getAvailableTasks().contains(alt));
    }
    
    /**
     * Test getAvailableTasks method with prereqs.
     */
    @Test
    public void testGetAvailableTasksPrereqs() {
    	Task prereq = p2.createTask(taskdescr, estdurH, estdurM, accdev, altFor, new int[]{t2.getId(), t3.getId()});
    	assertEquals(p2.getAvailableTasks().size(), 2);
    	assertTrue(p2.getAvailableTasks().contains(t2));
    	assertTrue(p2.getAvailableTasks().contains(t3));
    	assertFalse(p2.getAvailableTasks().contains(prereq));
    	
    	t2.update(start, end, Status.FINISHED);
    	t3.update(start, end, Status.FINISHED);
    	assertEquals(p2.getAvailableTasks().size(), 1);
    	assertFalse(p2.getAvailableTasks().contains(t2));
    	assertFalse(p2.getAvailableTasks().contains(t3));
    	assertTrue(p2.getAvailableTasks().contains(prereq));
    }
    
    /**
     * Test getAvailableTasks method when project is already finished.
     */
    @Test
    public void testGetAvailableTasksFinished() {
    	assertTrue(pFinished.getAvailableTasks().isEmpty());
    }
    
    /**
     * Test getAvailableTasks method when project is already finished.
     */
    @Test
    public void testCanhaveAsTask() {
    	assertFalse(p0.canHaveAsTask(null));
    }
    
    /**
     * Test isFinished method in trivial cases.
     */
    @Test
    public void testIsFinished() {
    	//Project without tasks can't be finished.
    	assertFalse(p0.isFinished());
    	
    	assertFalse(p1.isFinished());
    	t1.update(start, end, Status.FINISHED);
    	assertTrue(p1.isFinished());
    	
    	assertFalse(p2.isFinished());
    	t2.update(start, end, Status.FINISHED);
    	assertFalse(p2.isFinished());
    	t3.update(start, end, Status.FINISHED);
    	assertTrue(p2.isFinished());
    	
    	assertTrue(pFinished.isFinished());
    }
    
    /**
     * Test isFinished method in case of alternative.
     */
    @Test
    public void testIsFinishedAlternative() {
    	assertFalse(p1.isFinished());
    	t1.update(start, end, Status.FAILED);
    	assertFalse(p1.isFinished());
    	Task t = p1.createTask(taskdescr, estdurH, estdurM, accdev, t1.getId(), prereqs);
    	assertFalse(p1.isFinished());
    	t.update(start, end, Status.FINISHED);
    	assertTrue(p1.isFinished());
    }
    
    /**
     * Test isFinished method in case of prereqs.
     */
    @Test
    public void testIsFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdurH, estdurM, accdev, altFor, new int[]{t2.getId(), t3.getId()});
    	assertFalse(p2.isFinished());
    	t2.update(start, end, Status.FINISHED);
    	assertFalse(p2.isFinished());
    	t3.update(start, end, Status.FINISHED);
    	assertFalse(p2.isFinished());
    	t.update(end, end.plusDays(5), Status.FINISHED);
    	assertTrue(p2.isFinished());
    }
    
    /**
     * Test isOnTime method in trivial cases when simple project is finished (no alternatives or prereqs).
     */
    @Test
    public void testIsOnTimeFinishedSimple() {
    	assertTrue(pFinished.isOnTime());
    	
    	t1.update(start, due.plusDays(1), Status.FINISHED);
    	assertFalse(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no prereqs).
     */
    @Test
    public void testIsOnTimeFinishedAlternative() {
    	t1.update(start, end, Status.FAILED);
    	Task t = p1.createTask(taskdescr, estdurH, estdurM, accdev, t1.getId(), prereqs);
    	t.update(start, due, Status.FINISHED);
    	assertTrue(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no prereqs).
     */
    @Test
    public void testIsOnTimeFinishedAlternative2() {
    	t1.update(start, due.plusHours(1), Status.FAILED);
    	Task t = p1.createTask(taskdescr, estdurH, estdurM, accdev, t1.getId(), prereqs);
    	t.update(start, due, Status.FINISHED);
    	assertFalse(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no alternative).
     */
    @Test
    public void testIsOnTimeFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdurH, estdurM, accdev, altFor, new int[]{t2.getId(), t3.getId()});
    	t2.update(start, due, Status.FINISHED);
    	t3.update(start, end, Status.FINISHED);
    	t.update(due, due.plusHours(1), Status.FINISHED);
    	assertFalse(p2.isOnTime());
    }
    
}
