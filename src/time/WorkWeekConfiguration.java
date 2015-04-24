package time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a configuration of a bussiness week in an abstract way.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class WorkWeekConfiguration {


    private final int beginWorkWeek;

    private final int endWorkWeek;

    private final LocalTime beginWorkDay;

    private final LocalTime endWorkDay;

    private final LocalTime beginLunch;

    private final LocalTime endLunch;

    /**
     * **************************************
     * Constructors	
     * **************************************
     */
    
    /**
     * Initialize this configuration with the given parameters
     * 
     * @param beginWorkWeek The day of the week to start the workweek (1 is monday, 7 is sunday)
     * @param endWorkWeek The day of the week to end the workweek (1 is monday, 7 is sunday)
     * @param beginWorkDay The time to start the workday
     * @param endWorkDay The time to end the workday
     * @param beginLunch The time to begin lunch
     * @param endLunch The time to end lunch
     * @throws IllegalArgumentException The given begin and end time of 
     * a workday don't form a valid interval.
     * @throws IllegalArgumentException The given begin and end time of 
     * a lunch don't form a valid interval.
     */
    public WorkWeekConfiguration(int beginWorkWeek, int endWorkWeek, LocalTime beginWorkDay, 
            LocalTime endWorkDay, LocalTime beginLunch, LocalTime endLunch) throws IllegalArgumentException{
        
        if(!isValidInterval(beginWorkDay, endWorkDay))
            throw new IllegalArgumentException("The given begin and end time of a workday don't form a valid interval");
        
        if(!isValidInterval(beginLunch, endLunch))
            throw new IllegalArgumentException("The given begin and end time of the lunch don't form a valid interval");
            
        this.beginWorkWeek = beginWorkWeek;
        this.endWorkWeek = endWorkWeek;
        this.beginWorkDay = beginWorkDay;
        this.endWorkDay = endWorkDay;
        this.beginLunch = beginLunch;
        this.endLunch = endLunch;
    }
    
    /**
     * Initialize this configuration with a standard workweek of monday
     * to friday and the given parameters
     * 
     * @param beginWorkDay The time to start the workday
     * @param endWorkDay The time to end the workday
     * @param beginLunch The time to begin lunch
     * @param endLunch The time to end lunch
     * @throws IllegalArgumentException The given begin and end time of 
     * a workday don't form a valid interval.
     * @throws IllegalArgumentException The given begin and end time of 
     * a lunch don't form a valid interval.
     */
    public WorkWeekConfiguration(LocalTime beginWorkDay, 
            LocalTime endWorkDay, LocalTime beginLunch, LocalTime endLunch) throws IllegalArgumentException {
        
        this(1, 5, beginWorkDay, endWorkDay, beginLunch, endLunch);
    }
    
    /**
     * Initialize this configuration with a standard workweek of monday
     * to friday and workdays from 9 to 18h and the given lunchbreak
     * 
     * @param beginLunch The time to begin lunch
     * @param endLunch The time to end lunch
     * @throws IllegalArgumentException The given begin and end time of 
     * a lunch don't form a valid interval.
     */
    public WorkWeekConfiguration(LocalTime beginLunch, LocalTime endLunch) throws IllegalArgumentException {
        this(1, 5, LocalTime.of(9, 0), LocalTime.of(18, 0), beginLunch, endLunch);
    }   
    
    /**
     * Initialize this configuration with a standard workweek of monday
     * to friday and workdays from 9 to 18h and a lunchbreak from 12 to 13h
     * 
     */
    public WorkWeekConfiguration() {
        this(1, 5, LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(12, 0), LocalTime.of(13, 0));
    }   

    /**
     * **************************************
     * Getters and setters	
     * **************************************
     */
    
    /**
     * 
     * @return The start of this workweek (between 1 and 7)
     */
    public int getBeginWorkWeek() {
        return beginWorkWeek;
    }
    
    /**
     * 
     * @return The end of the workweek (between 1 and 7)
     */
    public int getEndWorkWeek() {
        return endWorkWeek;
    }
    
    /**
     * 
     * @return The time the workday starts
     */
    public LocalTime getBeginWorkDay() {
        return beginWorkDay;
    }

    /**
     * 
     * @return The time the workday ends
     */
    public LocalTime getEndWorkDay() {
        return endWorkDay;
    }
    
    /**
     * 
     * @return The time the lunchbreak starts
     */
    public LocalTime getBeginLunch() {
        return beginLunch;
    }
    
   /**
     * 
     * @return The time the lunchbreak ends
     */
    public LocalTime getEndLunch() {
        return endLunch;
    }
   

  

    /**
     * Checks whether the given end and begin time are valid, compared to each
     * other.
     *
     * @param begin The begin time to check
     * @param end The end time to check
     * @return True if and only is the given begin time is strictly before the
     * given end time.
     */
    public static boolean isValidInterval(LocalDateTime begin, LocalDateTime end) {
        return !begin.isAfter(end);
    }
    
    /**
     * Checks whether the given end and begin time are valid, compared to each
     * other.
     *
     * @param begin The begin time to check
     * @param end The end time to check
     * @return True if and only is the given begin time is strictly before the
     * given end time.
     */
    public static boolean isValidInterval(LocalTime begin, LocalTime end) {
        return !begin.isAfter(end);
    }

    /**
     * @return The number of minutes, the lunch break lasts
     */
    public long getMinutesOfLunchBreak() {
        return ChronoUnit.MINUTES.between(getBeginLunch(), getEndLunch());
    }

    /**
     * @return	the number of hours, a work day lasts (not taking into account
     * any lunch break).
     */
    public long getHoursOfWorkDay() {
        return ChronoUnit.HOURS.between(getBeginWorkDay(), getEndWorkDay());
    }

    /**
     * @return	the number of minutes, a work day lasts (taking into account
     * lunch break).
     */
    public long getMinutesOfWorkDay() {
        return ChronoUnit.MINUTES.between(getBeginWorkDay(), getEndWorkDay()) - getMinutesOfLunchBreak();
    }

    /**
     * @return The number of days in a work week.
     */
    public long getDaysOfWorkWeek() {
        return getEndWorkWeek() - getBeginWorkWeek() + 1;
    }

    /**
     * Check whether the given time falls within the business hours.
     *
     * @param time The time to check
     * @return True is and only in the following cases: - The given time is
     * between the beginworkday time and the endworkday time, including the
     * boundaries and - The given time doesn't fall outside the work week - The
     * given time doesn't fall into a lunch break
     */
    public boolean isValidWorkTime(LocalDateTime time) {
        //too much inefficiency to handle, but clear
        boolean checkDays = time.toLocalTime().compareTo(getBeginWorkDay()) >= 0
                && time.toLocalTime().compareTo(getEndWorkDay()) <= 0;
        boolean checkLunch = !(getBeginLunch().isBefore(time.toLocalTime())
                && time.toLocalTime().isBefore(getEndLunch()));
        return isValidWorkDay(time) && checkDays && checkLunch;
    }

    /**
     * Check whether the given date falls within the workweek
     *
     * @param date The date to check
     * @return True if and only if the given date is later than or equal to the
     * start of the workweek, and before or equal to the end of the workweek.
     */
    public boolean isValidWorkDay(LocalDateTime date) {
        return date.getDayOfWeek().getValue() >= getBeginWorkWeek()
                && date.getDayOfWeek().getValue() <= getEndWorkWeek();
    }

   
    /**
     * Get the first valid working hour past a given time.
     *
     * @param time The time to get the next valid working hour for.
     *
     * @return	the time if it is already a valid working time, the next Monday
     * morning if it was in the weekend, right after lunch break if it was
     * during lunch, as soon as the working day starts if time was before
     * working hours, the start of the next working day if time was after
     * working hours.
     */
    public LocalDateTime nextValidWorkTime(LocalDateTime time) {
        if (isValidWorkTime(time)) {
            return time;
        }

        if (isValidWorkDay(time)) {
            LocalTime beginTime = time.toLocalTime();
            if (beginTime.compareTo(getEndWorkDay()) > 0) {
                if (time.getDayOfWeek().getValue() == getEndWorkWeek()) {
                    time = time.plusDays(7 - getDaysOfWorkWeek());
                }
                return LocalDateTime.of(time.toLocalDate().plusDays(1), getBeginWorkDay());
            } else if (beginTime.compareTo(getBeginWorkDay()) < 0) {
                return LocalDateTime.of(time.toLocalDate(), getBeginWorkDay());
            } else {
                return LocalDateTime.of(time.toLocalDate(), getEndLunch());
            }
        } else {
            return nextValidWorkTime(time.plusDays(1).toLocalDate().atTime(getBeginWorkDay()));
        }
    }
}
