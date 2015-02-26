package domain;

import java.util.GregorianCalendar;

/**
 * This class represents the system clock
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Clock {
    
    private GregorianCalendar time;
    
    /**
     * Initializes this clock with the given time.
     * 
     * @param time The intitial time to start the clock on
     */
    public Clock(GregorianCalendar time) {
        this.time = time;
    }
    
    /**
     * Initializes this clock with the current time as its inception moment.
     */
    public Clock() {
        this(new GregorianCalendar());
    }
    
    /**
     * Advance the current system time to the given time.
     * 
     * @param time The time in the future to which the system time must be advanced.
     * @throws IllegalArgumentException The given time lays strictly in the past, 
     * compared to this clocks time.
     */
    public void advanceTime(GregorianCalendar time) throws IllegalArgumentException{
        if(time.before(this.time)){
            throw new IllegalArgumentException("The given timestamp is strictly before the current system time.");
        }
        
        this.time = time;
    }
}
