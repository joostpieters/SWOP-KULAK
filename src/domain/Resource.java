package domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import exception.ConflictException;

public class Resource {
	
	private static int nextId = 0;

	//TODO: wordt het niet stilletjesaan interessant om een id-klasse te maken?
	private final int id;
	private final String name;
	private final Set<Reservation> reservations;
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	public Resource(String name, ResourceType type) {
		this.id = generateId();
		this.name = name;
		this.reservations = new HashSet<Reservation>();
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
	
	public SortedSet<Reservation> getReservations(LocalDateTime from) {
		SortedSet<Reservation> result = new TreeSet<>(Reservation.timespanComparator());
		for(Reservation r : reservations) {
			if(!r.expiredBefore(from))
				result.add(r);
		}
		return result;
	}
	
	public SortedSet<Timespan> nextAvailableTimespans(LocalDateTime from) {
		SortedSet<Timespan> result = new TreeSet<>();
		SortedSet<Reservation> reservations = getReservations(from);
		if(reservations.isEmpty()) {
			result.add(new Timespan(from));
			return result;
		}
		
		Iterator<Reservation> it = reservations.iterator();
		Reservation r1, r2 = it.next();
		
		while(it.hasNext()) {
			r1 = r2;
			r2 = it.next();
			if(!r1.conflictsWith(r2))
				result.add(new Timespan(r1.getEndTime(), r2.getStartTime()));
		}
		
		result.add(new Timespan(r2.getEndTime()));
		return result;
//		SortedSet<LocalDateTime> result = new TreeSet<>();
//		Set<Reservation> reservations = getReservations(from);
//		if(reservations.isEmpty()) {
//			result.add(from);
//			return result;
//		}
//		
//		Iterator<Reservation> it = reservations.iterator();
//		Reservation r1, r2 = it.next();
//		while(it.hasNext()) {
//			r1 = r2;
//			r2 = it.next();
//			if(r1.timeBetween(r2).compareTo(dur) > 1) {
//				LocalDateTime next = r1.getEndTime();
//				result.add(next);
//				next = LocalDateTime.of(next.toLocalDate(), next.toLocalTime().minusMinutes(next.getMinute()));
//				while(dur.getEndTimeFrom(next).isBefore(r2.getStartTime())) {
//					result.add(next);
//					next = next.plusHours(1);
//				}
//			}
//		}
//		return result;
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
	public Reservation makeReservation(Task task, Timespan span) throws ConflictException {
		if(!isAvailable(span)) {
			Set<Task> confl = findConflictingTasks(span);
			confl.add(task);
			throw new ConflictException("This resource is not available for the given timespan.", confl);
		}
		Reservation r = new Reservation(task, span);
		reservations.add(r);
		return r;
	}
	
}
