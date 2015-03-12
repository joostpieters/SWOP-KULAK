package domain;

import java.time.LocalDateTime;
import java.util.List;

import domain.Duration;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import exception.ObjectNotFoundException;
import java.util.Arrays;

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
	
	private static final double EPS = 1e-15;
	private static final int DAYDIF = 4;
	private static final int HOURDIF = 2;
	
	private String name = "Mobile Steps";
	private String descr = "develop mobile app for counting steps using a specialized bracelet";
	private LocalDateTime create = LocalDateTime.of(2015, 2, 9, 0, 0);
	private LocalDateTime due = create.plusDays(ProjectTest.DAYDIF);

	String taskdescr = "act cool";
	Duration estdur = new Duration(8,0);
	int accdev = 10;
	int altFor = Project.NO_ALTERNATIVE;
	List<Integer> prereqs = Project.NO_DEPENDENCIES;
	
	LocalDateTime start = LocalDateTime.of(2015, 2, 9, 15, 0);
	LocalDateTime end = start.plusHours(ProjectTest.HOURDIF);
	
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
    	assertTrue(!create.isAfter(start));
    	assertTrue(!end.isAfter(due));
    	
    	pm = new ProjectManager();
    	pm.advanceSystemTime(create);
    	
    	p0 = pm.createProject(name, descr, create, due);
    	
    	p1 = pm.createProject(name, descr, create, due);
    	t1 = p1.createTask("design system", new Duration(8), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	
    	p2 = pm.createProject(name, descr, create, due);
    	t2 = p2.createTask("design system", new Duration(8), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	t3 = p2.createTask("implement system in native code", new Duration(16), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	
    	pFinished = pm.createProject(name, descr, create, due);
    	tFin = pFinished.createTask("design system", new Duration(8), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
    	pFinished.updateTask(tFin.getId(), start, end, Status.FINISHED);
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
    	
    	assertEquals(name, x.getName());
    	assertEquals(descr, x.getDescription());
    	x.getCreationTime().minusHours(2);
    	assertEquals(create, x.getCreationTime());
    	x.getDueTime().minusHours(2);
    	assertEquals(due, x.getDueTime());
    	x.getClock().advanceTime(LocalDateTime.of(2015, 12, 31, 23, 59));
    	assertEquals(pm.getSystemClock(), x.getClock());
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
     * Test getAvailableTasks method when project is already finished.
     */
    @Test
    public void testCanhaveAsTask() {
    	assertFalse(p0.canHaveAsTask(null));
    	Task t = p0.createTask(taskdescr, estdur, accdev, altFor, prereqs);
    	assertFalse(p0.canHaveAsTask(t));
    	assertFalse(p0.canHaveAsTask(tFin));
    }
    
    /**
     * Test createTask method with simple valid parameters (without alternative or dependencies).
     */
    @Test
    public void testCreateTaskSimple() {
    	Task t = p0.createTask(taskdescr, estdur, accdev, altFor, prereqs);
    	
    	assertTrue(p0.getTasks().contains(t));
    	assertEquals(taskdescr, t.getDescription());
    	assertEquals(estdur.getHours(), t.getEstimatedDuration().getHours());
    	assertEquals(estdur.getMinutes(), t.getEstimatedDuration().getMinutes());
    	assertEquals(accdev, t.getAcceptableDeviation());
    	assertFalse(t.hasAlternativeTask());
    	assertEquals(0, t.getPrerequisiteTasks().size());
    }
    
    /**
     * Test createTask method with only alternative (no dependencies).
     */
    @Test
    public void testCreateTaskAlternative() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	
    	int altFor = t1.getId();
    	Task t = p1.createTask(taskdescr, estdur, accdev, altFor, prereqs);
    	
    	assertTrue(p1.getTasks().contains(t));
    	assertEquals(taskdescr, t.getDescription());
    	assertEquals(estdur.getHours(), t.getEstimatedDuration().getHours());
    	assertEquals(estdur.getMinutes(), t.getEstimatedDuration().getMinutes());
    	assertEquals(accdev, t.getAcceptableDeviation());
    	assertEquals(t, t1.getAlternativeTask());
    	assertFalse(t.hasAlternativeTask());
    	assertEquals(0, t.getPrerequisiteTasks().size());
    }
    
    /**
     * Test createTask method with only dependencies (no alternative).
     */
    @Test
    public void testCreateTaskPrereqs() {
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()));
    	
    	assertTrue(p2.getTasks().contains(t));
    	assertEquals(taskdescr, t.getDescription());
    	assertEquals(estdur.getHours(), t.getEstimatedDuration().getHours());
    	assertEquals(estdur.getMinutes(), t.getEstimatedDuration().getMinutes());
    	assertEquals(accdev, t.getAcceptableDeviation());
    	assertFalse(t.hasAlternativeTask());
    	assertArrayEquals(new Task[]{t2, t3}, t.getPrerequisiteTasks().toArray());
    }
    
    /**
     * Test createTask method with false alternative (from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidAlternative() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	
    	int altFor = t1.getId();
    	p2.createTask(taskdescr, estdur, accdev, altFor, prereqs);
    }
    
    /**
     * Test createTask method with false prereqs (from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidPrereqs() {
    	p1.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()));
    }
    
    /**
     * Test createTask method with partially false prereqs (only one from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidPrereqs2() {
    	p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t1.getId(), t2.getId()));
    }
    
    /**
     * Test createTask method when project is finished.
     */
    @Test (expected = IllegalStateException.class)
    public void testCreateTaskFinished() {
    	pFinished.createTask(taskdescr, estdur, accdev, altFor, prereqs);
    }
    
    /**
     * Test updateTask method with valid parameters.
     */
    @Test
    public void testUpdateTaskValid() {
    	p1.updateTask(t1.getId(), start, end, Status.FINISHED);
    	assertEquals(start, t1.getTimeSpan().getStartTime());
    	assertEquals(end, t1.getTimeSpan().getEndTime());
    	assertEquals(Status.FINISHED, t1.getStatus());
    	p2.updateTask(t2.getId(), start, end, Status.FAILED);
    	assertEquals(start, t2.getTimeSpan().getStartTime());
    	assertEquals(end, t2.getTimeSpan().getEndTime());
    	assertEquals(Status.FAILED, t2.getStatus());
    	p2.updateTask(t3.getId(), start, end, Status.FINISHED);
    	assertEquals(start, t3.getTimeSpan().getStartTime());
    	assertEquals(end, t3.getTimeSpan().getEndTime());
    	assertEquals(Status.FINISHED, t3.getStatus());
    }
    
    /**
     * Test updateTask method with start time before creation of project.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUpdateTaskInvalidStartTime() {
    	p1.updateTask(t1.getId(), create.minusHours(1), end, Status.FAILED);
    }
    
    /**
     * Test updateTask method with invalid id for a task.
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testUpdateTaskInvalidId() {
    	p2.updateTask(t1.getId(), start, end, Status.FINISHED);
    }
    
    /**
     * Test getAvailableTasks method in simple cases.
     */
    @Test
    public void testGetAvailableTasksSimple() {
    	assertTrue(p0.getAvailableTasks().isEmpty());
    	
    	assertEquals(1, p1.getAvailableTasks().size());
    	assertTrue(p1.getAvailableTasks().contains(t1));
    	
    	assertEquals(2, p2.getAvailableTasks().size());
    	assertTrue(p2.getAvailableTasks().contains(t2));
    	assertTrue(p2.getAvailableTasks().contains(t3));
    }
    
    /**
     * Test getAvailableTasks method with alternative.
     */
    @Test
    public void testGetAvailableTasksAlternative() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	Task alt = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs);
    	assertEquals(1, p1.getAvailableTasks().size());
    	assertFalse(p1.getAvailableTasks().contains(t1));
    	assertTrue(p1.getAvailableTasks().contains(alt));
    }
    
    /**
     * Test getAvailableTasks method with prereqs.
     */
    @Test
    public void testGetAvailableTasksPrereqs() {
    	Task prereq = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()));
    	assertEquals(2, p2.getAvailableTasks().size());
    	assertTrue(p2.getAvailableTasks().contains(t2));
    	assertTrue(p2.getAvailableTasks().contains(t3));
    	assertFalse(p2.getAvailableTasks().contains(prereq));
    	
    	p2.updateTask(t2.getId(), start, end, Status.FINISHED);
    	p2.updateTask(t3.getId(), start, end, Status.FINISHED);
    	assertEquals(1, p2.getAvailableTasks().size());
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
     * Test getUnacceptablyOverdueTasks method in simple case (no alternative or prereqs).
     */
    @Test
    public void testGetUnacceptablyOverdueTasksSimple() {
    	assertTrue(p0.getUnacceptablyOverdueTasks().isEmpty());
    	assertTrue(p1.getUnacceptablyOverdueTasks().isEmpty());
    	assertTrue(p2.getUnacceptablyOverdueTasks().isEmpty());
    	assertTrue(pFinished.getUnacceptablyOverdueTasks().isEmpty());
    	
    	Task t = p1.createTask(taskdescr, new Duration(create, due.plusDays(DAYDIF)), accdev, altFor, prereqs);
    	assertEquals(1, p1.getUnacceptablyOverdueTasks().size());
    	assertTrue(p1.getUnacceptablyOverdueTasks().containsKey(t));
    	assertEquals((double) (DAYDIF - 2) / DAYDIF, (double) p1.getUnacceptablyOverdueTasks().get(t), EPS); //weekend: -2
    	
    	Task tt = p1.createTask(taskdescr, new Duration(create, due.plusDays(1)), accdev, altFor, prereqs);
    	assertEquals(2, p1.getUnacceptablyOverdueTasks().size());
    	assertTrue(p1.getUnacceptablyOverdueTasks().containsKey(t));
    	assertTrue(p1.getUnacceptablyOverdueTasks().containsKey(tt));
    	assertEquals((double) (DAYDIF - 2) / DAYDIF, (double) p1.getUnacceptablyOverdueTasks().get(t), EPS); //weekend: -2
    	assertEquals((double) 1 / DAYDIF, (double) p1.getUnacceptablyOverdueTasks().get(tt), EPS);
    	
    	pm.advanceSystemTime(due);
    	assertTrue(p0.getUnacceptablyOverdueTasks().isEmpty());
    	assertEquals(p1.getTasks().size(), p1.getUnacceptablyOverdueTasks().size());
    	assertEquals(p2.getTasks().size(), p2.getUnacceptablyOverdueTasks().size());
    	assertEquals(pFinished.getTasks().size(), pFinished.getUnacceptablyOverdueTasks().size());
    }
    
    /**
     * Test getUnacceptablyOverdueTasks method in case of alternative (no prereqs).
     */
    @Test
    public void testGetUnacceptablyOverdueTasksAlternative() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	Task t = p1.createTask(taskdescr, new Duration(create, due).add(new Duration(HOURDIF, 0)), accdev, t1.getId(), prereqs);
    	assertEquals(1, p1.getUnacceptablyOverdueTasks().size());
    	assertTrue(p1.getUnacceptablyOverdueTasks().containsKey(t));
    	assertEquals((double) 60 * HOURDIF / (DAYDIF * Duration.getMinutesOfWorkDay()), (double) p1.getUnacceptablyOverdueTasks().get(t), EPS);
    }
    
    
    /**
     * Test isFinished method in trivial cases.
     */
    @Test
    public void testIsFinished() {
    	//Project without tasks can't be finished.
    	assertFalse(p0.isFinished());
    	
    	assertFalse(p1.isFinished());
    	p1.updateTask(t1.getId(), start, end, Status.FINISHED);
    	assertTrue(p1.isFinished());
    	
    	assertFalse(p2.isFinished());
    	p2.updateTask(t2.getId(), start, end, Status.FINISHED);
    	assertFalse(p2.isFinished());
    	p2.updateTask(t3.getId(), start, end, Status.FINISHED);
    	assertTrue(p2.isFinished());
    	
    	assertTrue(pFinished.isFinished());
    }
    
    /**
     * Test isFinished method in case of alternative.
     */
    @Test
    public void testIsFinishedAlternative() {
    	assertFalse(p1.isFinished());
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	assertFalse(p1.isFinished());
    	Task t = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs);
    	assertFalse(p1.isFinished());
    	p1.updateTask(t.getId(), start, end, Status.FINISHED);
    	assertTrue(p1.isFinished());
    }
    
    /**
     * Test isFinished method in case of prereqs.
     */
    @Test
    public void testIsFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()));
    	assertFalse(p2.isFinished());
    	p2.updateTask(t2.getId(), start, end, Status.FINISHED);
    	assertFalse(p2.isFinished());
    	p2.updateTask(t3.getId(), start, end, Status.FINISHED);
    	assertFalse(p2.isFinished());
    	p2.updateTask(t.getId(), end, end.plusDays(5), Status.FINISHED);
    	assertTrue(p2.isFinished());
    }
    
    /**
     * Test isOnTime method in trivial cases when simple project is finished (no alternatives or prereqs).
     */
    @Test
    public void testIsOnTimeFinishedSimple() {
    	assertTrue(pFinished.isOnTime());
    	
    	p1.updateTask(t1.getId(), start, due.plusDays(1), Status.FINISHED);
    	assertFalse(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no prereqs).
     */
    @Test
    public void testIsOnTimeFinishedAlternative() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	Task t = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs);
    	p1.updateTask(t.getId(), start, due, Status.FINISHED);
    	assertTrue(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no prereqs).
     */
    @Test
    public void testIsOnTimeFinishedAlternative2() {
    	p1.updateTask(t1.getId(), start, due.plusHours(1), Status.FAILED);
    	Task t = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs);
    	p1.updateTask(t.getId(), start, due, Status.FINISHED);
    	assertFalse(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no alternative).
     */
    @Test
    public void testIsOnTimeFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()));
    	p2.updateTask(t2.getId(), start, due, Status.FINISHED);
    	p2.updateTask(t3.getId(), start, end, Status.FINISHED);
    	p2.updateTask(t.getId(), due, due.plusHours(1), Status.FINISHED);
    	assertFalse(p2.isOnTime());
    }
    
    /**
     * Test isOnTime method in trivial cases when simple project is ongoing (no alternatives or prereqs).
     */
    @Test
    public void testIsOnTimeUnFinishedSimple() {
    	assertTrue(p0.isOnTime());
    	assertTrue(p1.isOnTime());
    	assertTrue(p2.isOnTime());
    	
    	p1.createTask(taskdescr, new Duration(ProjectTest.DAYDIF*Duration.getMinutesOfWorkDay()).add(10), accdev, altFor, prereqs);
    	assertFalse(p1.isOnTime());
    	
    	pm.advanceSystemTime(due);
    	assertTrue(p0.isOnTime());
    	assertFalse(p1.isOnTime());
    	assertTrue(p2.isOnTime());
    	
    	pm.advanceSystemTime(due.plusHours(1));
    	assertFalse(p0.isOnTime());
    	assertFalse(p1.isOnTime());
    	assertFalse(p2.isOnTime());
    }
    
    /**
     * Test isOnTime method when ongoing project contains failed task.
     */
    @Test
    public void testIsOnTimeUnFinishedFailedTask() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	assertTrue(p1.isOnTime());
    	
    	p2.updateTask(t2.getId(), start, end, Status.FAILED);
    	assertTrue(p2.isOnTime());
    	p2.updateTask(t3.getId(), start, Duration.nextValidWorkTime(due.plusDays(1)), Status.FAILED);
    	assertFalse(p2.isOnTime());
    	
    	pm.advanceSystemTime(due.plusHours(1));
    	assertFalse(p1.isOnTime());
    }
    
    /**
     * Test isOnTime method when ongoing project in case of alternative tasks.
     */
    @Test
    public void testIsOnTimeUnFinishedAlternative() {
    	p1.updateTask(t1.getId(), start, end, Status.FAILED);
    	p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs);
    	assertTrue(p1.isOnTime());
    	
    	p2.updateTask(t2.getId(), start, end, Status.FAILED);
    	assertTrue(p2.isOnTime());
    	p2.createTask(taskdescr, new Duration(ProjectTest.DAYDIF*Duration.getMinutesOfWorkDay()), accdev, t2.getId(), prereqs);
    	assertFalse(p2.isOnTime());
    }
    
    /**
     * Test isOnTime method when ongoing project in case of prereqs.
     */
    @Test
    public void testIsOnTimeUnFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()));
    	assertTrue(p2.isOnTime());
    	p2.createTask(taskdescr, new Duration(ProjectTest.DAYDIF*Duration.getMinutesOfWorkDay()), accdev, altFor, Arrays.asList(t.getId()));
    	assertFalse(p2.isOnTime());
    }
    
    /**
     * Test isOnTime method in strange example-case.
     */
    @Test
    public void testIsOnTimeExample() {
    	Project p = pm.createProject(name, descr, LocalDateTime.of(2015, 2, 9, 8, 0), LocalDateTime.of(2015, 2, 13, 19, 0));
    	Task t1 = p.createTask("design system", new Duration(8,0), 0, altFor, prereqs);
    	Task t2 = p.createTask("implement system in native code", new Duration(16,0), 50, altFor, Arrays.asList(t1.getId()));
    	Task t3 = p.createTask("test system", new Duration(8,0), 0, altFor, Arrays.asList(t2.getId()));
    	Task t4 = p.createTask("write documentation", new Duration(8,0), 0, altFor, Arrays.asList(t2.getId()));
    	
    	pm.advanceSystemTime(create);
    	assertTrue(p.isOnTime());
    	pm.advanceSystemTime(create.plusDays(1));
    	p.updateTask(t1.getId(), LocalDateTime.of(2015, 2, 9, 9, 0), LocalDateTime.of(2015,  2, 9, 18, 0), Status.FINISHED);
    	assertTrue(p.isOnTime());
    	pm.advanceSystemTime(create.plusDays(2));
    	p.updateTask(t2.getId(), LocalDateTime.of(2015, 2, 10, 9, 0), LocalDateTime.of(2015,  2, 10, 18, 0), Status.FAILED);
    	assertTrue(p.isOnTime());
    	Task t5 = p.createTask("implement system with phonegap", new Duration(17,0), 100, t2.getId(), Arrays.asList(t1.getId()));
    	assertFalse(p.isOnTime()); //FIXME: why for god's sake should this hold in case of 8-hour-duration, it should be true up to 16-hour-durations?
    	pm.advanceSystemTime(due);
    	p.updateTask(t5.getId(), LocalDateTime.of(2015,  2, 11, 9, 0), LocalDateTime.of(2015, 2, 11, 18, 0), Status.FINISHED);
    	p.updateTask(t3.getId(), LocalDateTime.of(2015,  2, 12, 9, 0), LocalDateTime.of(2015, 2, 12, 18, 0), Status.FINISHED);
    	p.updateTask(t4.getId(), LocalDateTime.of(2015,  2, 13, 9, 0), LocalDateTime.of(2015, 2, 13, 18, 0), Status.FINISHED);
    	assertTrue(p.isOnTime());
    }
    
}
