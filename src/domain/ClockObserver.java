package domain;

import java.time.LocalDateTime;

/**
 * This class represents an object that observes the clock
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public interface ClockObserver {
    
    /**
     * Update this obeserver according to the given current time
     * 
     * @param currentTime The current time
     */
    public void update(LocalDateTime currentTime);
}
