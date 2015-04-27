package scenariotest;


import controller.CreateTaskHandler;
import controller.HandlerFactory;
import domain.Acl;
import domain.Auth;
import domain.Database;
import domain.Failed;
import domain.Project;
import domain.ProjectContainer;
import domain.Task;
import domain.Unavailable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import domain.time.Clock;
import domain.time.Duration;

/**
 * This scenario test, tests the create task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskScenarioTest {

	private static Database db;
    private static ProjectContainer manager;
    private static CreateTaskHandler handler;
    private static Project p1;
    private static Task t1;
    private static Task t2;
    private static Clock clock;
    private static Acl acl;
    private static Auth auth;

    @BeforeClass
    public static void setUpClass() {
    	db = new Database();
        manager = new ProjectContainer();
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
        HandlerFactory controller = new HandlerFactory(manager, clock, auth, acl, db);
        handler = controller.getCreateTaskHandler();
    }

    /**
     * Tests the main success scenario of the "Create Task" use case
     */
    @Test
    public void testMainSuccessScenario() {

        // Step 4
        clock.advanceTime(LocalDateTime.of(2015, 03, 16, 17, 30));
        t2.update(LocalDateTime.of(2015, 03, 12, 17, 30), LocalDateTime.of(2015, 03, 16, 17, 30), new Failed(), clock.getTime());
        
        //TODO: te controleren: ...,10,20,... -> ...,20,... (nog iets met uren en minuten?)
        handler.createTask(p1.getId(), "Fun task", 50, Arrays.asList(t1.getId()), 20, t2.getId(), new HashMap<>());
        
        Project project = manager.getProject(p1.getId());
        List<Task> tasks = project.getTasks();
        boolean foundTask = false;
        for (Task t : tasks) {
            if (t.getDescription().equals("Fun task")) {
                foundTask = true;
                assertEquals(50, t.getAcceptableDeviation());
                assertEquals(10, t.getEstimatedDuration().getHours());
                assertEquals(20, t.getEstimatedDuration().getMinutes());
                assertEquals(t, t2.getAlternativeTask());
                
                assertTrue(t.getStatus() instanceof Unavailable);
                assertTrue(t.getPrerequisiteTasks().contains(t1));

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
        
        Project p2 = manager.createProject(project2Name, project2Description, project2StartTime, project2EndTime);
        int alternativeId = t2.getId();
        //TODO: zie vorige to do...
        handler.createTask(p2.getId(), "Fun task8", 50, Project.NO_DEPENDENCIES, 20, alternativeId, new HashMap<>());
        
    }

    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid
     * deviation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataDeviation() {
        int deviation = 500;
        //TODO: zie vorige to do...
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
        
        Project p2 = manager.createProject(project2Name, project2Description, project2StartTime, project2EndTime);
        int dependencyId = t2.getId();
        //TODO: zie vorige to do...
        handler.createTask(p2.getId(), "Fun task5", 50, Arrays.asList(dependencyId), 20, Project.NO_ALTERNATIVE, new HashMap<>());
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid alternative
     * for, that is not a failed task.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidDataStartTime() {
    	//TODO: zie vorige to do...
        handler.createTask(p1.getId(), "Fun task6", 50, Arrays.asList(t1.getId()), 20, t2.getId(), new HashMap<>());
    }    

}
