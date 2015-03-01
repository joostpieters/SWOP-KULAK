package domaintest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.SortedMap;

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
 *
 * @author Mathias
 */
public class ProjectTest {
	
	public static double EPS = 1e-15;
	
	private int id = 0;
	private String name = "Mobile Steps";
	private String descr = "develop mobile app for counting steps using a specialized bracelet";
	private LocalDateTime create = LocalDateTime.of(2015, 2, 9, 0, 0);
	private LocalDateTime due = LocalDateTime.of(2015, 2, 13, 0, 0);
	
	private Project p = new Project(id, name, descr, create, due);

	//TODO: easy constructors!!!
	private Task t1 = new Task("bla1", 8, 0f, new ArrayList<Task>(), Status.AVAILABLE);
	private Task t2 = new Task("bla2", 8, 0.1f, new ArrayList<Task>(), Status.AVAILABLE);
	private Task t3;
    private Task t4;
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

    	ArrayList<Task> prereq = new ArrayList<>();
    	prereq.add(t1);
    	prereq.add(t2);
    	t3 = new Task("bla3", 7, 0.2f, prereq, Status.UNAVAILABLE);
    	
    	//TODO: add alternative!!!
    	t4 = new Task("bla4", 9, 0.3f, new ArrayList<>(), Status.AVAILABLE);
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
    	assertTrue(p.getTasks().containsKey(t1.getId()));
    	assertTrue(p.getTasks().containsValue(t1));
    	assertFalse(p.getTasks().containsKey(t2.getId()));
    	assertFalse(p.getTasks().containsValue(t2));
    	assertFalse(p.getTasks().containsKey(-1));
    	assertFalse(p.getTasks().containsValue(null));
    	
    	p.addTask(t2);
    	assertTrue(p.getTasks().containsKey(t1.getId()));
    	assertTrue(p.getTasks().containsValue(t1));
    	assertTrue(p.getTasks().containsKey(t2.getId()));
    	assertTrue(p.getTasks().containsValue(t2));
    	assertFalse(p.getTasks().containsKey(-1));
    	assertFalse(p.getTasks().containsValue(null));
    }
    
    /**
     * Test addTask method with null-task.
     */
    @Test (expected = IllegalArgumentException.class)
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
    	
    	assertTrue(p.getTasks().containsKey(t3.getId()));
    	assertTrue(p.getTasks().containsValue(t3));
    	assertFalse(p.getTasks().containsKey(-1));
    	assertFalse(p.getTasks().containsValue(null));
    }
    
    /**
     * Test addTask method with alternative for existing task in project.
     */
    @Test
    public void testAddTaskAlternative() {
    	//TODO: implement
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
    	p.addTask(t1);
    	p.addTask(t2);
    	
    	String descr = "design system";
    	int estDur = 8;
    	int accDev = 20;
    	//TODO: how to indicate there are no alternatives?
    	int alternative = -1;
    	//TODO: how to indicate there are no prereqs?
    	int[] prereq = new int[]{};
    	Status stat = Status.UNAVAILABLE;
    	p.createTask(descr, estDur, accDev, alternative, prereq, stat);
    	
    	SortedMap<Integer, Task> tasks = p.getTasks();
    	assertFalse(tasks.isEmpty());
    	
    	Task added = tasks.get(tasks.firstKey());
    	assertEquals(added.getDescription(), descr);
    	assertEquals(added.getEstimatedDuration(), estDur);
    	assertEquals(added.getAcceptableDeviation(), (double) accDev / 100, EPS);
    	//TODO: is this the expected behaviour?
    	assertEquals(added.getAlternativeTask(), null);
    	assertTrue(added.getPrerequisiteTasks().isEmpty());
    	
    }
    
}
