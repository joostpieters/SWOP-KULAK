package scenariotest;

import controller.AdvanceSystemTimeHandler;
import controller.HandlerFactory;
import domain.user.Acl;
import domain.user.Auth;
import domain.BranchOffice;
import domain.Database;
import domain.Project;
import domain.ProjectContainer;
import domain.ResourceContainer;
import domain.task.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import domain.time.Clock;
import domain.time.Duration;

/**
 * This scenario test, tests the advance system time use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class AdvanceSystemTimeScenarioTest {

	private static Database db;
    private static ProjectContainer manager;
    private static AdvanceSystemTimeHandler handler;
    private static Project p1;
    private static Task t1, t2;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;

    @BeforeClass
    public static void setUpClass() {
    	db = new Database();
        manager = new ProjectContainer();
        // only p1 has tasks
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 30, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());

        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()), new HashMap<>());

        manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        clock = new Clock();
        auth = new Auth(db);
        acl = new Acl();
        HandlerFactory controller = new HandlerFactory(new BranchOffice(manager, new ResourceContainer(), new Database()), clock, auth, acl, db);
        handler = controller.getAdvanceSystemTimeHandler();
    }

    /**
     * Tests the main success scenario of the "Advance System time" use case
     */
    @Test
    public void testMainSuccessScenario() {
        // Step 4
        assertTrue(p1.isOnTime(clock.getTime()));
        assertTrue(p1.getUnacceptablyOverdueTasks(clock.getTime()).isEmpty());
        handler.advanceTime("2015-12-03 18:00");
        assertFalse(p1.isOnTime(clock.getTime()));
        assertTrue(p1.getUnacceptablyOverdueTasks(clock.getTime()).containsKey(t1));
        assertTrue(p1.getUnacceptablyOverdueTasks(clock.getTime()).containsKey(t2));
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
