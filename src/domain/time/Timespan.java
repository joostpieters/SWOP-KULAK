package domain.time;

import java.time.LocalDateTime;


/**
 * This class represents a timespan with a start and end time
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public final class Timespan implements Comparable<Timespan>{
    
    private final LocalDateTime startTime, endTime;
    
    /**
     * Initialize this time span with the given begin and end time.
     * 
     * @param 	startTime 
     * 			The start time of this time span
     * @param 	endTime 
     * 			The end time of this time span
     * @throws 	IllegalArgumentException 
     * 			If the given start and end time don't form a valid interval. 
     */
    public Timespan(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException {
        
        if(!canHaveAsTime(startTime) || !canHaveAsTime(endTime)){
            throw new IllegalArgumentException("The start time is not valid.");
        }
        
        if(!canHaveAsTimeInterval(startTime, endTime)){
            throw new IllegalArgumentException("The start time is later than the end time.");
        }
        
        this.startTime = startTime;
        this.endTime = endTime;
        
    }
    
    /**
     * Initialize an infinite time span with given begin time.
     * 
     * @param 	startTime
     *       	The start time of this time span.
     */
    public Timespan(LocalDateTime startTime) {
    	this(startTime, LocalDateTime.MAX);
    }
    
    /**
     * Initialize this time span based on the given begin time and duration.
     * 
     * @param startTime
     *        The start time of this time span
     * @param duration
     *        The duration of this time span
     */
    public Timespan(LocalDateTime startTime, Duration duration)
    {
    	this(startTime, startTime.plusMinutes(duration.getMinutes()));
    }
    
    /**
     * Check whether the given start and end time form a valid interval
     * 
     * @param 	start 
     * 			The start time of the interval
     * @param 	end 
     * 			The end time of the interval
     * @return 	True if and only if the start time is strictly before the end time
     */
    private boolean canHaveAsTimeInterval(LocalDateTime start, LocalDateTime end){
        return start.isBefore(end);
    }
    
    /** 
     * @return 	The time this timespan starts
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /** 
     * @return 	The time this timespan ends
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Check whether this time span is infinite.
     * 
     * @return	{@code true} if this represents an infinite time span.
     */
	public boolean isInfinite() {
		return getEndTime() == LocalDateTime.MAX;
	}
    
    /**
     * Check whether this time span overlaps with the given time span
     * 
     * @param 	anotherTimespan 
     * 			The time span to check the overlap with
     * @return 	True if and only if both time spans share a certain period of time.
     */
    public boolean overlapsWith(Timespan anotherTimespan){
    	
        return anotherTimespan.getStartTime().compareTo(this.getEndTime()) < 0 &&
               this.getStartTime().compareTo(anotherTimespan.getEndTime()) < 0;
    }
    
    /**
     * Check whether this timespan contains the given time.
     * 
     * @param 	time
     *          The local date time to check
     * @return 	True if and only if the start time of this time span falls before the given time
     *          and the end time of this time span falls after the given time. (Not strict)
     */
    public boolean overlapsWith(LocalDateTime time){
    	return time.compareTo(getStartTime()) >= 0 && time.compareTo(getEndTime()) <= 0;
    }
    
    /**
     * Returns the duration by which the duration of this time span
     * exceeds the given duration.
     * 
     * @param 	duration 
     * 			The duration to check the delay.
     * @return 	A duration representing the amount of time the duration of this time span
     * 			exceeds the given duration. If the duration of this time span is shorter 
     * 			than the given duration, a duration of 0 is returned.
     */
    public Duration getExcess(Duration duration){
        if(getDuration().compareTo(duration) < 0){
            return new Duration(0);
        }
        
        return getDuration().subtract(duration);
    }
    
    /** 
     * @return 	The duration of this timespan 
     */
    public Duration getDuration() {
        return new Duration(startTime, endTime);
    }
    
    /**
     * Check whether this timespan can have the given time as its time.
     * 
     * @param 	time 
     * 			The time to check
     * @return 	True if and only if the given time is not null.
     */
    public boolean canHaveAsTime(LocalDateTime time){
        return time != null;
    }
    
    /**
     * Check whether this timespan ends before the given timespan, not necessarily
     * strictly before
     * 
     * @param 	anotherTimespan 
     * 			The timespan to compare to
     * @return 	True if and only if the end time of this timespan is before or 
     * 			equals the start time of the given timespan.
     */
    public boolean endsBefore(Timespan anotherTimespan){
        return getEndTime().compareTo(anotherTimespan.getStartTime()) <= 0;
    }
    
    /**
     * Check whether the given time is before the end of this timespan.
     * 
     * @param time The time to check
     * @return True if and only if the end time of this timespan happens strictly
     * before the given time.
     */
    public boolean endsAfter(LocalDateTime time){
        return getEndTime().isAfter(time);
    }
    
    /**
     * Compares this time span with the given time.
     * 
     * @param time The time to compare with.
     * @return 0 if this time span overlaps with the given time,
     *         -1 if the given time is strictly before the start time of this time span,
     *         1 otherwise.
     */
    public int compareTo(LocalDateTime time)
    {
    	if(overlapsWith(time))
    		return 0;
    	else if (getStartTime().isBefore(time))
    		return -1;
    	else return 1;
    }

    /**
     * Compares this time span with another time span.
     * This is done by comparing starting times.
     * In case of equal start times, end times are being compared.
     * 
     * @param	other
     * 			The Timespan to be compared with.
     * @return	0 if this time span equals other,
     *        	negative if other starts or ends before this,
     *        	positive if other starts or ends after this.
     */
    @Override
	public int compareTo(Timespan other) {
		int res = this.getStartTime().compareTo(other.getStartTime());
		if(res == 0)
			res = this.getEndTime().compareTo(other.getEndTime());
		return res;
	}

	public Timespan roundStartingTime() {
		if(getStartTime().getMinute() == 0)
			return this;
		
		//inconsistentie indien seconden of nanoseconden != 0
		LocalDateTime newStart = getStartTime().withMinute(0);
		return new Timespan(newStart, getEndTime()).postponeHours(1);
	}

	public Timespan postponeHours(int hours) {
		LocalDateTime newStart = getStartTime().plusHours(hours);
		if(canHaveAsTimeInterval(newStart, getEndTime()))
			return new Timespan(newStart, getEndTime());
		else 
			return null;
	}

	public boolean startsAfter(LocalDateTime time) {
		return getStartTime().isAfter(time);
	}

	public boolean startsBefore(LocalDateTime time) {
		return getStartTime().isBefore(time);
	}
	
	@Override
	public String toString() {
		return "[" + startTime.toString() + " to " + endTime.toString() + "]";
	}
    
}
