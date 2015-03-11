package scenariotest;

import controller.FrontController;
import controller.ShowProjectHandler;
import controller.UpdateTaskStatusHandler;
import domain.DetailedProject;
import domain.DetailedTask;
import domain.Duration;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class UpdateTaskStatusScenarioTest {
    
    private static ProjectManager manager;
    private static UpdateTaskStatusHandler handler;
    private static Project p1;
    private static Project p2;
    private static Project p3;
    private static Task t1;
    private static Task t2;
    
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
        handler = controller.getUpdateTaskHandler();
    }

    /**
     * Tests the main success scenario of the "Update Task Status" use case
     */
    @Test
    public void testMainSuccessScenario() {
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask("2015-03-14 10:10", "2015-03-17 14:10", "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with a badly formatted start time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataStartDateTime1()
    {
    	String invalidStartDateTime = "2015-3-3 10:10";
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(invalidStartDateTime, "2015-03-17 14:10", "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with an invalid start time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataStartTime2()
    {
    	String invalidStartDateTime = "2015-03-54 10:10";
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(invalidStartDateTime, "2015-03-17 14:10", "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with an invalid start time before the project creation time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataStartDateTime3()
    {
    	String invalidStartDateTime = "2014-03-54 10:10";
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(invalidStartDateTime, "2015-03-17 14:10", "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with a badly formatted start time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataEndDateTime1()
    {
    	String invalidEndDateTime = "2015-3-3 10:10";
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask("2015-03-14 10:10", invalidEndDateTime, "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with an invalid End time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataEndTime2()
    {
    	String invalidEndDateTime = "2015-03-54 10:10";
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask("2015-03-14 10:10", invalidEndDateTime, "FINISHED");
    }
    
    /**
     * Tests use case extension 6a, with an invalid End time before the project creation time.
     */
    @Test (expected = Exception.class)
    public void testInvalidDataEndDateTime3()
    {
    	String invalidEndDateTime = "2014-03-54 10:10";
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	Map<DetailedTask, DetailedProject> tasks = handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask("2015-03-14 10:10", invalidEndDateTime, "FINISHED");
    }
}
