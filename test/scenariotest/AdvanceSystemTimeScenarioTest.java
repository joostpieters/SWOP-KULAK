package scenariotest;

import controller.AdvanceSystemTimeHandler;
import controller.FrontController;
import domain.Duration;
import domain.Project;
import domain.ProjectManager;
import domain.Task;
import java.time.LocalDateTime;
import java.util.Arrays;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This scenario test, tests the advance system time use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class AdvanceSystemTimeScenarioTest {

    private static ProjectManager manager;
    private static AdvanceSystemTimeHandler handler;
    private static Project p1;
    private static Project p2;
    private static Project p3;
    private static Task t1;
    private static Task t2;

    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectManager();
        // only p1 has tasks
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 30, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);

        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(0));

        p2 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p3 = manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));

        FrontController controller = new FrontController(manager);
        handler = controller.getAdvanceSystemTimeHandler();
    }

    /**
     * Tests the main success scenario of the "Advance System time" use case
     */
    @Test
    public void testMainSuccessScenario() {
        // Step 4
        assertTrue(p1.isOnTime());
        assertTrue(p1.getUnacceptablyOverdueTasks().isEmpty());
        handler.advanceTime("2015-12-03 18:00");
        assertFalse(p1.isOnTime());
        assertTrue(p1.getUnacceptablyOverdueTasks().containsKey(t1));
        assertTrue(p1.getUnacceptablyOverdueTasks().containsKey(t2));
    }

    /**
     * Tests extension 4a. "The entered timestamp is invalid" with an invalid
     * timestamp
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataName() {

        handler.advanceTime("03-13-2015 17:30");
    }

    /**
     * Tests extension 4a. "The entered timestamp is invalid" with a timestamp
     * in the past.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataDescription() {
        handler.advanceTime("1944-03-12 17:30");
    }
}
