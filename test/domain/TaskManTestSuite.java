package domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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
                    DurationTest.class})
public class TaskManTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
