package scenariotest;

import controller.HandlerFactory;
import controller.UpdateTaskStatusHandler;
import domain.Database;
import domain.Project;
import domain.ProjectContainer;
import domain.BranchOffice;
import domain.ResourceContainer;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.GenericUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;



/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class UpdateTaskStatusScenarioTest {
    
	private static Database db;
    private static ProjectContainer pc;
    private static BranchOffice manager;
    private static UpdateTaskStatusHandler handler;
    private static Project p1;
    private static Task t1;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;
    
    @BeforeClass
    public static void setUpClass() {
    	db = new Database();
        pc = new ProjectContainer();
        manager = new BranchOffice("Monaco", pc, new ResourceContainer());
        // only p1 has tasks
        p1 = pc.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
        
        p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()), new HashMap<>());
        
        pc.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        pc.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        
        clock = new Clock();
        auth = new Auth(db);
        acl = Acl.DEFAULT;
        db.addUser(new GenericUser("John", "manager", manager));
        auth.login("John");
		HandlerFactory controller = new HandlerFactory(manager, clock, auth, acl, db);
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
