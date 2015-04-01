package scenariotest;


import controller.CreateTaskHandler;
import controller.HandlerFactory;
import domain.Available;
import domain.Duration;
import domain.Failed;
import domain.Project;
import domain.ProjectContainer;
import domain.Task;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This scenario test, tests the create task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskScenarioTest {

    private static ProjectContainer manager;
    private static CreateTaskHandler handler;
    private static Project p1;
    private static Task t1;
    private static Task t2;

    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectContainer();
        String project1Name = "project 1 :)";
        String project1Description = "This is project 1";
        LocalDateTime project1StartTime = LocalDateTime.of(2015, 03, 12, 17, 30);
        LocalDateTime project1EndTime = LocalDateTime.of(2015, 03, 16, 17, 30);
        p1 = manager.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
        t1 = p1.createTask("Prereq", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        t2 = p1.createTask("Alternative", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);
        
        HandlerFactory controller = new HandlerFactory(manager);
        handler = controller.getCreateTaskHandler();
    }

    /**
     * Tests the main success scenario of the "Create Task" use case
     */
    @Test
    public void testMainSuccessScenario() {

        // Step 4
        manager.advanceSystemTime(LocalDateTime.of(2015, 03, 16, 17, 30));
        p1.updateTask(t2.getId(), LocalDateTime.of(2015, 03, 12, 17, 30), LocalDateTime.of(2015, 03, 16, 17, 30), new Failed());
        
        handler.createTask(p1.getId(), "Fun task", 50, Arrays.asList(t1.getId()), 10, 20, t2.getId());
        
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
                assertTrue(t.getStatus() instanceof Available);
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
        handler.createTask(p2.getId(), "Fun task8", 50, Project.NO_DEPENDENCIES, 10, 20, alternativeId);
        
    }

    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid
     * deviation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataDeviation() {
        int deviation = 500;
       handler.createTask(p1.getId(), "Fun task7", deviation, Project.NO_DEPENDENCIES, 10, 20, Project.NO_ALTERNATIVE);
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
        handler.createTask(p2.getId(), "Fun task5", 50, Arrays.asList(dependencyId), 10, 20, Project.NO_ALTERNATIVE);
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid alternative
     * for, that is not a failed task.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidDataStartTime() {
        handler.createTask(p1.getId(), "Fun task6", 50, Arrays.asList(t1.getId()), 10, 20, t2.getId());
    }    

}
