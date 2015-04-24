package time;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import time.WorkWeekConfiguration;

public class WorkWeekConfigurationTest {
	private WorkWeekConfiguration wwc_default;
	
	@Before
	public void setup()
	{
		wwc_default = new WorkWeekConfiguration(1, 5, LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(12, 0), LocalTime.of(13, 0));
	}
	
    /**
     * Test nextValidWorkTime method in case of a valid work time.
     */
    @Test
    public void testNextValidWorkTimeValid() {
        LocalDateTime worktime = LocalDateTime.of(2015, 3, 10, 10, 0);
        assertTrue(wwc_default.isValidWorkTime(worktime));
        assertEquals(worktime, wwc_default.nextValidWorkTime(worktime));
    }

    /**
     * Test nextValidWorkTime method in case of a time before the work day
     * began.
     */
    @Test
    public void testNextValidWorkTimeTooEarly() {
        LocalDateTime tooEarly = LocalDateTime.of(2015, 3, 10, 5, 0);
        assertTrue(wwc_default.isValidWorkDay(tooEarly));
        assertTrue(tooEarly.toLocalTime().isBefore(wwc_default.getBeginWorkDay()));
        assertEquals(LocalDateTime.of(tooEarly.toLocalDate(), wwc_default.getBeginWorkDay()),
        		wwc_default.nextValidWorkTime(tooEarly));
    }
    
    /**
     * Test nextValidWorkTime method in case of a time after the work day ended.
     */
    @Test
    public void testNextValidWorkTimeTooLate() {
        LocalDateTime tooLate = LocalDateTime.of(2015, 3, 10, 22, 0);
        assertTrue(wwc_default.isValidWorkDay(tooLate));
        assertTrue(tooLate.getDayOfWeek().getValue() != wwc_default.getEndWorkWeek());
        assertTrue(tooLate.toLocalTime().isAfter(wwc_default.getEndWorkDay()));
        assertEquals(LocalDateTime.of(tooLate.toLocalDate().plusDays(1), wwc_default.getBeginWorkDay()),
        		wwc_default.nextValidWorkTime(tooLate));
    }
    
    /**
     * Test nextValidWorkTime method in case of a time during lunch break.
     */
    @Test
    public void testNextValidWorkTimeLunch() {
        LocalDateTime lunch = LocalDateTime.of(2015, 3, 10, 12, 30);
        assertTrue(wwc_default.isValidWorkDay(lunch));
        assertTrue(lunch.toLocalTime().isAfter(wwc_default.getBeginLunch()));
        assertTrue(lunch.toLocalTime().isBefore(wwc_default.getEndLunch()));
        assertEquals(LocalDateTime.of(lunch.toLocalDate(), wwc_default.getEndLunch()),
        		wwc_default.nextValidWorkTime(lunch));
    }
    
    /**
     * Test nextValidWorkTime method in case of a not working time.
     */
    @Test
    public void testNextValidWorkTimeWeekend() {
        LocalDateTime saturdayMorning = LocalDateTime.of(2015, 3, 7, 4, 0);
        assertFalse(wwc_default.isValidWorkDay(saturdayMorning));
        assertFalse(wwc_default.isValidWorkDay(saturdayMorning.plusDays(1)));
        assertTrue(wwc_default.isValidWorkDay(saturdayMorning.plusDays(2)));
        assertEquals(LocalDateTime.of(saturdayMorning.toLocalDate().plusDays(2), wwc_default.getBeginWorkDay()),
        		wwc_default.nextValidWorkTime(saturdayMorning));

        LocalDateTime saturdayEvening = LocalDateTime.of(2015, 3, 7, 1, 0);
        assertFalse(wwc_default.isValidWorkDay(saturdayEvening));
        assertFalse(wwc_default.isValidWorkDay(saturdayEvening.plusDays(1)));
        assertTrue(wwc_default.isValidWorkDay(saturdayEvening.plusDays(2)));
        assertEquals(LocalDateTime.of(saturdayEvening.toLocalDate().plusDays(2), wwc_default.getBeginWorkDay()),
        		wwc_default.nextValidWorkTime(saturdayEvening));

        LocalDateTime sundayMorning = LocalDateTime.of(2015, 3, 8, 10, 30);
        assertFalse(wwc_default.isValidWorkDay(sundayMorning));
        assertTrue(wwc_default.isValidWorkDay(sundayMorning.plusDays(1)));
        assertEquals(LocalDateTime.of(sundayMorning.toLocalDate().plusDays(1), wwc_default.getBeginWorkDay()),
        		wwc_default.nextValidWorkTime(sundayMorning));

        LocalDateTime sundayEvening = LocalDateTime.of(2015, 3, 8, 22, 45);
        assertFalse(wwc_default.isValidWorkDay(sundayEvening));
        assertTrue(wwc_default.isValidWorkDay(sundayEvening.plusDays(1)));
        assertEquals(LocalDateTime.of(sundayEvening.toLocalDate().plusDays(1), wwc_default.getBeginWorkDay()),
        		wwc_default.nextValidWorkTime(sundayEvening));
    }
}
