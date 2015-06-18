package domain;

import domain.task.Task;
import domain.time.Timespan;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * This class represents a reservation of a resource for a task
 * over a certain timespan
 * 
 * @author Mathias, Pieter-Jan, Frederic
 */
public class Reservation {

	private final Task task;
	private final Timespan timespan;
	
	/**
     * Initializes a new reservation for the given task over the given timespan.
     * 
     * @param task The task to make the reservation for
     * @param period The period over which to make the reservation
     */
	public Reservation(Task task, Timespan period) {
		this.task = task;
		this.timespan = period;
	}

	/****************************************************
	 * Getters & Setters                                *
	 ****************************************************/

	/**
	 * @return 	The task this reservation is made for
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @return 	The timespan over which this reservation is made
	 */
	public Timespan getTimespan() {
		return timespan;
	}
	
    /**
     * @return The starttime of this reservation
     */
	public LocalDateTime getStartTime() {
		return timespan.getStartTime();
	}
	
    /**
     * @return The end time of this reservation 
     */
	public LocalDateTime getEndTime() {
		return timespan.getEndTime();
	}

	/****************************************************
	 * Static methods                                   *
	 ****************************************************/

	/**
	 * Get a comparator to sort reservations on their time spans.
	 * NOTE that this comparator can return 0 if {@code !o1.equals(o2)}.
	 * 
	 * @return 	a comparator that compares the time spans of reservations 
	 *        	in order to sort reservations.
	 * @see		Timespan#compareTo(Timespan)
	 */
	public static Comparator<Reservation> timespanComparator() {
            return (Reservation o1, Reservation o2) -> o1.getTimespan().compareTo(o2.getTimespan());
	}
	
	/****************************************************
	 * Other methods                                    *
	 ****************************************************/
	
	/**
	 * Return whether or not this reservation causes a conflict with a given time span.
	 * 
	 * @param 	span
	 *       	The time span to check conflict with.
	 * @return	{@code true} if span overlaps with the time span of this reservation.
	 */
	public boolean conflictsWith(Timespan span) {
		return getTimespan().overlapsWith(span);
	}
	
	/**
	 * Checks whether this reservation conflicts with another reservation
	 * 
	 * @param reservation The reservation to compare with
	 * @return True if and only if the time span of this reservation
	 * overlaps with the time span of the given reservation.
	 */
	public boolean conflictsWith(Reservation reservation) {
		return conflictsWith(reservation.getTimespan());
	}

    /**
     * Checks whether this reservation expires before the given time
     * 
     * @param time The time to check
     * @return True if and only if the timespan of this reservation end
     * strictly before the given time.
     */
	public boolean expiredBefore(LocalDateTime time) {
		return getEndTime().isBefore(time);
	}
        
    /**
     * Checks whether this reservation starts after the given time
     * 
     * @param time The time to check
     * @return True if and only if the timespan of this reservation starts
     * strictly after the given time.
     */
	public boolean startsAfter(LocalDateTime time) {
		return getTimespan().startsAfter(time);
	}
}
