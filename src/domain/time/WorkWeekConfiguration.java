package domain.time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a configuration of a bussiness week in an abstract way.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class WorkWeekConfiguration implements Comparable<WorkWeekConfiguration> {

    private static final int BEGIN_WORKWEEK = 1;
    private static final int END_WORKWEEK = 5;
    private static final LocalTime BEGIN_WORKDAY = LocalTime.of(9, 0);
    private static final LocalTime END_WORKDAY = LocalTime.of(18, 0);
    private static final LocalTime BEGIN_LUNCHBREAK = LocalTime.of(12, 0);
    private static final LocalTime END_LUNCHBREAK = LocalTime.of(13, 0);
    
    private static final LocalTime BEGIN_BOUNDRY_LUNCHBREAK = LocalTime.of(11, 0);
    private static final LocalTime END_BOUNDRY_LUNCHBREAK = LocalTime.of(14, 0);
    
    /**
     * A constant for representing there is no lunchbreak.
     */
    public static final LocalTime NO_LUNCHBREAK = LocalTime.of(12, 0);
    /**
     * A constant representing a work week configuration that indicates 24/7
     * availability
     */
    public static final WorkWeekConfiguration ALWAYS
            = new WorkWeekConfiguration(1, 7, LocalTime.MIN, LocalTime.MAX, NO_LUNCHBREAK, NO_LUNCHBREAK);
    
    public static final WorkWeekConfiguration DEFAULT = new WorkWeekConfiguration();

    private final int beginWorkWeek;
    private final int endWorkWeek;
    private final LocalTime beginWorkDay;
    private final LocalTime endWorkDay;
    private final LocalTime beginLunch;
    private final LocalTime endLunch;

    /***************************************
     * Constructors                        *
     ***************************************/
    
    /**
     * Initialize this configuration with the given parameters
     *
     * @param beginWorkWeek The day of the week to start the work week (1 is
     * Monday, 7 is Sunday)
     * @param endWorkWeek The day of the week to end the work week (1 is Monday,
     * 7 is Sunday)
     * @param beginWorkDay The time to start the work day
     * @param endWorkDay The time to end the work day
     * @param beginLunch The time to begin lunch
     * @param endLunch The time to end lunch
     * @throws IllegalArgumentException The given begin and end time of a
     * work day don't form a valid interval.
     * @throws IllegalArgumentException The given begin and end time of a lunch
     * don't form a valid interval.
     */
    public WorkWeekConfiguration(int beginWorkWeek, int endWorkWeek, LocalTime beginWorkDay,
            LocalTime endWorkDay, LocalTime beginLunch, LocalTime endLunch) throws IllegalArgumentException {

        if (!isValidInterval(beginWorkDay, endWorkDay)) {
            throw new IllegalArgumentException("The given begin and end time of a workday don't form a valid interval");
        }
        if (!isValidLunch(beginLunch, endLunch)) {
            throw new IllegalArgumentException("The given begin and end time of the lunch don't form a valid interval");
        }

        this.beginWorkWeek = beginWorkWeek;
        this.endWorkWeek = endWorkWeek;
        this.beginWorkDay = beginWorkDay;
        this.endWorkDay = endWorkDay;
        this.beginLunch = beginLunch;
        this.endLunch = endLunch;
    }

    /**
     * Initialize this configuration with a standard work week of Monday to
     * Friday and the given parameters
     *
     * @param beginWorkDay The time to start the work day
     * @param endWorkDay The time to end the work day
     * @param beginLunch The time to begin lunch
     * @param endLunch The time to end lunch
     * @throws IllegalArgumentException The given begin and end time of a
     * work day don't form a valid interval.
     * @throws IllegalArgumentException The given begin and end time of a lunch
     * don't form a valid interval.
     */
    public WorkWeekConfiguration(LocalTime beginWorkDay,
            LocalTime endWorkDay, LocalTime beginLunch, LocalTime endLunch) throws IllegalArgumentException {

        this(BEGIN_WORKWEEK, END_WORKWEEK, beginWorkDay, endWorkDay, beginLunch, endLunch);
    }

    /**
     * Initialize this configuration with a standard work week from Monday to
     * Friday and work days from the given hours and no lunch break
     *
     * @param beginWorkDay The time to begin the work day
     * @param endWorkDay The time to end the work day
     * @throws IllegalArgumentException The given begin and end time of a day
     * don't form a valid interval.
     */
    public WorkWeekConfiguration(LocalTime beginWorkDay, LocalTime endWorkDay) throws IllegalArgumentException {
        this(BEGIN_WORKWEEK, END_WORKWEEK, beginWorkDay, endWorkDay, NO_LUNCHBREAK, NO_LUNCHBREAK);
    }

    /**
     * Initialize this configuration with a standard work week of Monday to
     * Friday and work days from 9 to 18h and a lunch break from 12 to 13h
     *
     */
    public WorkWeekConfiguration() {
        this(BEGIN_WORKDAY, END_WORKDAY, BEGIN_LUNCHBREAK, END_LUNCHBREAK);
    }

    /****************************************
     * Getters and setters                  *
     ****************************************/
    
    /**
     * @return The start of this work week (between 1 and 7)
     */
    public int getBeginWorkWeek() {
        return beginWorkWeek;
    }

    /**
     * @return The end of the work week (between 1 and 7)
     */
    public int getEndWorkWeek() {
        return endWorkWeek;
    }

    /**
     * @return The time the work day starts
     */
    public LocalTime getBeginWorkDay() {
        return beginWorkDay;
    }

    /**
     * @return The time the work day ends
     */
    public LocalTime getEndWorkDay() {
        return endWorkDay;
    }

    /**
     * @return The time the lunch break starts
     */
    public LocalTime getBeginLunch() {
        return beginLunch;
    }

    /**
     * @return The time the lunch break ends
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
     * Checks whether the given end and begin time are valid, compared to each
     * other.
     *
     * @param begin The begin time to check
     * @param end The end time to check
     * @return True if and only is the given begin time is strictly before the
     * given end time.
     */
    public static boolean isValidLunch(LocalTime begin, LocalTime end) {
        return isValidInterval(begin, end) 
                && ChronoUnit.MINUTES.between(begin, end) <= 60
                && !BEGIN_BOUNDRY_LUNCHBREAK.isAfter(begin)
                && !END_BOUNDRY_LUNCHBREAK.isBefore(end);
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
     * between the begin work day time and the end work day time, including the
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
     * Check whether the given date falls within the work week
     *
     * @param date The date to check
     * @return True if and only if the given date is later than or equal to the
     * start of the work week, and before or equal to the end of the work week.
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
        if (isValidWorkTime(time))
            return time;
        
        if(!isValidWorkDay(time))
        	return nextValidWorkTime(time.plusDays(1).toLocalDate().atTime(getBeginWorkDay()));

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
    }

    /**
     * Compares this work week configuration to the given work week configuration,
     * in terms of the daily availability.
     * Note: this class has a natural ordering that is inconsistent with equals.
     *
     * @param o The work week configuration to compare with
     * @return 1 if this work week configuration has a longer daily availability
     * -1 if this work week configuration has a shorter daily availability 0 if
     * this work week configuration has an equal daily availability
     */
    @Override
    public int compareTo(WorkWeekConfiguration o) {
        if (this.getMinutesOfWorkDay() > o.getMinutesOfWorkDay()) {
            return 1;
        } else if (this.getMinutesOfWorkDay() < o.getMinutesOfWorkDay()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    /**
     * @return A textual representation of this work week configuration
     */
    @Override
    public String toString() {
    	return "days: " + getBeginWorkWeek() + " - " + getEndWorkWeek() + ", "
    			+ "hours: " + getBeginWorkDay() + " - " + getEndWorkDay() + ", "
    			+ "break: " + getBeginLunch() + " - " + getEndLunch();
    }
}
