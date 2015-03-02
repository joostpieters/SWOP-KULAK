package domaintest;

import domain.ProjectManager;
import domain.Task;
import exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Mathias
 */
public class ProjectManagerTest {
    
    public ProjectManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getProject method, of class ProjectManager.
     */
    @Test (expected=ObjectNotFoundException.class)
    public void testGetProject() throws ObjectNotFoundException{
        System.out.println("getProject");
        ProjectManager instance = new ProjectManager();
        instance.getProject(12);
        
    }
}
