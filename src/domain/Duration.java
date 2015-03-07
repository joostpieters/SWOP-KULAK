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

    private final long minutes;
    
    private static float workHoursPerDay = 8; 
    
    private static LocalTime beginWorkDay = LocalTime.of(9, 0);

    private static LocalTime endWorkDay = LocalTime.of(18, 0);
    
    private static LocalTime beginLunch = LocalTime.of(12, 0);

    private static LocalTime endLunch = LocalTime.of(13, 0);

    /**
     * Initializes this duration based on the given begin and end time.
     * 
     * @param begin When this duration begins
     * @param end When this duration ends
     * @throws IllegalArgumentException The given begin or end time falls out of 
     * the bussiness hours or the given begin and end time don't form a strictly
     * valid interval.
     */
    public Duration(LocalDateTime begin, LocalDateTime end) {
        this(getWorkTimeBetween(begin, end));
    }
    
    /**
     * Initializes this duration with the given amount of minutes.
     * 
     * @param minutes The amount of minutes this duration occupies
     * @throws IllegalArgumentException The given amount of minutes is negative.
     */
    public Duration(long minutes) throws  IllegalArgumentException{
        if(minutes < 0 ){
            throw new IllegalArgumentException("The amount of minutes can't be negative.");
        }
        this.minutes = minutes;
    }

    /**
     * Calculates the time of work between the given begin and end time.
     * 
     * @param begin The begin time
     * @param end The end time
     * @return The work time between the 2 given moments in minutes based on the
     * start and end of a work day and minus the weekends and lunchbreaks.
     * @throws IllegalArgumentException The given time interval is not valid or 
     * the begin or end time is not valid
     */
    private static long getWorkTimeBetween(LocalDateTime begin, LocalDateTime end) throws IllegalArgumentException{
        if(!isValidInterval(begin, end)){
            throw new IllegalArgumentException("The given begin time isn't before the given end time.");
        }
        
        if(!isValidWorkTime(begin) || !isValidWorkTime(end)){
            throw new IllegalArgumentException("The given time is not a valid worktime.");
        }
        
        long days = getWorDaysBetween(begin, end);
        
        // same day
        if(days == 0){
            // contains lunch break
             if(begin.toLocalTime().compareTo(getBeginLunch()) <= 0 &&
                     end.toLocalTime().compareTo(getEndLunch()) >= 0){
                return ChronoUnit.MINUTES.between(begin.toLocalTime(), end.toLocalTime())-
                        getMinutesOfLunchBreak();
             }
            return ChronoUnit.MINUTES.between(begin.toLocalTime(), end.toLocalTime());
        }
        
        long fullWorkDays = days - 1;
        long minutes = (long) (fullWorkDays * 60 * workHoursPerDay);
        
        // The first workday timespan indludes a lunch break
        if(begin.toLocalTime().compareTo(getBeginLunch()) <= 0){
            // first working day worktime minus the lunch break
            minutes += ChronoUnit.MINUTES.between(begin.toLocalTime(), getEndWorkDay())
                        - getMinutesOfLunchBreak();
        }else{
            // first working day worktime
            minutes += ChronoUnit.MINUTES.between(begin.toLocalTime(), getEndWorkDay());
        }
       
        // The last workday timespan indludes a lunch break
        if(end.toLocalTime().compareTo(getBeginLunch()) <= 0){
            // last working day worktime minus the lunch break
            minutes += ChronoUnit.MINUTES.between(getBeginWorkDay(), end.toLocalTime())
                        - getMinutesOfLunchBreak();
        }else{
            // last working day worktime
            minutes += ChronoUnit.MINUTES.between(getBeginWorkDay(), end.toLocalTime());
        }       

        return minutes;
    }
    
    /**
     * Calculates the time this duration would end, given a start time, while 
     * taking into account the bussiness hours.
     * 
     * @param begin The time to start from
     * @return A LocalDateTime marking the end of this duration that starts at
     * the given start time.
     * @throws IllegalArgumentException The given time doesn't lay in between the 
     * bussiness hours.
     */
    public LocalDateTime getEndTimeFrom(LocalDateTime begin) throws IllegalArgumentException{
        
        if(!isValidWorkTime(begin)){
            throw new IllegalArgumentException("The given time is not a valid working time.");
        }
        
        // calculate full work days
        long days = (long) (minutes / (getWorkHoursPerDay() * 60));
        long remainingMinutes = (long) (minutes % (getWorkHoursPerDay() * 60));
        LocalDateTime end  = begin.plusDays(days);
        long availableMinutes;
        boolean hasLunchBreak = false;
        long minutesToEndWorkDay = ChronoUnit.MINUTES.between(end.toLocalTime(), getEndWorkDay());
        // check if we can do all the work on the last day, or if we have to add
        // another day
        if(end.toLocalTime().compareTo(getBeginLunch()) <= 0){
            // lunch break
            availableMinutes = minutesToEndWorkDay - getMinutesOfLunchBreak();
            hasLunchBreak = true;
        }else{
            availableMinutes = minutesToEndWorkDay;
        }
        
        
        // there are enough minutes left
        if(availableMinutes >= remainingMinutes){
            // don't forget to skip the lunch time, if present
            if(end.toLocalTime().compareTo(getBeginLunch()) >= 0){
                
                // The time falls after of in a lunch break, so skip the lunch break 
                return end.plusMinutes(remainingMinutes).plusMinutes(getMinutesOfLunchBreak());
            }
            
            return end.plusMinutes(remainingMinutes);
            
            
        }else{
            remainingMinutes -= availableMinutes;
            // one day later at the beginning of the workday
            end = LocalDateTime.of(end.toLocalDate().plusDays(1), getBeginWorkDay());
            // add the still remaining minutes
            if(end.toLocalTime().compareTo(getBeginLunch()) >= 0){
                
                // The time falls after of in a lunch break, so skip the lunch break 
                return end.plusMinutes(remainingMinutes).plusMinutes(getMinutesOfLunchBreak());
            }
            return end.plusMinutes(remainingMinutes);
           
        }
        
    }
    
    /**
     * 
     * @return The number of minutes, the lunch break lasts
     */
    private static long getMinutesOfLunchBreak() {
        return ChronoUnit.MINUTES.between(getBeginLunch(), getEndLunch());
    }
    
    /**
     * Checks whether the given end and begin time are valid, compared to each other.
     * 
     * @param begin The begin time to check
     * @param end The end time to check
     * @return True if and only is the given begin time is strictly before the 
     * given end time.
     */
    public static boolean isValidInterval(LocalDateTime begin, LocalDateTime end){
        return begin.isBefore(end);
    }
    
    /**
     * Check whether the given time falls within the bussiness hours.
     * 
     * @param time The time to check
     * @return True is and only in the followinging cases:
     * -The given time is between the beginworkday time and the end workday time, 
     * including the boundaries and 
     * -The given time doesn't fall in a weekend
     * -The given time doesn't fall into a lunch break
     */
    public static boolean isValidWorkTime(LocalDateTime time){
        return time.toLocalTime().compareTo(getBeginWorkDay()) >= 0 &&
                time.toLocalTime().compareTo(getEndWorkDay()) <= 0 &&
                !(getBeginLunch().isBefore(time.toLocalTime()) && time.toLocalTime().isBefore(getEndLunch())) &&
                !(time.getDayOfWeek().equals(DayOfWeek.SATURDAY) || time.getDayOfWeek().equals(DayOfWeek.SUNDAY));
    }
    
    
    
    /**
     * Get the number of workdays between the given time moments.
     * 
     * @param begin The begin time
     * @param end The end time
     * @return The number of weekdays between the given moments, including the begin
     * excluding the end.
     */
    public static long getWorDaysBetween(LocalDateTime begin, LocalDateTime end) {
        
        int w1 = begin.getDayOfWeek().getValue();
        begin = begin.minusDays(w1-1);

        int w2 = end.getDayOfWeek().getValue();
        end = end.minusDays(w2-1);

        //end Saturday to start Saturday 
        long days = ChronoUnit.DAYS.between(begin, end);
        long daysWithoutSunday = days - (days * 2 / 7);

        return daysWithoutSunday - w1 + w2;
    }
    
    /**
     * 
     * @return The hour field of this duration.
     */
    public long getHours(){
        return minutes / 60;
    }
    
    /**
     * 
     * @return The minute field of this duration, value between 0 and 59 
     */
    public int getMinutes(){
        return (int) minutes % 60;
    }
    
    /**
     * 
     * @return The total number of minutes in this duration.
     */
    public long toMinutes(){
        return minutes;
    }
    
    /**
     * 
     * @return The amount of working hours in 1 day.
     */
    public static float getWorkHoursPerDay() {
        return workHoursPerDay;
    }
    
    /**
     * 
     * @return The time the working day starts.
     */
    public static LocalTime getBeginWorkDay() {
        return beginWorkDay;
    }
    
    /**
     * 
     * @return The time a working day ends. 
     */
    public static LocalTime getEndWorkDay() {
        return endWorkDay;
    }
    
    /**
     * 
     * @return The time the lunch break begins. 
     */
    public static LocalTime getBeginLunch() {
        return beginLunch;
    }

    /**
     * 
     * @return The time the lunch break ends.
     */
    public static LocalTime getEndLunch() {
        return endLunch;
    }
    
    /**
     * Returns a copy of this duration with the given amount of minutes added.
     * 
     * @param minutesToAdd The amount of minutes to add
     * @return A duration, based on this duration with the given amount of minutes added.
     * @throws IllegalArgumentException The given amount of minutes causes the duration to be negative.
     */
    public Duration add(long minutesToAdd) throws IllegalArgumentException{
        return new Duration(minutes + minutesToAdd);
    }
    
    /**
     * Returns a copy of this duration with the given Duration added.
     * 
     * @param otherDuration The Duration to add to this duration
     * @return A Duration, based on this duration with the given amount of 
     * time the given duration represents added.
     */
    public Duration add(Duration otherDuration){
        return new Duration(minutes + otherDuration.toMinutes());
    }
    
    /**
     * Returns a copy of this duration with the given Duration subtracted.
     * 
     * @param otherDuration The Duration to subtract from this duration
     * @return A Duration, based on this duration with the given amount of 
     * time the given duration represents added.
     * @throws IllegalArgumentException The given duration to subtract
     * causes the duration to be negative.
     */
    public Duration subtract(Duration otherDuration) throws IllegalArgumentException{
        return new Duration(minutes - otherDuration.toMinutes());
    }
    
    /**
     * Returns a copy of this duration multiplied by the given mulitplicant
     * 
     * @param multiplicant The value to multiply this duration by
     * @return A duration, based on this duration, multiplied by the given multiplicant,
     * rounded to the nearest integer.
     */
    public Duration multiplyBy(double multiplicant){
        return new Duration(Math.round(multiplicant * toMinutes()));
    }
    
    /**
     * Compare the length of this duration with the given duration 
     * 
     * @param duration The duration to compare with
     * @return A negative number if this duration is shorter than the given duration,
     * a positive value if this duration is longer, 0 if they are equal in length.
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
}
