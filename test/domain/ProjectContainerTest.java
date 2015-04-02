package domain;

import exception.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test class for projectContainer
 * 
 * @author Mathias Benoit
 */
public class ProjectContainerTest {
    private static Project p1, p2, p3;
    private static Task t1, t2, t3;
    
    private static ProjectContainer manager;
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectContainer();
        manager.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        // available
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        // unavailable
        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(0));
        
        p2 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        //available
        t3 =  p2.createTask("Another difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        p2.updateTask(t3.getId(), p2.getCreationTime(), p2.getDueTime(), new Finished());
        p3 = manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
    }

    /**
     * Test of getProject method, of class ProjectContainer if the id doesn't exists.
     */
    @Test (expected=ObjectNotFoundException.class)
    public void testGetProjectFail() throws ObjectNotFoundException{
        ProjectContainer instance = new ProjectContainer();
        instance.getProject(12);
    }
    
    /**
     * Test of getProject and create project method, of class ProjectContainer.
     */
    @Test
    public void testGetProjectAndCreateProject() throws ObjectNotFoundException{
        ProjectContainer projectContainer = new ProjectContainer();
        Project project1 = projectContainer.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
        int pId = project1.getId();
        Project project2 = projectContainer.getProject(pId);
        assertEquals(project1, project2);
    }
    
    /**
     * Test of getAllavailableTasks, of class ProjectContainer.
     */
    @Test
    public void testGetAvailableTasks(){
                
        assertFalse(manager.getAllAvailableTasks().containsKey(t3));
        assertTrue(manager.getAllAvailableTasks().containsKey(t1));
        assertFalse(manager.getAllAvailableTasks().containsKey(t2));
    }
    
    /**
     * Test of getAllTasks, of class ProjectContainer.
     */
    @Test
    public void testGetAllTasks(){
                
        assertTrue(manager.getAllTasks().contains(t3));
        assertTrue(manager.getAllTasks().contains(t1));
        assertTrue(manager.getAllTasks().contains(t2));
    }
    
    /**
     * Test of getUnfinishedProjects, of class ProjectContainer.
     */
    @Test
    public void testGetUnfinishedProjects(){
                
        assertTrue(manager.getUnfinishedProjects().contains(p1));
        assertTrue(manager.getUnfinishedProjects().contains(p3));
        assertFalse(manager.getUnfinishedProjects().contains(p2));
    }
    
    /**
     * Test of getNbProjects, of class ProjectContainer.
     */
    @Test
    public void testGetNbProjects(){
                
        assertEquals(4, manager.getNbProjects());
    }
    
}
