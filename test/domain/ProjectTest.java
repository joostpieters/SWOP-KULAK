package domain;

import exception.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse; 
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.command.CreateTaskCommand;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.WorkWeekConfiguration;

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
	
	ProjectContainer pc;
	Project p0, p1, p2, pFinished;
	Task t1, t2, t3, tFin;
    private Clock clock;
    
    private WorkWeekConfiguration wwc_default;
    
    @Before
    public void setUp() {
    	assertTrue(!create.isAfter(start));
    	assertTrue(!end.isAfter(due));
    	
    	pc = new ProjectContainer();
    	clock = new Clock(create);
    	
    	wwc_default = new WorkWeekConfiguration(1, 5, LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(12, 0), LocalTime.of(13, 0));
    	
    	p0 = pc.createProject(name, descr, create, due);
    	
    	p1 = pc.createProject(name, descr, create, due);
    	t1 = p1.createTask("design system", new Duration(8, 0), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	
    	p2 = pc.createProject(name, descr, create, due);
    	t2 = p2.createTask("design system", new Duration(8, 0), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	t3 = p2.createTask("implement system in native code", new Duration(16, 0), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	
    	LocalDateTime hist = create.minusDays(DAYDIF);
    	pFinished = pc.createProject(name, descr, hist, create);
    	tFin = pFinished.createTask("design system", new Duration(8, 0), 0, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	tFin.update(hist, hist.plusHours(HOURDIF), new Finished(), clock.getTime());
    }
    
    /**
     * Test constructor with valid arguments
     */
    @Test
    public void testConstructorValid() {
    	Project x = new Project(name, descr, create, due);
    	
    	assertEquals(name, x.getName());
    	assertEquals(descr, x.getDescription());
    	x.getCreationTime().minusHours(2);
    	assertEquals(create, x.getCreationTime());
    	x.getDueTime().minusHours(2);
    	assertEquals(due, x.getDueTime());
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
    	
    	new Project(name, descr, create, due);
    }
    
    /**
     * Test constructor with null-name.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
    	String name = null;
    	
    	new Project(name, descr, create, due);
    }
    
    /**
     * Test constructor with empty name.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
    	String name = "";
    	
    	new Project(name, descr, create, due);
    }
    
    /**
     * Test constructor with null-description.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullDescr() {
    	String descr = null;
    	
    	new Project(name, descr, create, due);
    }
    
    /**
     * Test constructor with insufficient description.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorEmptyDescr() {
    	String descr = "";
    	
    	new Project(name, descr, create, due);
    }
    
    /**
     * Test getAvailableTasks method when project is already finished.
     */
    @Test
    public void testCanhaveAsTask() {
    	assertFalse(p0.canHaveAsTask(null));
    	Task t = p0.createTask(taskdescr, estdur, accdev, altFor, prereqs, new HashMap<>());
    	assertFalse(p0.canHaveAsTask(t));
    	assertFalse(p0.canHaveAsTask(tFin));
    }
    
    /**
     * Test createTask method with simple valid parameters (without alternative or dependencies).
     */
    @Test
    public void testCreateTaskSimple() {
    	Task t = p0.createTask(taskdescr, estdur, accdev, altFor, prereqs, new HashMap<>());
    	
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
    	//pm.advanceSystemTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	
    	int altFor = t1.getId();
    	Task t = p1.createTask(taskdescr, estdur, accdev, altFor, prereqs, new HashMap<>());
    	
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
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()), new HashMap<>());
    	
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
    	//pm.advanceSystemTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	
    	int altFor = t1.getId();
    	p2.createTask(taskdescr, estdur, accdev, altFor, prereqs, new HashMap<>());
    }
    
    /**
     * Test createTask method with false prereqs (from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidPrereqs() {
    	p1.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()), new HashMap<>());
    }
    
    /**
     * Test createTask method with partially false prereqs (only one from other project).
     */
    @Test (expected = ObjectNotFoundException.class)
    public void testCreateTaskInvalidPrereqs2() {
    	p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t1.getId(), t2.getId()), new HashMap<>());
    }
    
    /**
     * Test createTask method when project is finished.
     */
    @Test (expected = IllegalStateException.class)
    public void testCreateTaskFinished() {
    	pFinished.createTask(taskdescr, estdur, accdev, altFor, prereqs, new HashMap<>());
    }

    //TODO: irrelevant dus weg?
//    /**
//     * Test updateTask method with valid parameters.
//     */
//    @Test
//    public void testUpdateTaskValid() {
//    	//pm.advanceSystemTime(end);
//    	t1.update(start, end, new Finished(), clock.getTime());
//    	assertEquals(start, t1.getTimeSpan().getStartTime());
//    	assertEquals(end, t1.getTimeSpan().getEndTime());
//    	assertEquals(new Finished(), t1.getStatus());
//    	t2.update(start, end, new Failed(), clock.getTime());
//    	assertEquals(start, t2.getTimeSpan().getStartTime());
//    	assertEquals(end, t2.getTimeSpan().getEndTime());
//    	assertEquals(new Failed(), t2.getStatus());
//    	t3.update(start, end, new Finished(), clock.getTime());
//    	assertEquals(start, t3.getTimeSpan().getStartTime());
//    	assertEquals(end, t3.getTimeSpan().getEndTime());
//    	assertEquals(new Finished(), t3.getStatus());
//    }
//    
//    /**
//     * Test updateTask method with start time before creation of project.
//     */
//    @Test (expected = IllegalArgumentException.class)
//    public void testUpdateTaskInvalidStartTime() {
//    	p1.updateTask(t1.getId(), create.minusHours(1), end, new Failed());
//    }
//    
//    /**
//     * Test updateTask method with invalid id for a task.
//     */
//    @Test (expected = ObjectNotFoundException.class)
//    public void testUpdateTaskInvalidId() {
//    	//pm.advanceSystemTime(end);
//    	p2.updateTask(t1.getId(), start, end, new Finished());
//    }
    
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
    	//pm.advanceSystemTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	Task alt = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs, new HashMap<>());
    	assertEquals(1, p1.getAvailableTasks().size());
    	assertFalse(p1.getAvailableTasks().contains(t1));
    	assertTrue(p1.getAvailableTasks().contains(alt));
    }
    
    /**
     * Test getAvailableTasks method with prereqs.
     */
    @Test
    public void testGetAvailableTasksPrereqs() {
    	Task prereq = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()), new HashMap<>());
    	assertEquals(2, p2.getAvailableTasks().size());
    	assertTrue(p2.getAvailableTasks().contains(t2));
    	assertTrue(p2.getAvailableTasks().contains(t3));
    	assertFalse(p2.getAvailableTasks().contains(prereq));
    	
    	//pm.advanceSystemTime(end);
    	t2.update(start, end, new Finished(), clock.getTime());
    	t3.update(start, end, new Finished(), clock.getTime());
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
        
    	assertTrue(p0.getUnacceptablyOverdueTasks(clock).isEmpty());
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).isEmpty());
    	assertTrue(p2.getUnacceptablyOverdueTasks(clock).isEmpty());
    	assertTrue(pFinished.getUnacceptablyOverdueTasks(clock).isEmpty());
    	
    	Task t = p1.createTask(taskdescr, new Duration(create, due.plusDays(DAYDIF)), accdev, altFor, prereqs, new HashMap<>());
    	assertEquals(1, p1.getUnacceptablyOverdueTasks(clock).size());
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(t));
    	assertEquals((double) (DAYDIF - 2) / DAYDIF, (double) p1.getUnacceptablyOverdueTasks(clock).get(t), EPS); //weekend: -2
    	
    	Task tt = p1.createTask(taskdescr, new Duration(create, due.plusDays(1)), accdev, altFor, prereqs, new HashMap<>());
    	assertEquals(2, p1.getUnacceptablyOverdueTasks(clock).size());
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(t));
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(tt));
    	assertEquals((double) (DAYDIF - 2) / DAYDIF, (double) p1.getUnacceptablyOverdueTasks(clock).get(t), EPS); //weekend: -2
    	assertEquals((double) 1 / DAYDIF, (double) p1.getUnacceptablyOverdueTasks(clock).get(tt), EPS);
    	
    	clock.advanceTime(due);
    	assertTrue(p0.getUnacceptablyOverdueTasks(clock).isEmpty());
    	assertEquals(p1.getTasks().size(), p1.getUnacceptablyOverdueTasks(clock).size());
    	assertEquals(p2.getTasks().size(), p2.getUnacceptablyOverdueTasks(clock).size());
    	assertTrue(pFinished.getUnacceptablyOverdueTasks(clock).isEmpty());
    }
    
    /**
     * Test getUnacceptablyOverdueTasks method in case of alternative (no prereqs).
     */
    @Test
    public void testGetUnacceptablyOverdueTasksAlternative() {
    	clock.advanceTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	Task t = p1.createTask(taskdescr, new Duration(create, due), accdev, t1.getId(), prereqs, new HashMap<>());
    	assertEquals(1, p1.getUnacceptablyOverdueTasks(clock).size());
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(t));
    	assertEquals((double) new Duration(create, end).toMinutes() / (DAYDIF * wwc_default.getMinutesOfWorkDay()), 
    			(double) p1.getUnacceptablyOverdueTasks(clock).get(t), EPS);
    }
    
    /**
     * Test getUnacceptablyOverdueTasks method in case of prereqs (no alternative).
     */
    @Test
    public void testGetUnacceptablyOverdueTasksPrereqs() {
    	Task t = p1.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t1.getId()), new HashMap<>());
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).isEmpty());
    	clock.advanceTime(due);
    	t1.update(start, due, new Finished(), clock.getTime());
    	assertEquals(1, p1.getUnacceptablyOverdueTasks(clock).size());
    	assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(t));
    	assertEquals((double) estdur.toMinutes() / (DAYDIF * wwc_default.getMinutesOfWorkDay()), (double) p1.getUnacceptablyOverdueTasks(clock).get(t), EPS);
    } 
    
    /**
     * Test isFinished method in trivial cases.
     */
    @Test
    public void testIsFinished() {
    	//Project without tasks can't be finished.
    	assertFalse(p0.isFinished());
    	
    	assertFalse(p1.isFinished());
    	clock.advanceTime(end);
    	t1.update(start, end, new Finished(), clock.getTime());
    	assertTrue(p1.isFinished());
    	
    	assertFalse(p2.isFinished());
    	t2.update(start, end, new Finished(), clock.getTime());
    	assertFalse(p2.isFinished());
    	t3.update(start, end, new Finished(), clock.getTime());
    	assertTrue(p2.isFinished());
    	
    	assertTrue(pFinished.isFinished());
    }
    
    /**
     * Test isFinished method in case of alternative.
     */
    @Test
    public void testIsFinishedAlternative() {
    	assertFalse(p1.isFinished());
    	
    	t1.update(start, end, new Failed(), clock.getTime());
    	assertFalse(p1.isFinished());
    	Task t = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs, new HashMap<>());
    	assertFalse(p1.isFinished());
    	t.update(start, end, new Finished(), clock.getTime());
    	assertTrue(p1.isFinished());
    }
    
    /**
     * Test isFinished method in case of prereqs.
     */
    @Test
    public void testIsFinishedPrereqs() {
    	
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()), new HashMap<>());
    	assertFalse(p2.isFinished());
    	t2.update(start, end, new Finished(), clock.getTime());
    	assertFalse(p2.isFinished());
    	t3.update(start, end, new Finished(), clock.getTime());
    	assertFalse(p2.isFinished());
    	
    	t.update(end, end.plusDays(5), new Finished(), clock.getTime());
    	assertTrue(p2.isFinished());
    }
    
    /**
     * Test isOnTime method in trivial cases when simple project is finished (no alternatives or prereqs).
     */
    @Test
    public void testIsOnTimeFinishedSimple() {
    	assertTrue(pFinished.isOnTime(clock));
    	clock.advanceTime(due.plusDays(1));
    	t1.update(start, due.plusDays(1), new Finished(), clock.getTime());
    	assertFalse(p1.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no prereqs).
     */
    @Test
    public void testIsOnTimeFinishedAlternative() {
    	clock.advanceTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	Task t = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs, new HashMap<>());
    	clock.advanceTime(due);
    	t.update(start, due, new Finished(), clock.getTime());
    	assertTrue(p1.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no prereqs).
     */
    @Test
    public void testIsOnTimeFinishedAlternative2() {
    	clock.advanceTime(due.plusHours(1));
    	t1.update(start, due.plusHours(1), new Failed(), clock.getTime());
    	Task t = p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs, new HashMap<>());
    	t.update(start, due, new Finished(), clock.getTime());
    	assertFalse(p1.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method in trivial cases when project is finished (no alternative).
     */
    @Test
    public void testIsOnTimeFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()), new HashMap<>());
    	//pm.advanceSystemTime(due);
    	t2.update(start, due, new Finished(), clock.getTime());
    	t3.update(start, end, new Finished(), clock.getTime());
    	clock.advanceTime(due.plusHours(1));
    	t.update(due, due.plusHours(1), new Finished(), clock.getTime());
    	assertFalse(p2.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method in trivial cases when simple project is ongoing (no alternatives or prereqs).
     */
    @Test
    public void testIsOnTimeUnFinishedSimple() {
    	assertTrue(p0.isOnTime(clock));
    	assertTrue(p1.isOnTime(clock));
    	assertTrue(p2.isOnTime(clock));
    	
    	p1.createTask(taskdescr, new Duration(ProjectTest.DAYDIF*wwc_default.getMinutesOfWorkDay()).add(10), accdev, altFor, prereqs, new HashMap<>());
    	assertFalse(p1.isOnTime(clock));
    	
    	clock.advanceTime(due);
    	assertTrue(p0.isOnTime(clock));
    	assertFalse(p1.isOnTime(clock));
    	assertTrue(p2.isOnTime(clock));
    	
    	clock.advanceTime(due.plusHours(1));
    	assertFalse(p0.isOnTime(clock));
    	assertFalse(p1.isOnTime(clock));
    	assertFalse(p2.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method when ongoing project contains failed task.
     */
    @Test
    public void testIsOnTimeUnFinishedFailedTask() {
    	clock.advanceTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	assertTrue(p1.isOnTime(clock));
    	
    	t2.update(start, end, new Failed(), clock.getTime());
    	assertTrue(p2.isOnTime(clock));
    	clock.advanceTime(due.plusDays(1));
    	t3.update(start, due.plusDays(1), new Failed(), clock.getTime());
    	assertFalse(p2.isOnTime(clock));
    	
    	assertFalse(p1.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method when ongoing project in case of alternative tasks.
     */
    @Test
    public void testIsOnTimeUnFinishedAlternative() {
    	clock.advanceTime(end);
    	t1.update(start, end, new Failed(), clock.getTime());
    	p1.createTask(taskdescr, estdur, accdev, t1.getId(), prereqs, new HashMap<>());
    	assertTrue(p1.isOnTime(clock));
    	
    	t2.update(start, end, new Failed(), clock.getTime());
    	assertTrue(p2.isOnTime(clock));
    	p2.createTask(taskdescr, new Duration(ProjectTest.DAYDIF*wwc_default.getMinutesOfWorkDay()), accdev, t2.getId(), prereqs, new HashMap<>());
    	assertFalse(p2.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method when ongoing project in case of prereqs.
     */
    @Test
    public void testIsOnTimeUnFinishedPrereqs() {
    	Task t = p2.createTask(taskdescr, estdur, accdev, altFor, Arrays.asList(t2.getId(), t3.getId()), new HashMap<>());
    	assertTrue(p2.isOnTime(clock));
    	p2.createTask(taskdescr, new Duration(ProjectTest.DAYDIF*wwc_default.getMinutesOfWorkDay()), accdev, altFor, Arrays.asList(t.getId()), new HashMap<>());
    	assertFalse(p2.isOnTime(clock));
    }
    
    /**
     * Test isOnTime method in strange example-case.
     */
    @Test
    public void testIsOnTimeExample() {
    	LocalDateTime create = LocalDateTime.of(2015, 2, 9, 8, 0);
    	LocalDateTime due = LocalDateTime.of(2015, 2, 13, 19, 0);
    	
    	Project p = pc.createProject(name, descr, create, due);
    	Task t1 = p.createTask("design system", new Duration(8,0), 0, altFor, prereqs, new HashMap<>());
    	Task t2 = p.createTask("implement system in native code", new Duration(16,0), 50, altFor, Arrays.asList(t1.getId()), new HashMap<>());
    	Task t3 = p.createTask("test system", new Duration(8,0), 0, altFor, Arrays.asList(t2.getId()), new HashMap<>());
        
    	Task t4 = p.createTask("write documentation", new Duration(8,0), 0, altFor, Arrays.asList(t2.getId()), new HashMap<>());
    	
    	clock.advanceTime(create);
    	assertTrue(p.isOnTime(clock));
    	clock.advanceTime(create.plusDays(1));
    	t1.update(LocalDateTime.of(2015, 2, 9, 9, 0), LocalDateTime.of(2015,  2, 9, 18, 0), new Finished(), clock.getTime());
    	assertTrue(p.isOnTime(clock));
    	clock.advanceTime(create.plusDays(2));
    	t2.update(LocalDateTime.of(2015, 2, 10, 9, 0), LocalDateTime.of(2015,  2, 10, 18, 0), new Failed(), clock.getTime());
        
    	assertTrue(p.isOnTime(clock));
    	Task t5 = p.createTask("implement system with phonegap", new Duration(17,0), 100, t2.getId(), Arrays.asList(t1.getId()), new HashMap<>());
    	assertFalse(p.isOnTime(clock)); //FIXME: why for god's sake should this hold in case of 8-hour-duration, it should be true up to 16-hour-durations?
    	clock.advanceTime(due);
    	t5.update(LocalDateTime.of(2015,  2, 11, 9, 0), LocalDateTime.of(2015, 2, 11, 18, 0), new Finished(), clock.getTime());
       
    	t3.update(LocalDateTime.of(2015,  2, 12, 9, 0), LocalDateTime.of(2015, 2, 12, 18, 0), new Finished(), clock.getTime());
    	t4.update(LocalDateTime.of(2015,  2, 13, 9, 0), LocalDateTime.of(2015, 2, 13, 18, 0), new Finished(), clock.getTime());
    	assertTrue(p.isOnTime(clock));
    }
    
    /**
     * Test getDelay method in case project is on time.
     */
    @Test
    public void testGetDelayOnTime() {
    	assertEquals(Duration.ZERO, p0.getDelay(clock));
    	assertEquals(Duration.ZERO, p1.getDelay(clock));
    	assertEquals(Duration.ZERO, p2.getDelay(clock));
    	assertEquals(Duration.ZERO, pFinished.getDelay(clock));
    	
    	LocalDateTime end = estdur.getEndTimeFrom(start);
    	clock.advanceTime(end);
    	t1.update(start, end, new Finished(), clock.getTime());
    	assertFalse(end.isAfter(due));
    	assertEquals(Duration.ZERO, p1.getDelay(clock));
    	
    	clock.advanceTime(end.plusDays(1));
    	t2.update(start, end.plusDays(1), new Finished(), clock.getTime());
    	assertEquals(Duration.ZERO, p2.getDelay(clock));
    	clock.advanceTime(end.plusDays(2));
    	t3.update(start, end.plusDays(2), new Finished(), clock.getTime());
    	assertEquals(Duration.ZERO, p2.getDelay(clock));
    }
    
    /**
     * Test getDelay method in case project is not on time.
     */
    @Test
    public void testGetDelayNotOnTime() {
    	clock.advanceTime(due.plusDays(1));
    	t1.update(start, due.plusDays(1), new Finished(), clock.getTime());
    	assertEquals(t1.getDelay(), p1.getDelay(clock));
    	
    	t2.update(start, end.plusDays(1), new Failed(), clock.getTime());
    	t3.update(start,  due.plusDays(1), new Finished(), clock.getTime());
    	assertEquals(t2.getDelay().add(t3.getDelay()), p2.getDelay(clock));
    }
    
    /**
     * Test getTotalExecutionTime method.
     */
    @Test
    public void testGetTotalExecutionTime() {
    	assertEquals(Duration.ZERO, p0.getTotalExecutionTime());
    	assertEquals(Duration.ZERO, p1.getTotalExecutionTime());
    	assertEquals(Duration.ZERO, p2.getTotalExecutionTime());
    	assertEquals(tFin.getTimeSpan().getDuration(), pFinished.getTotalExecutionTime());
    	
    	clock.advanceTime(end);
    	t1.update(start, end, new Finished(), clock.getTime());
    	assertEquals(t1.getTimeSpan().getDuration(), p1.getTotalExecutionTime());
    	
    	Duration dur = new Duration(13, 30);
    	clock.advanceTime(dur.getEndTimeFrom(start));
    	t2.update(start, dur.getEndTimeFrom(start), new Finished(), clock.getTime());
    	assertEquals(dur, p2.getTotalExecutionTime());
    	t3.update(start, end, new Failed(), clock.getTime());
    	assertEquals(new Duration(start, end).add(dur), p2.getTotalExecutionTime());
    }
    
}
