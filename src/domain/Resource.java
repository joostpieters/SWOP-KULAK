package domain;

import exception.ConflictException;
import java.util.HashSet;
import java.util.Set;

public class Resource {
	
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
	
	public void update(LocalDateTime currentTime)
	{
		
	}
}
