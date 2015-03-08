package domaintest;

import java.time.LocalDateTime;

import domain.Project;

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
	
	private int id = 0;
	private String name = "Mobile Steps";
	private String descr = "develop mobile app for counting steps using a specialized bracelet";
	private LocalDateTime create = LocalDateTime.of(2015, 2, 9, 0, 0);
	private LocalDateTime due = LocalDateTime.of(2015, 2, 13, 0, 0);
	Project p0, p1, p2, pFinished;
	
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
    	//TODO: hoe in gods naam?
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
    
    //TODO: opnieuw beginnen als ik eindelijk eens weet wat ik wil.
    
}
