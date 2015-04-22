package domain;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Reservation {

	private final Task task;
	private final Timespan timespan;
	
	/**
	 * 
	 * @param t
	 * @param period
	 * @param needed
	 */
	public Reservation(Task t, Timespan period) {
		task = t;
		timespan = period;
	}

	/****************************************************
	 * Getters & Setters                                *
	 ****************************************************/

	/**
	 * @return 	the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @return 	the timespan
	 */
	public Timespan getTimespan() {
		return timespan;
	}
	
	public LocalDateTime getStartTime() {
		return timespan.getStartTime();
	}
	
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
		return new Comparator<Reservation>() {

			@Override
			public int compare(Reservation o1, Reservation o2) {
				return o1.getTimespan().compareTo(o2.getTimespan());
			}
			
		};
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
	
	public boolean conflictsWith(Reservation reservation) {
		return conflictsWith(reservation.getTimespan());
	}
	
	public boolean expiredBefore(LocalDateTime time) {
		return getEndTime().isBefore(time);
	}
}
