package domain;

import java.time.DayOfWeek;
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
        if(minutes < duration.toMinutes()){
            return -1;
        }else if (minutes > duration.toMinutes()){
            return 1;
        }else{
            return 0;
        }
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
    private static long getMinutesOfLunchBreak() {
        return ChronoUnit.MINUTES.between(BEGINLUNCH, ENDLUNCH);
    }
    
    /**
     * @return 	The number of days in a work week.
     */
    private static long getDaysOfWorkWeek() {
        return ENDWORKWEEK - BEGINWORKWEEK + 1;
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
     * 			- The given time doesn't fall in a weekend
     * 			- The given time doesn't fall into a lunch break
     */
    public static boolean isValidWorkTime(LocalDateTime time){
        boolean checkDays = time.toLocalTime().compareTo(BEGINWORKDAY) >= 0 && 
        		time.toLocalTime().compareTo(ENDWORKDAY) <= 0;
		boolean checkWeekends = !(time.getDayOfWeek().equals(DayOfWeek.SATURDAY) || 
				time.getDayOfWeek().equals(DayOfWeek.SUNDAY));
        boolean checkLunch = !(BEGINLUNCH.isBefore(time.toLocalTime()) && 
        		time.toLocalTime().isBefore(ENDLUNCH));
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
        
        long days = getWorkDaysBetween(begin, end);
        
        // same day
        if(days == 0) {
        	boolean test = begin.toLocalTime().compareTo(BEGINLUNCH) <= 0 &&
                    end.toLocalTime().compareTo(ENDLUNCH) >= 0;
        	return ChronoUnit.MINUTES.between(begin.toLocalTime(), end.toLocalTime()) - 
        			((test) ? getMinutesOfLunchBreak() : 0);
        }
        
        long fullWorkDays = days - 1;
        long minutes = (long) (fullWorkDays * 60 * WORKHOURSPERDAY);

        minutes += ChronoUnit.MINUTES.between(begin.toLocalTime(), ENDWORKDAY);
        minutes += ChronoUnit.MINUTES.between(BEGINWORKDAY, end.toLocalTime()); 
        
        // The first workday timespan includes a lunch break
        minutes -= (begin.toLocalTime().compareTo(BEGINLUNCH) <= 0) ? getMinutesOfLunchBreak() : 0;
        //last workday timespan inclused a lunch break
        //TODO: compareTo() > 0, niet???
        minutes -= (end.toLocalTime().compareTo(BEGINLUNCH) <= 0) ? getMinutesOfLunchBreak() : 0;
        
        return minutes;
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
        
        // calculate full work days
        long days = (long) (minutes / (WORKHOURSPERDAY * 60));
        long remainingMinutes = (long) (minutes % (WORKHOURSPERDAY * 60));
        LocalDateTime end  = begin.plusDays(days);
        long minutesToEndWorkDay = ChronoUnit.MINUTES.between(end.toLocalTime(), ENDWORKDAY);
        // check if we can do all the work on the last day, or if we have to add another day
        minutesToEndWorkDay -= (end.toLocalTime().compareTo(BEGINLUNCH) <= 0) ? Duration.getMinutesOfLunchBreak() : 0;
        
        // there are enough minutes left
        if(minutesToEndWorkDay >= remainingMinutes){
            // don't forget to skip the lunch time, if present
            if(end.toLocalTime().compareTo(BEGINLUNCH) >= 0) {
                // The time falls after or in a lunch break, so skip the lunch break 
                return end.plusMinutes(remainingMinutes + getMinutesOfLunchBreak());
            }
            return end.plusMinutes(remainingMinutes);  
        }else{
            remainingMinutes -= minutesToEndWorkDay;
            //If last day of week, skip over the weekend.
            end = (end.getDayOfWeek().getValue() == ENDWORKWEEK) ? end.plusDays(7 - Duration.getDaysOfWorkWeek()) : end;
            // one day later at the beginning of the workday
            end = LocalDateTime.of(end.toLocalDate().plusDays(1), BEGINWORKDAY);
            // add the still remaining minutes
            if(end.toLocalTime().compareTo(BEGINLUNCH) >= 0) {
                // The time falls after of in a lunch break, so skip the lunch break 
                return end.plusMinutes(remainingMinutes + Duration.getMinutesOfLunchBreak());
            }
            return end.plusMinutes(remainingMinutes);
           
        }    
    }
    
    @Override
    public boolean equals(Object o) {
    	Duration other = (Duration) o;
    	return this.getHours() == other.getHours() && this.getMinutes() == other.getMinutes();
    }
}
