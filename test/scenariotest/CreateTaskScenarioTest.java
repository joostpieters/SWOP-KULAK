package scenariotest;


import controller.CreateTaskHandler;
import controller.HandlerFactory;
import domain.BranchOffice;
import domain.Database;
import domain.Project;
import domain.ProjectContainer;
import domain.ResourceContainer;
import domain.ResourceType;
import domain.task.Task;
import domain.task.Unavailable;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.GenericUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * This scenario test, tests the create task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskScenarioTest {

	private static Database db;
    private static ProjectContainer pc;
    private static BranchOffice manager;
    private static CreateTaskHandler handler;
    private static Project p1;
    private static Task t1;
    private static Task t2;
    private static Clock clock;
    private static Acl acl;
    private static Auth auth;
    private static ResourceType resType1;
    private static ResourceType resType2;

    @Before
    public void setUp() {
    	db = new Database();
        pc = new ProjectContainer();
        // resourcetypes
        resType1 = ResourceType.DEVELOPER;
        db.addResourceType(resType1);
        resType2 = new ResourceType("car");
        db.addResourceType(resType2);
        
        manager = new BranchOffice("Kortrijk", pc, new ResourceContainer());
        db.addOffice(manager);
        String project1Name = "project 1 :)";
        String project1Description = "This is project 1";
        LocalDateTime project1StartTime = LocalDateTime.of(2015, 03, 12, 17, 30);
        LocalDateTime project1EndTime = LocalDateTime.of(2015, 03, 16, 17, 30);
        p1 = pc.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
        t1 = p1.createTask("Prereq", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.getDefaultRequiredResources());
        t2 = p1.createTask("Alternative", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.getDefaultRequiredResources());
        
        clock = new Clock();
        auth = new Auth(db);
        acl = new Acl();
		acl.addEntry("developer", Arrays.asList("UpdateTaskStatus"));
		acl.addEntry("manager", Arrays.asList("CreateTask", "CreateProject", "PlanTask", "RunSimulation", "CreateTask", "CreateTaskSimulator", "PlanTaskSimulator", "DelegateTask"));
		acl.addEntry("admin", acl.getPermissions("manager"));
        for(String permission : acl.getPermissions("developer"))
        	acl.addPermission("admin", permission);
        manager.addUser(new GenericUser("John", "manager", manager));
        auth.login("John");
        HandlerFactory controller = new HandlerFactory(manager, clock, auth, acl, db);
        handler = controller.getCreateTaskHandler();
    }

    /**
     * Tests the main success scenario of the "Create Task" use case
     */
    @Test
    public void testMainSuccessScenario() {
        // Step 3
        // Check whether all the input is available
        assertTrue(handler.getUnfinishedProjects().contains(p1));
        assertEquals(1, handler.getUnfinishedProjects().size());
        
        assertTrue(handler.getAllTasks().contains(t1));
        assertTrue(handler.getAllTasks().contains(t2));
        assertEquals(2, handler.getAllTasks().size());
        
        assertTrue(handler.getResourceTypes().contains(resType1));
        assertTrue(handler.getResourceTypes().contains(resType2));
        assertEquals(2, handler.getResourceTypes().size());
        
        
        // Step 4
        clock.advanceTime(LocalDateTime.of(2015, 03, 16, 17, 30));
        t2.fail(new Timespan(LocalDateTime.of(2015, 03, 12, 17, 30), LocalDateTime.of(2015, 03, 16, 17, 30)), clock.getTime());
        
            HashMap<Integer, Integer> requirements = new HashMap<>();
            requirements.put(0, 2);
            requirements.put(1, 4);
            
        handler.createTask(p1.getId(), "Fun task", 50, Arrays.asList(t1.getId()), 20, t2.getId(), requirements);
        
        Project project = pc.getProject(p1.getId());
        List<Task> tasks = project.getTasks();
        boolean foundTask = false;
        for (Task t : tasks) {
            if (t.getDescription().equals("Fun task")) {
                foundTask = true;
                assertEquals(50, t.getAcceptableDeviation());
                assertEquals(20, t.getEstimatedDuration().getMinutes());
                assertEquals(t, t2.getAlternativeTask());
                
                assertTrue(t.getStatus() instanceof Unavailable);
                assertTrue(t.getPrerequisiteTasks().contains(t1));
                
                // required resources
                
                Map<ResourceType, Integer> requiredResources = t.getRequiredResources();
                //TODO: nullpointer!!
				assertEquals(2, (int) requiredResources.get(resType1));
                assertEquals(4, (int) requiredResources.get(resType2));
                assertEquals(2, requiredResources.size());

            }
        }
        assertTrue(foundTask);
    }

    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid alternative for,
     * that belongs to another project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataAlternativeFor() {
        String project2Name = "project 1 :)";
        String project2Description = "This is project 1";
        LocalDateTime project2StartTime = LocalDateTime.of(2015, 03, 12, 17, 30);
        LocalDateTime project2EndTime = LocalDateTime.of(2015, 03, 16, 17, 30);
        
        Project p2 = pc.createProject(project2Name, project2Description, project2StartTime, project2EndTime);
        int alternativeId = t2.getId();
        
        handler.createTask(p2.getId(), "Fun task8", 50, Project.NO_DEPENDENCIES, 20, alternativeId, new HashMap<>());
        
    }

    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid
     * deviation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataDeviation() {
        int deviation = 500;
        
       handler.createTask(p1.getId(), "Fun task7", deviation, Project.NO_DEPENDENCIES, 20, Project.NO_ALTERNATIVE, new HashMap<>());
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid prerequisite,
     * that belongs to another project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataPrerequisites() {
        String project2Name = "project 1 :)";
        String project2Description = "This is project 1";
        LocalDateTime project2StartTime = LocalDateTime.of(2015, 03, 12, 17, 30);
        LocalDateTime project2EndTime = LocalDateTime.of(2015, 03, 16, 17, 30);
        
        Project p2 = pc.createProject(project2Name, project2Description, project2StartTime, project2EndTime);
        int dependencyId = t2.getId();
        
        handler.createTask(p2.getId(), "Fun task5", 50, Arrays.asList(dependencyId), 20, Project.NO_ALTERNATIVE, new HashMap<>());
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid alternative
     * for, that is not a failed task.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidDataStartTime() {
        handler.createTask(p1.getId(), "Fun task6", 50, Arrays.asList(t1.getId()), 20, t2.getId(), new HashMap<>());
    }    

}
