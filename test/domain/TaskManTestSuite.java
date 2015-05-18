package domain;

import domain.memento.MementoProjectContainerTest;
import domain.memento.MementoProjectTest;
import domain.memento.MementoTest;
import domain.task.TaskTest;
import domain.time.ClockTest;
import domain.time.DurationTest;
import domain.time.TimespanTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import scenariotest.AdvanceSystemTimeScenarioTest;
import scenariotest.CreateProjectScenarioTest;
import scenariotest.CreateTaskScenarioTest;
import scenariotest.ShowProjectScenarioTest;
import scenariotest.SimulatorScenarioTest;
import scenariotest.UpdateTaskStatusScenarioTest;

/**
 * This testsuite, runs all the tests for this application
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ClockTest.class,
                     MementoTest.class,
                    ProjectTest.class,
                    TaskTest.class,
                    TimespanTest.class, 
                    DurationTest.class,
                    ResourceTest.class,
                    ResourceTypeTest.class,
                    ResourceContainerTest.class,
                    MementoTest.class,
                    MementoProjectTest.class,
                    MementoProjectContainerTest.class,
                    AdvanceSystemTimeScenarioTest.class,
                    CreateProjectScenarioTest.class,
                    CreateTaskScenarioTest.class,
                    ShowProjectScenarioTest.class,
                    UpdateTaskStatusScenarioTest.class,
                    SimulatorScenarioTest.class})
public class TaskManTestSuite {
    
}
