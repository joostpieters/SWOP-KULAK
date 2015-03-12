package domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


/**
 * This class represents a duration over a bussiness week in an abstract way.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Duration implements Comparable<Duration>{
        
    /** Constant to indicate the first day of the work week. 
     * The value for this constant is {@value} (Monday is 1, Sunday is 7). */
	public static final int BEGINWORKWEEK = 1;
    /** Constant to indicate the last day of the work week. 
     * The value for this constant is {@value} (Monday is 1, Sunday is 7). */
	public static final int ENDWORKWEEK = 5;	
    /** Constant to indicate the begin time of a work day. */
	public static final LocalTime BEGINWORKDAY = LocalTime.of(9, 0);
	/** Constant to indicate the end time of a work day. */
	public static final LocalTime ENDWORKDAY = LocalTime.of(18, 0);
    /** Constant to indicate the begin time of the lunch. */
	public static final LocalTime BEGINLUNCH = LocalTime.of(12, 0);
    /** Constant to indicate the end time of the lunch. */
	public static final LocalTime ENDLUNCH = LocalTime.of(13, 0);
    /** Constant for a duration of zero. */
    public static final Duration ZERO = new Duration(0);

	private final long minutes;
    /****************************************
     * Constructors							*
     ****************************************/    
    
    /**
     * Initializes this duration based on the given begin and end time.
     * 
     * @param 	begin 
     * 			When this duration begins
     * @param 	end 
     * 			When this duration ends
     * @throws 	IllegalArgumentException The given begin or end time falls out of 
     * 			the bussiness hours or the given begin and end time don't form 
     * 			a strictly valid interval.
     */
    public Duration(LocalDateTime begin, LocalDateTime end) {
        this(getWorkTimeBetween(begin, end));
    }
     
    /**
     * Initialize this duration with a given amount of hours and minutes.
     * 
     * @param 	hours
     * 			The amount of hours this duration occupies.
     * @param	minutes
     * 			The amount of extra minutes this duration occupies.
     */
    public Duration(int hours, int minutes) {
    	this((long) hours * 60 + minutes);
    }
    
    /**
     * Initializes this duration with the given amount of minutes.
     * 
     * @param 	minutes 
     * 			The amount of minutes this duration occupies
     * @throws 	IllegalArgumentException 
     * 			The given amount of minutes is negative.
     */
    public Duration(long minutes) throws  IllegalArgumentException{
        if(minutes < 0 ){
            throw new IllegalArgumentException("The amount of minutes can't be negative.");
        }
        this.minutes = minutes;
    }
    
    /****************************************
     * Getters and setters					*
     ****************************************/
    
    /**
     * @return 	The hour field of this duration.
     */
    public long getHours(){
        return minutes / 60;
    }
    
    /**
     * @return 	The minute field of this duration, value between 0 and 59 
     */
    public int getMinutes(){
        return (int) minutes % 60;
    }
    
    /**
     * @return 	The total number of minutes in this duration.
     */
    public long toMinutes(){
        return minutes;
    }
    
    /****************************************
     * arithmetics							*
     ****************************************/
    
    /**
     * Returns a copy of this duration with the given amount of minutes added.
     * 
     * @param 	minutesToAdd 
     * 			The amount of minutes to add
     * @return 	A duration, based on this duration with the given amount of minutes added.
     * @throws 	IllegalArgumentException 
     * 			if the given amount of minutes causes the duration to be negative.
     */
    public Duration add(long minutesToAdd) throws IllegalArgumentException{
        return new Duration(minutes + minutesToAdd);
    }
    
    /**
     * Returns a copy of this duration with the given Duration added.
     * 
     * @param 	otherDuration 
     * 			The Duration to add to this duration
     * @return 	A Duration, based on this duration with the given amount of 
     * 			time the given duration represents added.
     */
    public Duration add(Duration otherDuration){
        return new Duration(minutes + otherDuration.toMinutes());
    }
    
    /**
     * Returns a copy of this duration with the given Duration subtracted.
     * 
     * @param 	otherDuration 
     * 			The Duration to subtract from this duration
     * @return 	A Duration, based on this duration with the given amount of 
     * 			time the given duration represents added.
     * @throws 	IllegalArgumentException 
     * 			if the given duration to subtract causes the duration to be negative.
     */
    public Duration subtract(Duration otherDuration) throws IllegalArgumentException{
        return new Duration(minutes - otherDuration.toMinutes());
    }
    
    /**
     * Returns a copy of this duration multiplied by the given multiplicand
     * 
     * @param 	multiplicand 
     * 			The value to multiply this duration by
     * @return 	A duration, based on this duration, multiplied by the given multiplicand,
     * 			rounded to the nearest integer.
     */
    public Duration multiplyBy(double multiplicand){
        return new Duration(Math.round(multiplicand * toMinutes()));
    }
    
    /**
     * Calculate how much this duration lasts longer than another duration.
     * 
     * @param 	other
     * 			The duration to compare with.
     * 
     * @return	0 if the given duration is longer.
     * 			(this - other) / other - 1 otherwise.
     */
    public double percentageOver(Duration other) {
    	long dif = toMinutes()- other.toMinutes();
    	if(dif < 0)
    		return 0;
    	
    	return (double) dif / other.toMinutes();
    }
    
    /**
     * Compare the length of this duration with the given duration 
     * 
     * @param 	duration 
     * 			The duration to compare with
     * @return 	A negative number if this duration is shorter than the given duration,
     *			a positive value if this duration is longer, 0 if they are equal in length.
     */
    @Override
    public int compareTo(Duration duration) {
    	return Long.signum(minutes - duration.toMinutes());
    }
    
    /****************************************
     * static methods						*
     ****************************************/
    
    /**
     * Checks whether the given end and begin time are valid, compared to each other.
     * 
     * @param 	begin 
     * 			The begin time to check
     * @param 	end 
     * 			The end time to check
     * @return 	True if and only is the given begin time is strictly before the 
     * 			given end time.
     */
    public static boolean isValidInterval(LocalDateTime begin, LocalDateTime end) {
        return !begin.isAfter(end);
    }
    
    /**
     * @return 	The number of minutes, the lunch break lasts
     */
    public static long getMinutesOfLunchBreak() {
        return ChronoUnit.MINUTES.between(Duration.BEGINLUNCH, Duration.ENDLUNCH);
    }
    
    /**
     * @return	the number of hours, a work day lasts (not taking into account any lunch break).
     */
    public static long getHoursOfWorkDay() {
    	return ChronoUnit.HOURS.between(Duration.BEGINWORKDAY, Duration.ENDWORKDAY);
    }
    
    /**
     * @return	the number of minutes, a work day lasts (taking into account lunch break).
     */
    public static long getMinutesOfWorkDay() {
    	return ChronoUnit.MINUTES.between(Duration.BEGINWORKDAY, Duration.ENDWORKDAY) - getMinutesOfLunchBreak();
    }
    
    /**
     * @return 	The number of days in a work week.
     */
    public static long getDaysOfWorkWeek() {
        return Duration.ENDWORKWEEK - Duration.BEGINWORKWEEK + 1;
    }

	
	/**
	 * Check whether the given time falls within the business hours.
	 * 
	 * @param 	time 
	 * 			The time to check
	 * @return 	True is and only in the following cases:
	 * 			- The given time is between the beginworkday time and the endworkday time, 
	 * 			  including the boundaries and 
	 * 			- The given time doesn't fall outside the work week
	 * 			- The given time doesn't fall into a lunch break
	 */
	public static boolean isValidWorkTime(LocalDateTime time){
		//too much inefficiency to handle, but clear
		boolean checkDays = time.toLocalTime().compareTo(Duration.BEGINWORKDAY) >= 0 && 
				time.toLocalTime().compareTo(Duration.ENDWORKDAY) <= 0;
		boolean checkLunch = !(Duration.BEGINLUNCH.isBefore(time.toLocalTime()) && 
				time.toLocalTime().isBefore(Duration.ENDLUNCH));
		return isValidWorkDay(time) && checkDays && checkLunch;
	}
	
        /**
         * Check whether the given date falls within the workweek
         * 
         * @param date The date to check
         * @return True if and only if the given date is later than or equal 
         * to the start of the workweek, and before or equal to the end of the 
         * workweek.
         */
	public static boolean isValidWorkDay(LocalDateTime date) {
		return date.getDayOfWeek().getValue() >= Duration.BEGINWORKWEEK && 
				date.getDayOfWeek().getValue() <= Duration.ENDWORKWEEK;
	}

	/**
	 * Calculates the time of work between the given begin and end time.
	 * 
	 * @param 	begin 
	 * 			The begin time
	 * @param 	end 
	 * 			The end time
	 * @return 	The work time between the 2 given moments in minutes based on the
	 * 			start and end of a work day and minus the weekends and lunch breaks.
	 * 			In case of invalid begin or end time, the next valid work time is used.
	 * @throws 	IllegalArgumentException 
	 * 			The given time interval is not valid.
	 */
	private static long getWorkTimeBetween(LocalDateTime begin, LocalDateTime end) throws IllegalArgumentException{
		if(!Duration.isValidInterval(begin, end))
			throw new IllegalArgumentException("The given begin time isn't before the given end time.");
		if(!Duration.isValidWorkTime(begin))
			begin = Duration.nextValidWorkTime(begin);
		if(!Duration.isValidWorkTime(end))
			end = Duration.nextValidWorkTime(end);

		long minutesTillEnd = ChronoUnit.MINUTES.between(begin,  end);
		long totalMinutes = 0;
		LocalTime beginLocalTime = begin.toLocalTime();
		LocalDateTime nextBegin;
		if(beginLocalTime.compareTo(ENDLUNCH) < 0)
		{
			long minutesTillLunch = ChronoUnit.MINUTES.between(beginLocalTime,BEGINLUNCH);
			if(minutesTillEnd <= minutesTillLunch)
				return minutesTillEnd;
			else
				totalMinutes += minutesTillLunch;
			//nextBegin set to end of lunch break
			nextBegin = LocalDateTime.of(begin.toLocalDate(), ENDLUNCH);

		}
		else //if(beginLocalTime.compareTo(ENDLUNCH) >= 0)
		{
			long minutesTillEndOfDay = ChronoUnit.MINUTES.between(beginLocalTime,ENDWORKDAY);
			if(minutesTillEnd <= minutesTillEndOfDay)
				return minutesTillEnd;
			else
				totalMinutes += minutesTillEndOfDay;
			int weekDay = begin.getDayOfWeek().getValue();
			if(weekDay == ENDWORKWEEK)
				nextBegin = LocalDateTime.of(begin.toLocalDate().plusDays(8 - Duration.getDaysOfWorkWeek()), BEGINWORKDAY);
			else
				nextBegin = LocalDateTime.of(begin.toLocalDate().plusDays(1), BEGINWORKDAY);
		}
		return totalMinutes + getWorkTimeBetween(nextBegin, end);
	}
	
	/**
	 * Get the first valid working hour past a given time.
	 * 
	 * @param 	time
	 * 			The time to get the next valid working hour for.
	 * 
	 * @return	the time if it is already a valid working time,
	 * 			the next Monday morning if it was in the weekend,
	 * 			right after lunch break if it was during lunch,
	 * 			as soon as the working day starts if time was before working hours,
	 * 			the start of the next working day if time was after working hours.
	 */
	public static LocalDateTime nextValidWorkTime(LocalDateTime time) {
		if(Duration.isValidWorkTime(time))
			return time;
		
		if(Duration.isValidWorkDay(time)) {
			LocalTime beginTime = time.toLocalTime();
			if(beginTime.compareTo(Duration.ENDWORKDAY) > 0) {
				if(time.getDayOfWeek().getValue() == Duration.ENDWORKWEEK)
					time = time.plusDays(7 - Duration.getDaysOfWorkWeek());
				return LocalDateTime.of(time.toLocalDate().plusDays(1), Duration.BEGINWORKDAY);
			} else if(beginTime.compareTo(Duration.BEGINWORKDAY) < 0) {
				return LocalDateTime.of(time.toLocalDate(), Duration.BEGINWORKDAY);
			} else {
				return LocalDateTime.of(time.toLocalDate(), Duration.ENDLUNCH);
			}
		} else {
			return Duration.nextValidWorkTime(time.plusDays(1).toLocalDate().atTime(Duration.BEGINWORKDAY));
		}
	}

	/****************************************
	 * other methods						*
	 ****************************************/

	/**
	 * Calculates the time this duration would end, given a start time, while 
	 * taking into account the business hours.
	 * 
	 * @param 	begin 
	 * 			The time to start from
	 * @return 	a LocalDateTime marking the end of this duration that starts at
	 * 			the given start time.
	 * @throws 	IllegalArgumentException 
	 * 			if the given time doesn't lay in between the business hours.
	 */
	public LocalDateTime getEndTimeFrom(LocalDateTime begin) throws IllegalArgumentException{
		LocalDateTime start = begin;
		if(!isValidWorkTime(begin))
			start = nextValidWorkTime(begin);

		long minutesRemaining = minutes;

		if(minutesRemaining == 0)
			return start;

		LocalTime beginLocalTime = start.toLocalTime();
		LocalDateTime nextBegin;
		if(beginLocalTime.compareTo(ENDLUNCH) < 0)
		{
			long minutesTillLunch = ChronoUnit.MINUTES.between(beginLocalTime,BEGINLUNCH);
			if(minutesRemaining <= minutesTillLunch)
				return start.plusMinutes(minutesRemaining);
			else
				minutesRemaining -= minutesTillLunch;

			//nextBegin set to end of lunch break
			nextBegin = LocalDateTime.of(start.toLocalDate(), ENDLUNCH);
		}
		else //if(beginLocalTime.compareTo(ENDLUNCH) >= 0)
		{
			long minutesTillEndOfDay = ChronoUnit.MINUTES.between(beginLocalTime,ENDWORKDAY);
			if(minutesRemaining <= minutesTillEndOfDay)
				return start.plusMinutes(minutesRemaining);
			else
				minutesRemaining -= minutesTillEndOfDay;
			int weekDay = start.getDayOfWeek().getValue();
			if(weekDay == ENDWORKWEEK)
				nextBegin = LocalDateTime.of(start.toLocalDate().plusDays(8 - Duration.getDaysOfWorkWeek()), BEGINWORKDAY);
			else
				nextBegin = LocalDateTime.of(start.toLocalDate().plusDays(1), BEGINWORKDAY);
		}
		return new Duration(minutesRemaining).getEndTimeFrom(nextBegin);
	}
        
        /**
         * 
         * @return 	a textual representation of this duration in the form of: 
         * 			**hours **minutes
         */
        @Override
        public String toString(){
            return getHours() + " hours  " + getMinutes() + " minutes";
        }
        
        /**
         * Checks whether the given duration is equal to the given duration.
         * 
         * @param 	o 
		 *			The other duration to check
         * @return 	true if and only if the given object is a duration and represents the same 
         * 			amount of time like this duration.
         */
	@Override
	public boolean equals(Object o) {
            if(!(o instanceof Duration)){
        
                return false;
            }
		Duration other = (Duration) o;
		return this.getHours() == other.getHours() && this.getMinutes() == other.getMinutes();
	}
        /**
         * @return 	the hashcode of this duration, durations that represent the same
         * 			ammount of time, 
         */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.minutes ^ (this.minutes >>> 32));
        return hash;
    }
}
