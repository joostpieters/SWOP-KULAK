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
import domain.task.Executing;
import domain.task.Failed;
import domain.task.Finished;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.Developer;
import domain.user.Role;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;



/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class UpdateTaskStatusScenarioTest {
    
	private static Company db;
    private static ProjectContainer pc;
    private static BranchOffice branchOffice;
    private static UpdateTaskStatusHandler handler;
    private static Project p1;
    private static Task t1;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;
    private static Resource dev;
	private static ResourceContainer rc;
    private Developer dev2;
    
    @Before
    public void setUp() {
        clock = new Clock();
    	db = new Company();
        pc = new ProjectContainer();
        rc = new ResourceContainer();
        dev = rc.createResource("jef", ResourceType.DEVELOPER);
		branchOffice = new BranchOffice("Monaco", pc, rc);
        db.addOffice(branchOffice);
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
		acl.addEntry(Role.DEVELOPER, Arrays.asList("UpdateTaskStatus"));
		acl.addEntry(Role.MANAGER, Arrays.asList("CreateTask", "CreateProject", "PlanTask", "RunSimulation", "CreateTask", "CreateTaskSimulator", "PlanTaskSimulator", "DelegateTask"));
		acl.addEntry(Role.ADMIN, acl.getPermissions(Role.MANAGER));
        for(String permission : acl.getPermissions(Role.DEVELOPER))
        	acl.addPermission(Role.ADMIN, permission);
           dev2 = new Developer("John", branchOffice);
        branchOffice.addUser(dev2);
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
        assertTrue(t1.getStatus() instanceof Finished);
        Assert.assertEquals(LocalDateTime.of(2015,03,14,10,10), t1.getTimeSpan().getStartTime());
         Assert.assertEquals(LocalDateTime.of(2015,03,17,14,10), t1.getTimeSpan().getEndTime());
    }
    
     /**
     * Tests the main success scenario of the "Update Task Status" use case
     */
    @Test
    public void testMainSuccessScenario2() {
        
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(LocalDateTime.of(2015,03,14,10,10), LocalDateTime.of(2015,03,17,14,10), "Failed");
        assertTrue(t1.getStatus() instanceof Failed);
         Assert.assertEquals(LocalDateTime.of(2015,03,14,10,10), t1.getTimeSpan().getStartTime());
         Assert.assertEquals(LocalDateTime.of(2015,03,17,14,10), t1.getTimeSpan().getEndTime());
    }
    
    /**
     * Tests the main success scenario of the "Update Task Status" use case update to execute
     */
    @Test
    public void testMainSuccessScenario3() {
        Task t2 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.getDefaultRequiredResources());
        t2.plan(clock.getTime(), Arrays.asList(dev2), clock);
        
        
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t2.getId());
    	// Step 6: The system updates the task status.
    	handler.executeCurrentTask();
        
        assertTrue(t2.getStatus() instanceof Executing);
        
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
    
    /**
     * Tests use case extension 6a, with an invalid stauts.
     */
    @Test (expected = Exception.class)
    public void testInvalidStatus()
    {
    	
    	// Step 2: The system shows a list of all available tasks and the project
    	//         they belong to.
    	handler.getAvailableTasks();
    	// Step 3: The user selects the task he wants to change.
    	handler.selectTask(p1.getId(), t1.getId());
    	// Step 6: The system updates the task status.
    	handler.updateCurrentTask(LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015,03,17,14,10), "Fddsfsd");
    }
}
