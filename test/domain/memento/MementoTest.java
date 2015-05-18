package domain.memento;

import domain.Project;
import domain.ProjectContainer;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
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
public class MementoTest {
	
	private static Clock clock;
    private static Project p1, p2, p3;
    private static Task t1, t2, t3;
    
    private static ProjectContainer container;
    
    @BeforeClass
    public static void setUpClass() {
        container = new ProjectContainer();
        clock = new Clock(LocalDateTime.MAX);
        container.createProject("Test", "Description", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 12, 17, 50));
        p1 = container.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        // available
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.NO_REQUIRED_RESOURCE_TYPES);
        
        // unavailable
        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(0), Task.NO_REQUIRED_RESOURCE_TYPES);
        
        p2 = container.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        //available
        t3 =  p2.createTask("Another difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.NO_REQUIRED_RESOURCE_TYPES);
        
        t3.finish(new Timespan(p2.getCreationTime(), p2.getDueTime()), clock.getTime());
        p3 = container.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
    }

    /**
     * Test of getProject method, of class BranchOffice if the id doesn't exists.
     */
    @Test (expected=ObjectNotFoundException.class)
    public void testGetProjectFail() throws ObjectNotFoundException{
        ProjectContainer instance = new ProjectContainer();
        instance.getProject(12);
    }
    
    /**
     * Test of getProject and create project method, of class BranchOffice.
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
     * Test of getAllavailableTasks, of class BranchOffice.
     */
    @Test
    public void testGetAvailableTasks(){
                
        assertFalse(container.getAllAvailableTasks().containsKey(t3));
        assertTrue(container.getAllAvailableTasks().containsKey(t1));
        assertFalse(container.getAllAvailableTasks().containsKey(t2));
    }
    
    /**
     * Test of getAllTasks, of class BranchOffice.
     */
    @Test
    public void testGetAllTasks(){
                
        assertTrue(container.getAllTasks().contains(t3));
        assertTrue(container.getAllTasks().contains(t1));
        assertTrue(container.getAllTasks().contains(t2));
    }
    
    /**
     * Test of getUnfinishedProjects, of class BranchOffice.
     */
    @Test
    public void testGetUnfinishedProjects(){
                
        assertTrue(container.getUnfinishedProjects().contains(p1));
        assertTrue(container.getUnfinishedProjects().contains(p3));
        assertFalse(container.getUnfinishedProjects().contains(p2));
    }
    
    /**
     * Test of getNbProjects, of class BranchOffice.
     */
    @Test
    public void testGetNbProjects(){
                
        assertEquals(4, container.getNbProjects());
    }
    
}
