package domain;

import domain.Clock;
import java.time.LocalDateTime;
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
        Clock instance = new Clock(LocalDateTime.of(2015, 2, 26, 14, 30));
        instance.advanceTime(LocalDateTime.of(2015, 2, 27, 14, 40));
        
        assertEquals(instance.getTime(), LocalDateTime.of(2015, 2, 27, 14, 40));
    }
    
    /**
     * Test of advanceTime method, of class Clock, when a timestamp in the past is given.
     */
    @Test (expected=IllegalArgumentException.class)
    public void testAdvanceTimeWithPastTime() {
        Clock instance = new Clock();
        instance.advanceTime(LocalDateTime.of(1994, 11, 30, 10, 30));
    }
    
}
