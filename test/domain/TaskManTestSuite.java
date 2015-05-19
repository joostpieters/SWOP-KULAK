package domain;

import domain.command.CreateReservationCommandTest;
import domain.command.PlanTaskCommandTest;
import domain.memento.MementoProjectContainerTest;
import domain.memento.MementoProjectTest;
import domain.memento.MementoTaskTest;
import domain.memento.MementoTest;
import domain.task.TaskTest;
import domain.time.ClockTest;
import domain.time.DurationTest;
import domain.time.TimespanTest;
import domain.time.WorkWeekConfigurationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import scenariotest.AdvanceSystemTimeScenarioTest;
import scenariotest.CreateProjectScenarioTest;
import scenariotest.CreateTaskScenarioTest;
import scenariotest.DelegateTaskScenarioTest;
import scenariotest.LoginScenarioTest;
import scenariotest.PlanTaskScenarioTest;
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
                    ProjectTest.class,
                    TaskTest.class,
                    TimespanTest.class, 
                    DurationTest.class,
                    WorkWeekConfigurationTest.class, 
                    ResourceTest.class,
                    ResourceTypeTest.class,
                    ResourceContainerTest.class,
                    CreateReservationCommandTest.class, 
                    PlanTaskCommandTest.class, 
                    MementoTest.class,
                    MementoProjectTest.class,
                    MementoTaskTest.class,
                    MementoProjectContainerTest.class,
                    AdvanceSystemTimeScenarioTest.class,
                    CreateProjectScenarioTest.class,
                    CreateTaskScenarioTest.class,
                    ShowProjectScenarioTest.class,
                    UpdateTaskStatusScenarioTest.class,
                    SimulatorScenarioTest.class,
                    PlanTaskScenarioTest.class, 
                    LoginScenarioTest.class, 
                    DelegateTaskScenarioTest.class})
public class TaskManTestSuite {
    
}
