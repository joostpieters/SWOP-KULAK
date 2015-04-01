package scenariotest;

import controller.HandlerFactory;
import controller.ShowProjectHandler;
import domain.Available;
import domain.Clock;
import domain.DetailedProject;
import domain.DetailedTask;
import domain.Duration;
import domain.Project;
import domain.ProjectContainer;
import domain.Task;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class ShowProjectScenarioTest {
    
    private static ProjectContainer manager;
    private static ShowProjectHandler handler;
    private static Project p1, p2, p3;
    private static Task t1, t2;
    private static Clock clock;
    
    public ShowProjectScenarioTest() {
    }
    
    @BeforeClass
    public static void setUpBeforeClass() {
        manager = new ProjectContainer();
        // only p1 has tasks
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()));
        
        p2 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p3 = manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        
        clock = new Clock();
        HandlerFactory controller = new HandlerFactory(manager, clock);
        handler = controller.getShowProjectHandler();
    }

    /**
     * Test of main method, of class Scenario1test.
     */
    @Test
    public void testShowProjects() {
        //step 2
        List<DetailedProject> projects = handler.getProjects();
        assertTrue(projects.contains(p1));
        assertTrue(projects.contains(p2));
        assertTrue(projects.contains(p3));
        assertTrue(projects.size() == 3);
        
        //step 3, select project 1
        handler.selectProject(p1.getId());
        
        //step 4, show details of p1
        DetailedProject p1Details = handler.getProject();
        assertEquals("Mobile Steps", p1Details.getName());
        assertEquals("A description.", p1Details.getDescription());
        assertEquals(LocalDateTime.of(2015, 3, 12, 17, 30), p1Details.getCreationTime());
        assertEquals(LocalDateTime.of(2015, 3, 22, 17, 50), p1Details.getDueTime());
        assertFalse(p1Details.isFinished());
        assertTrue(p1Details.isOnTime(clock));
        assertTrue(p1.getTasks().contains(t1));
        assertTrue(p1.getTasks().contains(t2));
        assertEquals(Duration.ZERO, p1Details.getDelay(clock));
        
        //step 5 select task t2
        DetailedTask t2Details = handler.getTask(t2.getId());
        assertEquals("A difficult task.", t2Details.getDescription());
        assertEquals(new Duration(500), t2Details.getEstimatedDuration());
        assertTrue(t2Details.getStatus() instanceof Available);
        assertEquals(Arrays.asList(t1), t2Details.getPrerequisiteTasks());
        assertEquals(50, t2Details.getAcceptableDeviation());
        
    }
    
}
