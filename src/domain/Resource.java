package domain;

import domain.dto.DetailedResource;
import domain.task.Task;
import domain.time.Timespan;
import domain.time.WorkWeekConfiguration;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a resource that can be used to perform tasks
 *
 * @author Mathias, Pieter-Jan, Frederic
 */
public class Resource implements DetailedResource {

    private static int nextId = 0;
    private final int id;
    private final String name;
    private final ResourceType type;
    private final Set<Reservation> reservations;
    private final List<Reservation> previousReservations;
    private WorkWeekConfiguration availability;

    /**
     * Initialize a resource with a given name and clock to observe.
     *
     * @param name The name for this resource.
     * @param type The type for this resou
     * 
     */
    public Resource(String name, ResourceType type) {
        if(!canHaveAsName(name)) {
            throw new IllegalArgumentException("This resource can't have the given name as name.");
        }
        if(!canHaveAsType(type)) {
        	throw new IllegalArgumentException("This resource can't have the given type as type.");
        }
        
        this.id = generateId();
        this.name = name;
        this.type = type;
        this.reservations = new HashSet<>();
        this.previousReservations = new ArrayList<>();
               
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
    @Override
    public int getId() {
        return id;
    }

    /**
     * Checks whether this resource can have the given name as its name
     * 
     * @param name The name to check
     * @return True if and only if the given name is longer than 0 and isn't null.
     */
    public final boolean canHaveAsName(String name) {
        return name != null && name.length() > 0;
    }

    /**
     * @return The name of this resource
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Checks whether this resource can have the given type as its type
     * 
     * @param type The type to check
     * @return True if and only if the given type is not null.
     */
    public final boolean canHaveAsType(ResourceType type) {
		return type != null;
	}

    /**
	 * @return the type of this resource
	 */
    @Override
	public ResourceType getType() {
		return type;
	}

	/**
     * @return The reservations of this resource
     */
    public Set<Reservation> getReservations() {
        return new HashSet<>(reservations);
    }

    /**
     * @return The previous reservations of this resource.
     */
    public List<Reservation> getPreviousReservations() {
        return new ArrayList<>(previousReservations);
    }

    /****************************************************
     * Accessors                                        *
	 ****************************************************/
    
    /**
     * Return whether or not this resource is available during a given time
     * span.
     *
     * @param span The time span the resource should be available in.
     * @return	{@code true} if there exist no reservation of this resource with
     * a time span overlapping span.
     */
    public boolean isAvailable(Timespan span) {
        for (Reservation r : reservations) {
            if (r.conflictsWith(span)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the set of reservations that conflict with a given time span.
     *
     * @param span The time span the reservation conflicts with.
     * @return	all reservation that conflict with span.
     * @see	Reservation#conflictsWith(Timespan)
     */
    public Set<Reservation> getConflictingReservations(Timespan span) {
        Set<Reservation> result = new HashSet<>();
        for (Reservation r : reservations) {
            if (r.conflictsWith(span)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Get the set of tasks that cause conflicts with a given time span.
     *
     * @param span The time span the tasks conflict with.
     * @return	the tasks from all conflicting reservations.
     * @see	#getConflictingReservations(Timespan)
     */
    public Set<Task> findConflictingTasks(Timespan span) {
        Set<Reservation> temp = getConflictingReservations(span);
        Set<Task> result = new HashSet<>();
        for (Reservation r : temp) {
            result.add(r.getTask());
        }
        return result;
    }

    /**
     * Get all the reservations that involve a given task.
     *
     * @param	t The task to get the reservations for.
     * @return	the reservation linked to the given task.
     */
    public Reservation getReservation(Task t) {
        for (Reservation r : reservations) {
            if (r.getTask().equals(t)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Get the reservations from a given point in time.
     *
     * @param from The time stamp to start looking for reservations.
     * @return	A sorted set of reservations which are not yet finished on from.
     */
    public SortedSet<Reservation> getReservations(LocalDateTime from) {
        SortedSet<Reservation> result = new TreeSet<>(Reservation.timespanComparator());
        for (Reservation r : getReservations()) {
            if (!r.expiredBefore(from)) {
                result.add(r);
            }
        }
        return result;
    }
    
    /**
     * Checks whether this resource is available, based on its work week configuration.
     * 
     * @param time The time to check the availability on
     * @return True if and only if the given time falls inside the working hours
     * of this resource or if the resource has no work week configuration
     */
    public boolean isAvailable(LocalDateTime time) {
        // if this resource doesn't have a work week configuration of itself, use that of its type
        
        if(availability == null){
            return type.getAvailability().isValidWorkTime(time);
        }
        return availability.isValidWorkTime(time);
    }
    
	/**
	 * @return the availability of this resource if it is set, otherwise the 
	 * availability of this resource its type is returned.
	 */
    public WorkWeekConfiguration getAvailability() {
    	if(availability == null) {
    		return type.getAvailability();
        }
    	
        return availability;
    }

    /**
     *
     * @return The name of this resource
     */
    @Override
    public String toString() {
        return getName();
    }

    /****************************************************
     * Mutators                                         *
	 ****************************************************/
    
    /**
     * Reserve this resource for a given task and a given time span.
     *
     * @param task The task for which this resource needs to be reserved.
     * @param span The time span to reserve this resource for.
     * @return The newly made reservation
     * @throws ConflictException if this resource is not available during span.
     */
    public Reservation makeReservation(Task task, Timespan span) throws ConflictException {
        if (getReservation(task) != null) {
            throw new IllegalArgumentException("This resource has already been reserved for the given task.");
        }
        if (!isAvailable(span)) {
            Set<Task> confl = findConflictingTasks(span);
            throw new ConflictException("This resource is not available for the given timespan.", task, confl);
        }
        
        Reservation r = new Reservation(task, span);
        reservations.add(r);
        return r;
    }

    /**
     * Clears the future reservations of the given task. If the reservation is
     * only partially in the future, the already consumed part will be archived.
     *
     * @param currentTime The current time.
     * @param task The task of which the future reservations are cleared.
     */
    public void clearFutureReservations(LocalDateTime currentTime, Task task) {
        for (Iterator<Reservation> iterator = reservations.iterator(); iterator.hasNext();) {
            Reservation reservation = iterator.next();
            
            if (reservation.getTask().equals(task)) {
                if (reservation.getStartTime().compareTo(currentTime) >= 0) {
                    iterator.remove();
                } else if (reservation.getTimespan().overlapsWith(currentTime)) {  
                    iterator.remove();
                    Timespan newTimeSpan = new Timespan(reservation.getStartTime(), currentTime);
                    archiveReservation(new Reservation(reservation.getTask(), newTimeSpan));
                }
            }
        }
    }
    
    /**
     * Archives the given reservation
     * 
     * @param r The reservation to archive
     */
    void archiveReservation(Reservation r) {
        previousReservations.add(r);
    }

    /**
     * Archives the reservations which are in the past compared to the given
     * current time.
     * @param currentTime The time to compare to
     */
    public void archiveOldReservations(LocalDateTime currentTime) {
        for (Iterator<Reservation> iterator = getReservations().iterator(); iterator.hasNext();) {
            Reservation reservation = iterator.next();
            
            if (reservation.getTimespan().compareTo(currentTime) <= 0) {
                iterator.remove();
                archiveReservation(reservation);
            }
        }
    }
    
    /**
     * Sets the availability of this resource
     * 
     * @param availability The availability to set
     */
    public void setAvailability(WorkWeekConfiguration availability) {
        this.availability = availability;
    }

    /****************************************************
     * Memento                                          *
	 ****************************************************/

    /**
     * Creates a memento for this resource.
     *
     * @return A memento which stores the the state of this resource.
     */
    public Memento createMemento() {
        return new Memento(reservations, previousReservations);
    }

    /**
     * Sets the state of this task to the state stored inside the given memento.
     *
     * @param memento The memento containing the new state of this task.
     */
    public void setMemento(Memento memento) {
        this.reservations.clear();
        this.reservations.addAll(memento.getReservations());

        this.previousReservations.clear();
        this.previousReservations.addAll(memento.getPreviousReservations());
    }
    
    /**
     * This memento represents the internal state of this resource
     */
    public class Memento {

        private final Set<Reservation> reservations;
        private final List<Reservation> previousReservations;

        private Set<Reservation> getReservations() {
            return new HashSet<>(this.reservations);
        }

        private List<Reservation> getPreviousReservations() {
            return new ArrayList<>(this.previousReservations);
        }

        private Memento(Set<Reservation> reservations, List<Reservation> previousReservations) {
            this.reservations = new HashSet<>(reservations);
            this.previousReservations = new ArrayList<>(previousReservations);
        }
    }
}
