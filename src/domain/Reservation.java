package domain;

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
	
	/**
	 * Return whether or not this reservation causes a conflict with a given time span.
	 * 
	 * @param 	span
	 *       	The time span to check conflict with.
	 * @return	{@code true} if span overlaps with the time span of this reservation.
	 */
	public boolean conflictsWith(Timespan span) {
		return timespan.overlapsWith(span);
	}
}
