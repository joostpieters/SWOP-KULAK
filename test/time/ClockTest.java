package time;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.time.Clock;

/**
 *
 * @author Mathias
 */
public class ClockTest {
	
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
    
    /**
     * Test of equals method, of class Clock.
     */
    @Test
    public void testEquals() {
        Clock instance = new Clock(LocalDateTime.of(2015, 2, 26, 14, 30));
        Clock instance2 = new Clock(LocalDateTime.of(2015, 2, 26, 14, 30));
        Clock instance3 = new Clock(LocalDateTime.of(2015, 2, 26, 14, 31));
        
        assertTrue(instance.equals(instance2));
        assertFalse(instance.equals(instance3));
        
        assertFalse(instance.equals(LocalDateTime.of(2015, 2, 26, 14, 31)));
        
        
    }
    
}
