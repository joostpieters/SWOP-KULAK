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

	public static final float WORKHOURSPERDAY = 8; 
	public static final int BEGINWORKWEEK = 1;										//monday
	public static final int ENDWORKWEEK = 5;										//friday
	public static final LocalTime BEGINWORKDAY = LocalTime.of(9, 0);
	public static final LocalTime ENDWORKDAY = LocalTime.of(18, 0);
	public static final LocalTime BEGINLUNCH = LocalTime.of(12, 0);
	public static final LocalTime ENDLUNCH = LocalTime.of(13, 0);

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
     * Returns a copy of this duration multiplied by the given mulitplicant
     * 
     * @param 	multiplicant 
     * 			The value to multiply this duration by
     * @return 	A duration, based on this duration, multiplied by the given multiplicant,
     * 			rounded to the nearest integer.
     */
    public Duration multiplyBy(double multiplicant){
        return new Duration(Math.round(multiplicant * toMinutes()));
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
    public static boolean isValidInterval(LocalDateTime begin, LocalDateTime end){
        return begin.isBefore(end);
    }
    
    /**
     * @return 	The number of minutes, the lunch break lasts
     */
    public static long getMinutesOfLunchBreak() {
        return ChronoUnit.MINUTES.between(Duration.BEGINLUNCH, Duration.ENDLUNCH);
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
	 * Get the number of work days between the given time moments.
	 * 
	 * @param 	begin 
	 * 			The begin time
	 * @param 	end 
	 * 			The end time
	 * @return 	The number of weekdays between the given moments, including the begin
	 * 			excluding the end.
	 */
	public static long getWorkDaysBetween(LocalDateTime begin, LocalDateTime end) {
		int w1 = begin.getDayOfWeek().getValue();
		begin = begin.minusDays(w1-1);

		int w2 = end.getDayOfWeek().getValue();
		end = end.minusDays(w2-1);

		//end Saturday to start Saturday 
		long days = ChronoUnit.DAYS.between(begin, end);
		long daysWithoutSunday = days / 7 * Duration.getDaysOfWorkWeek();

		return daysWithoutSunday - w1 + w2;
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
		boolean checkWeekends = time.getDayOfWeek().getValue() >= Duration.BEGINWORKWEEK && 
				time.getDayOfWeek().getValue() <= Duration.ENDWORKWEEK;
		boolean checkLunch = !(Duration.BEGINLUNCH.isBefore(time.toLocalTime()) && 
				time.toLocalTime().isBefore(Duration.ENDLUNCH));
		return checkDays && checkLunch && checkWeekends;
	}

	/**
	 * Calculates the time of work between the given begin and end time.
	 * 
	 * @param 	begin 
	 * 			The begin time
	 * @param 	end 
	 * 			The end time
	 * @return 	The work time between the 2 given moments in minutes based on the
	 * 			start and end of a work day and minus the weekends and lunchbreaks.
	 * @throws 	IllegalArgumentException 
	 * 			The given time interval is not valid or 
	 * 			the begin or end time is not valid
	 */
	private static long getWorkTimeBetween(LocalDateTime begin, LocalDateTime end) throws IllegalArgumentException{
		if(!isValidInterval(begin, end))
			throw new IllegalArgumentException("The given begin time isn't before the given end time.");
		if(!isValidWorkTime(begin) || !isValidWorkTime(end))
			throw new IllegalArgumentException("Begin- as well as endtime should be during workhours.");

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

	/****************************************
	 * other methods						*
	 ****************************************/

	/**
	 * Calculates the time this duration would end, given a start time, while 
	 * taking into account the business hours.
	 * 
	 * @param 	begin 
	 * 			The time to start from
	 * @return 	A LocalDateTime marking the end of this duration that starts at
	 * 			the given start time.
	 * @throws 	IllegalArgumentException 
	 * 			if the given time doesn't lay in between the business hours.
	 */
	public LocalDateTime getEndTimeFrom(LocalDateTime begin) throws IllegalArgumentException{
		if(!isValidWorkTime(begin))
			throw new IllegalArgumentException("The given time is not a valid working time.");

		long minutesRemaining = minutes;

		if(minutesRemaining == 0)
			return begin;

		LocalTime beginLocalTime = begin.toLocalTime();
		LocalDateTime nextBegin;
		if(beginLocalTime.compareTo(ENDLUNCH) < 0)
		{
			long minutesTillLunch = ChronoUnit.MINUTES.between(beginLocalTime,BEGINLUNCH);
			if(minutesRemaining <= minutesTillLunch)
				return begin.plusMinutes(minutesRemaining);
			else
				minutesRemaining -= minutesTillLunch;

			//nextBegin set to end of lunch break
			nextBegin = LocalDateTime.of(begin.toLocalDate(), ENDLUNCH);
		}
		else //if(beginLocalTime.compareTo(ENDLUNCH) >= 0)
		{
			long minutesTillEndOfDay = ChronoUnit.MINUTES.between(beginLocalTime,ENDWORKDAY);
			if(minutesRemaining <= minutesTillEndOfDay)
				return begin.plusMinutes(minutesRemaining);
			else
				minutesRemaining -= minutesTillEndOfDay;
			int weekDay = begin.getDayOfWeek().getValue();
			if(weekDay == ENDWORKWEEK)
				nextBegin = LocalDateTime.of(begin.toLocalDate().plusDays(8 - Duration.getDaysOfWorkWeek()), BEGINWORKDAY);
			else
				nextBegin = LocalDateTime.of(begin.toLocalDate().plusDays(1), BEGINWORKDAY);
		}
		return new Duration(minutesRemaining).getEndTimeFrom(nextBegin);
	}
	
	@Override
	public boolean equals(Object o) {
		Duration other = (Duration) o;
		return this.getHours() == other.getHours() && this.getMinutes() == other.getMinutes();
	}
}
