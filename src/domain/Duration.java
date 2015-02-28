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
    
    private static float hoursPerDay = 8; 
    
    private static LocalTime beginWorkDay = LocalTime.of(8, 0);

    private static LocalTime endWorkDay = LocalTime.of(16, 0);

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
     * @return The work time between the 2 given moments in minutes
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
            return ChronoUnit.MINUTES.between(begin.toLocalTime(), end.toLocalTime());
        }
        
        long fullWorkDays = days - 1;
        long minutes = (long) (fullWorkDays * 60 * hoursPerDay);
        
        // first working day worktime
        minutes += ChronoUnit.MINUTES.between(begin.toLocalTime(), getEndWorkDay());
        
        // last working day worktime
        minutes += ChronoUnit.MINUTES.between(getBeginWorkDay(), end.toLocalTime());
        

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
        long days = minutes / (24 * 60);
        long remainingMinutes = minutes % (24 * 60);
        LocalDateTime end  = begin.plusDays(days);
        // check if we can do all the work on the last day, or if we have to add
        // another day
        long availableMinutes = ChronoUnit.MINUTES.between(end.toLocalTime(), getEndWorkDay());
        
        if(availableMinutes >= remainingMinutes){
            return end.plusMinutes(remainingMinutes);
        }else{
            remainingMinutes -= availableMinutes;
            // one day later at the beginning of the workday
            end = LocalDateTime.of(end.toLocalDate().plusDays(1), getBeginWorkDay());
            // add the still remaining minutes
            return end.plusMinutes(remainingMinutes);
           
        }
        
    }
    
    /**
     * Checks whether the given end and begin time are valid, compared to each other.
     * 
     * @param begin The begin time to check
     * @param end The end time to check
     * @return True if and only is the given begin time is strictly before the 
     * given end time.
     */
    private static boolean isValidInterval(LocalDateTime begin, LocalDateTime end){
        return begin.isBefore(end);
    }
    
    /**
     * Check whether the given time falls within the bussiness hours.
     * 
     * @param time The time to check
     * @return True is and only if the given time is between the beginworkday time
     * and the end workday time, including the boundaries and the given times doaesn't
     * fall in a weekend.
     */
    private static boolean isValidWorkTime(LocalDateTime time){
        return time.toLocalTime().compareTo(getBeginWorkDay()) >= 0 &&
                time.toLocalTime().compareTo(getEndWorkDay()) <= 0 &&
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
    private static long getWorDaysBetween(LocalDateTime begin, LocalDateTime end) {
        
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
    public static float getHoursPerDay() {
        return hoursPerDay;
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
