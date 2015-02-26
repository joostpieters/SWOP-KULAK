package domain;

import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mathias
 */
public class ClockTest {
    
    public ClockTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of advanceTime method, of class Clock.
     */
    @Test
    public void testAdvanceTime() {
        System.out.println("advanceTime");
        Clock instance = new Clock(new GregorianCalendar(2015, 2, 26));
        instance.advanceTime(new GregorianCalendar(2015, 2, 27));
        
    }
    
    /**
     * Test of advanceTime method, of class Clock, when a timestamp in the past is given.
     */
    @Test (expected=IllegalArgumentException.class)
    public void testAdvanceTimeWithPastTime() {
        System.out.println("advanceTime");
        Clock instance = new Clock();
        instance.advanceTime(new GregorianCalendar(1994, 30, 11));
    }
    
}
