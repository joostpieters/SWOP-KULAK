package scenariotest;

import controller.HandlerFactory;
import controller.UpdateTaskStatusHandler;
import domain.BranchOffice;
import domain.Company;
import domain.Project;
import domain.ProjectContainer;
import domain.Resource;
import domain.ResourceContainer;
import domain.ResourceType;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.Developer;
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
    
	private static Company db;
    private static ProjectContainer pc;
    private static BranchOffice manager;
    private static UpdateTaskStatusHandler handler;
    private static Project p1;
    private static Task t1;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;
    private static Resource dev;
	private static ResourceContainer rc;
    
    @BeforeClass
    public static void setUpClass() {
        clock = new Clock();
    	db = new Company();
        pc = new ProjectContainer();
        rc = new ResourceContainer();
        dev = rc.createResource("jef", ResourceType.DEVELOPER);
		manager = new BranchOffice("Monaco", pc, rc);
        db.addOffice(manager);
        // only p1 has tasks
        p1 = pc.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.getDefaultRequiredResources());
        t1.plan(clock.getTime(), Arrays.asList(dev), clock);
        t1.execute(clock);
        
        p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()), Task.getDefaultRequiredResources());
        
        pc.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        pc.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        
        auth = new Auth(db);
        acl = new Acl();
		acl.addEntry("developer", Arrays.asList("UpdateTaskStatus"));
		acl.addEntry("manager", Arrays.asList("CreateTask", "CreateProject", "PlanTask", "RunSimulation", "CreateTask", "CreateTaskSimulator", "PlanTaskSimulator", "DelegateTask"));
		acl.addEntry("admin", acl.getPermissions("manager"));
        for(String permission : acl.getPermissions("developer"))
        	acl.addPermission("admin", permission);
        manager.addUser(new Developer("John", manager));
        auth.login("John");
		HandlerFactory controller = new HandlerFactory(db, auth, acl, clock);
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
