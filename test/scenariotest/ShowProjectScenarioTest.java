package scenariotest;

import controller.FrontController;
import controller.ShowProjectHandler;
import domain.DetailedProject;
import domain.DetailedTask;
import domain.Duration;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class ShowProjectScenarioTest {
    
    private static ProjectManager manager;
    private static ShowProjectHandler handler;
    private static Project p1;
    private static Project p2;
    private static Project p3;
    private static Task t1;
    private static Task t2;
    
    public ShowProjectScenarioTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectManager();
        // only p1 has tasks
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(0));
        
        p2 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p3 = manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        
        FrontController controller = new FrontController(manager);
        handler = controller.getShowProjectHandler();
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
     * Test of main method, of class Scenario1test.
     */
    @Test
    public void testShowProjects() {
        //step 2
        List<DetailedProject> projects = handler.getProjects();
        assertEquals(p1, projects.get(p1.getId()));
        assertEquals(p2, projects.get(p2.getId()));
        assertEquals(p3, projects.get(p3.getId()));
        
        //step 3, select project 1
        handler.selectProject(p1.getId());
        
        //step 4, show details of p1
        DetailedProject p1Details = handler.getProject();
        assertEquals("Mobile Steps", p1Details.getName());
        assertEquals("A description.", p1Details.getDescription());
        assertEquals(LocalDateTime.of(2015, 3, 12, 17, 30), p1Details.getCreationTime());
        assertEquals(LocalDateTime.of(2015, 3, 22, 17, 50), p1Details.getDueTime());
        // TODO more details
        
        //step 5 select task t2
        DetailedTask t2Details = handler.getTask(t2.getId());
        assertEquals("A difficult task.", t2Details.getDescription());
        assertEquals(new Duration(500), t2Details.getEstimatedDuration());
        assertEquals(Status.UNAVAILABLE, t2Details.getStatus());
        assertEquals(Arrays.asList(t1), t2Details.getPrerequisiteTasks());
        assertEquals(50, t2Details.getAcceptableDeviation());
        
    }
    
}
