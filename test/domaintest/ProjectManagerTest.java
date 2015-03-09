package domaintest;

import domain.Project;
import domain.ProjectManager;
import exception.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test class for projectmanager
 * 
 * @author Mathias Benoit
 */
public class ProjectManagerTest {
    
    public ProjectManagerTest() {
    }
    
    private static ProjectManager manager;
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectManager();
        manager.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
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
     * Test of getProject method, of class ProjectManager if the id doesn't exists.
     */
    @Test (expected=ObjectNotFoundException.class)
    public void testGetProjectFail() throws ObjectNotFoundException{
        ProjectManager instance = new ProjectManager();
        instance.getProject(12);
    }
    
    /**
     * Test of getProject and create project method, of class ProjectManager.
     */
    @Test
    public void testGetProjectAndCreateProject() throws ObjectNotFoundException{
        Project project1 = manager.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
        int pId = project1.getId();
        Project project2 = manager.getProject(pId);
        assertEquals(project1, project2);
    }
}
