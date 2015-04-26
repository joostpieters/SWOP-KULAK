package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import scenariotest.AdvanceSystemTimeScenarioTest;
import scenariotest.CreateProjectScenarioTest;
import scenariotest.CreateTaskScenarioTest;
import scenariotest.ShowProjectScenarioTest;
import scenariotest.UpdateTaskStatusScenarioTest;
import domain.time.ClockTest;
import domain.time.DurationTest;
import domain.time.TimespanTest;

/**
 * This testsuite, runs all the tests for this application
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ClockTest.class,
                     ProjectContainerTest.class,
                    ProjectTest.class,
                    TaskTest.class,
                    TimespanTest.class, 
                    DurationTest.class,
                    AdvanceSystemTimeScenarioTest.class,
                    CreateProjectScenarioTest.class,
                    CreateTaskScenarioTest.class,
                    ShowProjectScenarioTest.class,
                    UpdateTaskStatusScenarioTest.class})
public class TaskManTestSuite {
    
}
