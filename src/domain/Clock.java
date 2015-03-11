package domain;

import java.time.LocalDateTime;

/**
 * This class represents the system clock
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Clock {
	
	/**
         * Time used to initialize clock: 1 january 2000
         */
	public static final LocalDateTime NEW_YEAR = LocalDateTime.of(2000, 1, 1, 0, 0);
    
    private LocalDateTime time;
    
    /**
     * Initializes this clock with the given time.
     * 
     * @param 	time 
     * 			The intitial time to start the clock on
     */
    public Clock(LocalDateTime time) {
        setTime(time);
    }
    
    /**
     * Initializes this clock with new year 2015 as its inception moment.
     */
    public Clock() {
        this(NEW_YEAR);
    }
    
    /**
     * Initialize a copy of a given clock.
     * 
     * @param 	c
     * 			The clock to be copied.
     */
    public Clock(Clock c) {
    	this(c.getTime());
    }
    
    /****************************************
     * Getters & setters					*
     ****************************************/
    
    /**
     * @return	the current time of this clock.
     */
    public LocalDateTime getTime() {
    	return this.time;
    }
    
    /**
     * Set the time of this clock.
     * 
     * @param 	time
     * 			The new time for this clock.
     */
    private void setTime(LocalDateTime time) {
    	this.time = time;
    }
    
    /**
     * Advance the current system time to the given time.
     * 
     * @param 	time 
     * 			The time in the future to which the system time must be advanced.
     * @throws 	IllegalArgumentException 
     * 			if the given time lays strictly in the past, compared to this clocks time.
     */
    public void advanceTime(LocalDateTime time) throws IllegalArgumentException{
        if(time.isBefore(this.time)){
            throw new IllegalArgumentException("The given timestamp is strictly before the current system time.");
        }
        
        setTime(time);
    }
    
    @Override
    public boolean equals(Object o) {
    	return ((Clock) o).getTime() == getTime();
    }
}
