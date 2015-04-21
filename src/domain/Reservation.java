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
	
	public Duration timeBetween(Reservation other) {
		return getTimespan().timeBetween(other.getTimespan());
	}
	
	public static Comparator<Reservation> timespanComparator() {
		return new Comparator<Reservation>() {

			@Override
			public int compare(Reservation o1, Reservation o2) {
				return o1.getTimespan().compareTo(o2.getTimespan());
			}
			
		};
	}
}
