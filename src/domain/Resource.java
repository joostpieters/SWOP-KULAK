package domain;

import exception.ConflictException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
public class Resource implements ClockObserver {

	private static int nextId = 0;

	//TODO: wordt het niet stilletjesaan interessant om een id-klasse te maken?
	private final int id;
	private final String name;
	private final Set<Reservation> reservations;
	private final Set<Reservation> previousReservations;

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public Resource(String name, ResourceType type) {
		this.id = generateId();
		this.name = name;
		this.reservations = new HashSet<>();
		this.previousReservations = new HashSet<>();
	}

	/****************************************************
	 * Getters & Setters                                *
	 ****************************************************/

	/**
	 * Generates an id for a new Resource.
	 *
	 * @return The id to be used for a newly created task.
	 */
	private static int generateId() {
		return nextId++;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the reservations
	 */
	public Set<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * @return The previous reservations.
	 */
	public Set<Reservation> getPreviousReservations() {
		return previousReservations;
	}

	/****************************************************
	 * Accessors                                       *
	 ****************************************************/

	/**
	 * Return whether or not this resource is available during a given time span.
	 * 
	 * @param 	span
	 * 			The time span the resource should be available in.
	 * @return	{@code true} if there exist no reservation of this resource 
	 *        	with a time span overlapping span.
	 */
	public boolean isAvailable(Timespan span) {
		for(Reservation r : reservations) {
			if(r.conflictsWith(span))
				return false;
		}
		return true;
	}

	public Set<Reservation> getConflictingReservations(Timespan span) {
		Set<Reservation> result = new HashSet<>();
		for(Reservation r : reservations)
			if(r.conflictsWith(span))
				result.add(r);
		return result;
	}

	/**
	 * Get the set of tasks that cause conflicts with the given time span.
	 * 
	 * @param 	span
	 * 	     	The time span the tasks conflict with.
	 * @return	all tasks that reserved this resource in span.
	 */
	public Set<Task> findConflictingTasks(Timespan span) {
		Set<Reservation> temp = getConflictingReservations(span);
		Set<Task> result = new HashSet<>();
		for(Reservation r : temp)
			result.add(r.getTask());
		return result;
	}

	/****************************************************
	 * Mutators                                         *
	 ****************************************************/

	/**
	 * Reserve this resource for a given task and a given time span.
	 * 
	 * @param 	task
	 *       	The task for which this resource needs to be reserved.
	 * @param 	span
	 *       	The time span to reserve this resource for.
	 * @throws 	ConflictException
	 *        	if this resource is not available during span.
	 */
	public void makeReservation(Task task, Timespan span) throws ConflictException {
		if(!isAvailable(span)) {
			Set<Task> confl = findConflictingTasks(span);
			confl.add(task);
			throw new ConflictException("This resource is not available for the given timespan.", confl);
		}
		reservations.add(new Reservation(task, span));
	}
	
	/**
	 * Clears the future reservations of the given task. If the reservation is only partially in the future,
	 * the already consumed part will be archived.
	 * 
	 * @param currentTime The current time.
	 * @param task The task of which the future reservations are cleared.
	 */
	public void clearFutureReservations(LocalDateTime currentTime, Task task)
	{
		List<Reservation> archivedReservations = new ArrayList<Reservation>();
		for(Iterator<Reservation> iterator = getReservations().iterator(); iterator.hasNext();)
		{
			Reservation reservation = iterator.next();
			if(reservation.getTask().equals(task))
			{
				if(reservation.getTimespan().getStartTime().isAfter(currentTime)) // in the future TODO reservation logica
				{
					iterator.remove();
				}
				else if (reservation.getTimespan().overlapsWith(currentTime)) // partially consumed TODO reservation logica?
				{
					iterator.remove();
					Timespan newTimeSpan = new Timespan(reservation.getTimespan().getStartTime(), currentTime); // TODO timespan logica?
					archiveReservation(new Reservation(reservation.getTask(), newTimeSpan));
				}
			}
		}
	}

	void archiveReservation(Reservation r)
	{
		previousReservations.add(r);
	}
	
	/**
	 * Archives the reservations which are in the past compared to the given current time.
	 */
	public void update(LocalDateTime currentTime)
	{
		for(Iterator<Reservation> iterator = getReservations().iterator(); iterator.hasNext();)
		{
			Reservation reservation = iterator.next();
			if(reservation.getTimespan().getEndTime().compareTo(currentTime) <= 0) // TODO logica misschien verplaatsen
			{
				iterator.remove();
				archiveReservation(reservation);
			}
		}
	}
	
	/**
	 * Removes the given reservation from this resources reservations.
	 * 
	 * @param reservation The reservation to remove.
	 * @return True if this resource contained the given reservation.
	 */
	public boolean removeReservation(Reservation reservation)
	{
		return getReservations().remove(reservation);
	}
}
