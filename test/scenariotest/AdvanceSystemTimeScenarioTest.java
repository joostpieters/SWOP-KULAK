package scenariotest;

import controller.AdvanceSystemTimeHandler;
import controller.HandlerFactory;
import domain.Acl;
import domain.Auth;
import domain.Project;
import domain.ProjectContainer;
import domain.Task;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import time.Clock;
import time.Duration;

/**
 * This scenario test, tests the advance system time use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class AdvanceSystemTimeScenarioTest {

    private static ProjectContainer manager;
    private static AdvanceSystemTimeHandler handler;
    private static Project p1;
    private static Task t1, t2;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;

    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectContainer();
        // only p1 has tasks
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 30, 17, 50));
        t1 = p1.createTask("An easy task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES);

        t2 = p1.createTask("A difficult task.", new Duration(500), 50, Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()));

        manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        manager.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        clock = new Clock();
        auth = new Auth();
        acl = new Acl();
        HandlerFactory controller = new HandlerFactory(manager, clock, auth, acl);
        handler = controller.getAdvanceSystemTimeHandler();
    }

    /**
     * Tests the main success scenario of the "Advance System time" use case
     */
    @Test
    public void testMainSuccessScenario() {
        // Step 4
        assertTrue(p1.isOnTime(clock));
        assertTrue(p1.getUnacceptablyOverdueTasks(clock).isEmpty());
        handler.advanceTime("2015-12-03 18:00");
        assertFalse(p1.isOnTime(clock));
        assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(t1));
        assertTrue(p1.getUnacceptablyOverdueTasks(clock).containsKey(t2));
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
