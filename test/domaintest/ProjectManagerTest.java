package domaintest;

import domain.Duration;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import exception.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;

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
    private static Project p1;
    private static Project p0;
    private static Task t1;
    private static Task t2;
    private static Project p2;
    private static Project p3;
    private static Task t3;
    
    public ProjectManagerTest() {
    }
    
    private static ProjectManager manager;
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectManager();
        p0 = manager.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(0));
        
        p2 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        
        t3 =  p2.createTask("Another difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        p2.updateTask(t3.getId(), p2.getCreationTime(), p2.getDueTime(), Status.FINISHED);
        p3 = manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
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
        ProjectManager projectManager = new ProjectManager();
        Project project1 = projectManager.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
        int pId = project1.getId();
        Project project2 = projectManager.getProject(pId);
        assertEquals(project1, project2);
    }
    
    /**
     * Test of getAllavailableTasks, of class ProjectManager.
     */
    @Test
    public void testGetAvailableTasks(){
                
        assertFalse(manager.getAllAvailableTasks().containsKey(t3));
        assertTrue(manager.getAllAvailableTasks().containsKey(t1));
        assertFalse(manager.getAllAvailableTasks().containsKey(t2));
    }
    
    /**
     * Test of getAllTasks, of class ProjectManager.
     */
    @Test
    public void testGetAllTasks(){
                
        assertTrue(manager.getAllTasks().contains(t3));
        assertTrue(manager.getAllTasks().contains(t1));
        assertTrue(manager.getAllTasks().contains(t2));
    }
    
    /**
     * Test of getUnfinishedProjects, of class ProjectManager.
     */
    @Test
    public void testGetUnfinishedProjects(){
                
        assertTrue(manager.getUnfinishedProjects().contains(p1));
        assertTrue(manager.getUnfinishedProjects().contains(p3));
        assertFalse(manager.getUnfinishedProjects().contains(p2));
    }
    
    /**
     * Test of getNbProjects, of class ProjectManager.
     */
    @Test
    public void testGetNbProjects(){
                
        assertEquals(4, manager.getNbProjects());
    }
    
    /**
     * Test of testAdvanceSystemTime, of class ProjectManager.
     */
    @Test
    public void testAdvanceSystemTime(){
        manager.advanceSystemTime(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, manager.getSystemClock().getTime());
    }
}
