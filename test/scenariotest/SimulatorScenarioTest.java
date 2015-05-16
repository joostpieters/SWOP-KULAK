package scenariotest;


import controller.CreateTaskHandler;
import controller.HandlerFactory;
import controller.RunSimulationHandler;
import domain.Database;
import domain.Project;
import domain.BranchOffice;
import domain.task.Task;
import domain.task.Unavailable;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.GenericUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This scenario test, tests the create task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class SimulatorScenarioTest {

	private static Database db;
    private static BranchOffice manager;
    private static RunSimulationHandler simHandler;
    private static CreateTaskHandler createTaskSimHandler;
    private static Project p1;
    private static Task t1;
    private static Task t2;
    private static Clock clock;
    private static Acl acl;
    private static Auth auth;

    @Before
    public void setUp() {
    	db = new Database();
        manager = new BranchOffice();
        String project1Name = "project 1 :)";
        String project1Description = "This is project 1";
        LocalDateTime project1StartTime = LocalDateTime.of(2015, 03, 12, 17, 30);
        LocalDateTime project1EndTime = LocalDateTime.of(2015, 03, 16, 17, 30);
        p1 = manager.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
        t1 = p1.createTask("Prereq", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
        t2 = p1.createTask("Alternative", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
        
        
        clock = new Clock();
        auth = new Auth(db);
        acl = new Acl();
        db.addUser(new GenericUser("root", "admin"));
        acl.addEntry("admin", Arrays.asList("UpdateTaskStatus", "CreateProject", "PlanTask", "RunSimulation", "CreateTask", "CreateTaskSimulator", "PlanTaskSimulator", "updateTaskStatus"));
        auth.login("root");
        HandlerFactory controller = new HandlerFactory(manager, clock, auth, acl, db);
        simHandler = controller.getSimulationHandler();
        createTaskSimHandler  = simHandler.getCreateTaskSimulatorHandler();
    }

    @Test
    public void testCreateTaskConfirmScenario() {

        // Step 4
        clock.advanceTime(LocalDateTime.of(2015, 03, 16, 17, 30));
        t2.fail(new Timespan(LocalDateTime.of(2015, 03, 12, 17, 30), LocalDateTime.of(2015, 03, 16, 17, 30)), clock.getTime());
        
        
        createTaskSimHandler.createTask(p1.getId(), "Fun task", 50, Arrays.asList(t1.getId()), 20, t2.getId(), new HashMap<>());
        simHandler.carryOutSimulation();
        
        Project project = manager.getProject(p1.getId());
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

            }
        }
        assertTrue(foundTask);
    }
    @Test
    public void testCreateTaskRevertScenario() {

        // Step 4
        clock.advanceTime(LocalDateTime.of(2015, 03, 16, 17, 30));
        t2.fail(new Timespan(LocalDateTime.of(2015, 03, 12, 17, 30), LocalDateTime.of(2015, 03, 16, 17, 30)), clock.getTime());
        
        
        createTaskSimHandler.createTask(p1.getId(), "Fun task", 50, Arrays.asList(t1.getId()), 20, t2.getId(), new HashMap<>());
        simHandler.cancelSimulation();
        
        Project project = manager.getProject(p1.getId());
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

            }
        }
        assertTrue(!foundTask);
    }
}
