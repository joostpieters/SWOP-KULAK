package domain;

import scenariotest.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import scenariotest.AdvanceSystemTimeScenarioTest;

/**
 * This testsuite, runs all the tests for this application
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ClockTest.class,
                     ProjectManagerTest.class,
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
