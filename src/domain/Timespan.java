package domain;

import java.util.GregorianCalendar;

/**
 * This class represents a timespan with a start and end time
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Timespan {
    
    private final GregorianCalendar startTime, endTime;
    
    /**
     * Initialize this timespan with the given begin and end time.
     * 
     * @param startTime The start time of this timespan
     * @param endTime The end time of this timespan
     * @throws IllegalArgumentException If the given start and end time don't form a valid interval. 
     */
    public Timespan(GregorianCalendar startTime, GregorianCalendar endTime) throws IllegalArgumentException {
        
        if(!isValidTimeInterval(startTime, endTime)){
            throw new IllegalArgumentException("The start time is later than the end time.");
        }
        
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Check whether the given start and end time form a valid interval
     * 
     * @param start The start time of the interval
     * @param end The end time of the interval
     * @return True if and only if the start time is strictly before the end time
     */
    private boolean isValidTimeInterval(GregorianCalendar start, GregorianCalendar end){
        return start.before(end);
    }
    
    /** 
     * @return The time this timespan starts
     */
    public GregorianCalendar getStartTime() {
        return (GregorianCalendar) startTime.clone();
    }
    
    /** 
     * @return The time this timespan ends
     */
    public GregorianCalendar getEndTime() {
        return (GregorianCalendar) endTime.clone();
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
}
