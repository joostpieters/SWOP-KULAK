package domain;

import java.time.LocalDateTime;


/**
 * This class represents a timespan with a start and end time
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Timespan {
    
    private final LocalDateTime startTime, endTime;
    
    private final Duration duration;
    
    /**
     * Initialize this timespan with the given begin and end time.
     * 
     * @param startTime The start time of this timespan
     * @param endTime The end time of this timespan
     * @throws IllegalArgumentException If the given start and end time don't form a valid interval. 
     */
    public Timespan(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException {
        
        if(!isValidTimeInterval(startTime, endTime)){
            throw new IllegalArgumentException("The start time is later than the end time.");
        }
        
        this.startTime = startTime;
        this.endTime = endTime;
        duration = new Duration(startTime, endTime);
    }

    /**
     * Check whether the given start and end time form a valid interval
     * 
     * @param start The start time of the interval
     * @param end The end time of the interval
     * @return True if and only if the start time is strictly before the end time
     */
    private boolean isValidTimeInterval(LocalDateTime start, LocalDateTime end){
        return start.isBefore(end);
    }
    
    /** 
     * @return The time this timespan starts
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /** 
     * @return The time this timespan ends
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    /**
     * Check whether this timespan overlaps with the given timespan
     * 
     * @param anotherTimespan The timespan to check the overlap with
     * @return True if and only if both timespans share a same moment in time 
     */
    public boolean overlapsWith(Timespan anotherTimespan){
        return anotherTimespan.getStartTime().compareTo(endTime) <= 0 &&
               startTime.compareTo(anotherTimespan.getEndTime()) <= 0;
    }
    
    /**
     * Returns the duration by which the given duration exceeds the duration
     * of this timespan.
     * 
     * @param duration The duration to check the delay.
     * @return A duration representing the amount of time the given duration 
     * exceeds the duration of this timespan. If the given duration is shorter 
     * than the duration of this timespan, a duration of 0 is returned.
     */
    public Duration getDelay(Duration duration){
        if(getDuration().compareTo(duration) > 0){
            return new Duration(0);
        }
        
        return duration.subtract(getDuration());
    }
    
    /** 
     * @return The duration of this timespan 
     */
    public Duration getDuration() {
        return duration;
    }
}
