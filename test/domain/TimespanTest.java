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
public class TimespanTest {
    
    public TimespanTest() {
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

    @Test (expected = IllegalArgumentException.class)
    public void testTimespanConstructorWithEndBeforeBegin() {
        GregorianCalendar start = new GregorianCalendar(2015, 1, 3, 14, 30);
        GregorianCalendar end = new GregorianCalendar(2015, 1, 3, 14, 29);
        Timespan timespan = new Timespan(start, end);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testTimespanConstructorWithEqualEndAndBegin() {
        GregorianCalendar start = new GregorianCalendar(2015, 1, 3, 14, 30);
        
        Timespan timespan = new Timespan(start, start);
    }
    
    @Test
    public void testTimespanConstructor() {
        GregorianCalendar start = new GregorianCalendar(2015, 1, 3, 14, 29);
        GregorianCalendar end = new GregorianCalendar(2015, 1, 3, 14, 30);
        Timespan timespan = new Timespan(start, end);
           assertTrue(true); 
    }
    
    @Test
    public void testOverlapsWith() {
        GregorianCalendar start1 = new GregorianCalendar(2015, 1, 3, 14, 0);
        GregorianCalendar end1 = new GregorianCalendar(2015, 1, 3, 15, 30);
        Timespan timespan1 = new Timespan(start1, end1);
        GregorianCalendar start2 = new GregorianCalendar(2015, 1, 3, 14, 30);
        GregorianCalendar end2 = new GregorianCalendar(2015, 1, 3, 15, 40);
        Timespan timespan2 = new Timespan(start2, end2);
        
        assertTrue(timespan1.overlapsWith(timespan2));
        // conversely
        assertTrue(timespan2.overlapsWith(timespan1));
        
        // a timespan also overlaps with itself
        assertTrue(timespan1.overlapsWith(timespan1));
        
        GregorianCalendar start3 = new GregorianCalendar(2015, 1, 3, 15, 41);
        GregorianCalendar end3 = new GregorianCalendar(2015, 1, 3, 15, 52);
        Timespan timespan3 = new Timespan(start3, end3);
        
        assertFalse(timespan2.overlapsWith(timespan3));
        //conversely
        assertFalse(timespan3.overlapsWith(timespan2));
        
    }
    
}
