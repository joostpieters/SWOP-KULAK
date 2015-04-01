package scenariotest;

import controller.HandlerFactory;
import controller.UpdateTaskStatusHandler;
import domain.Clock;
import domain.Duration;
import domain.Project;
import domain.ProjectContainer;
import domain.Task;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;



/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class UpdateTaskStatusScenarioTest {
    
    private static ProjectContainer manager;
    private static UpdateTaskStatusHandler handler;
    private static Project p1;
    private static Task t1;
    private static Clock clock;
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectContainer();
        // only p1 has tasks
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()));
        
        manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        
        clock = new Clock();
        HandlerFactory controller = new HandlerFactory(manager, clock);
        handler = controller.getUpdateTaskHandler();
        clock.advanceTime(LocalDateTime.of(2015,03,17,14,10));
    }

    /**
     * Tests the main success scenario of the "Update Task Status" use case
     */
    @Test
    public void testMainSuccessScenario() {
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(LocalDateTime.of(2015,03,14,10,10), LocalDateTime.of(2015,03,17,14,10), "Finished");
    }
    
    /**
     * Tests use case extension 6a, with an invalid start time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataStartDateTime1()
    {
    	LocalDateTime invalidStartDateTime = LocalDateTime.of(2015,240,17,14,10);
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(invalidStartDateTime, LocalDateTime.of(2015,03,17,14,10), "FINISHED");
    }
    
    
    /**
     * Tests use case extension 6a, with an invalid start time before the project creation time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataStartDateTime3()
    {
    	LocalDateTime invalidStartDateTime = LocalDateTime.of(1944, 3, 12, 17, 30);
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(invalidStartDateTime, LocalDateTime.of(2015, 3, 12, 17, 30), "Finished");
    }
    
    /**
     * Tests use case extension 6a, with an invalid End time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataEndTime2()
    {
    	LocalDateTime invalidEndDateTime = LocalDateTime.of(2015, 3, 78, 17, 30);
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(LocalDateTime.of(2015, 3, 12, 17, 40), invalidEndDateTime, "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with an invalid End time before the project creation time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataEndDateTime3()
    {
    	LocalDateTime invalidEndDateTime = LocalDateTime.of(1944, 3, 12, 17, 30);
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(LocalDateTime.of(2015, 3, 12, 17, 30), invalidEndDateTime, "Finished");
    }
}
