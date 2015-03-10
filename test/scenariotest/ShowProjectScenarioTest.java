package scenariotest;

import domain.Project;
import domain.ProjectManager;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * This scenario test, tests the show projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */

public class ShowProjectScenarioTest {
    
    private static ProjectManager manager;
    private static Project p1;
    private static Project p2;
    private static Project p3;
    
    public ShowProjectScenarioTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        p1 = manager.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p2 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p3 = manager.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Scenario1test.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
