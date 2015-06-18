package domain;

import domain.dto.DetailedPlanning;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Timespan;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents a planning for a task, containing the reservations and
 * the planned timespan
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Planning implements ClockObserver, DetailedPlanning{
    
    private List<Resource> resources;
    private final Timespan timespan;
    private final Task task;
    
    /**
     * Instantiates this planning with the given resources, timespan and task.
     * 
     * @param resources The resources belonging to this planning.
     * @param timespan The timespan belonging to this planning.
     * @param task The task belonging to this planning.
     * @param clock The clock to which this planning has to be attached as a clock observer.
     */
    public Planning(List<Resource> resources, Timespan timespan, Task task, Clock clock) {
        this.resources = resources;
        this.timespan = timespan;
        this.task = task;
        clock.attach(this);
    }
    
    /**
     * Clears the future reservations of the resources belonging to this planning.
     * 
     * @param currentTime The curren time
     */
    public void clearFutureReservations(LocalDateTime currentTime) {
        for (Resource resource : resources) {
            resource.clearFutureReservations(currentTime, task);
        }
    }

    /**
     * Checks whether this planning's time span is before the given time.
     * @param currentTime The current time.
     * @return True if and only if the time span belonging to this planning starts before
     */
    public boolean isBefore(LocalDateTime currentTime) {
        return timespan.startsBefore(currentTime) || timespan.getStartTime().equals(currentTime);
    }
    
    /**
     * Restores the state of this planning to the state described in the given memento.
     * 
     * @param memento The memento containing the new state of this planning.
     */
    public void setMemento(Memento memento) {
    	this.resources = memento.getResources();
    	
    	for(Entry<Resource, Resource.Memento> entry : memento.getResourceMementos().entrySet()) {
    		entry.getKey().setMemento(entry.getValue());
    	}
    	
    	this.task.setMemento(memento.getTaskMemento());
    }
    
    /**
     * Ends this planning when it is in the past and free all reserved resources
     * 
     * @param currentTime The time to compare to
     */
    @Override
    public void update(LocalDateTime currentTime) {
        if(!timespan.endsAfter(currentTime)) {
            task.setPlanning(null);
            
            for(Resource res :resources) {
                res.archiveOldReservations(currentTime);
            }
        }
    }
   
    /**
     * Checks whether the resources belonging to this planning are available at the given time.
     * 
     * @param time The time at which to check whether all the resources belonging to this planning are available.
     * @return True if and only if all resources belonging to this planning are available at the given time.
     */
    public boolean isAvailable(LocalDateTime time) {
    	for(Resource resource : resources)
    		if(!resource.isAvailable(time))
    			return false;
    	
    	return true;
    }
    
    /**
     * @return The timespan of this planning 
     */
    @Override
    public Timespan getTimespan() {
        return timespan;
    }
    
    /**
     * @return The resources assigned to this planning 
     */
    @Override
    public List<Resource> getResources() {
        return new ArrayList<>(resources);
    }  
    
    /**
     * Creates a memento of this planning.
     * 
     * @return A memento representing the state of this planning.
     */
    public Memento createMemento() {
    	return new Memento(this.resources, this.task);
    }
    
    /**
     * Describes a planning memento which can be used to save or load the state of a planning.
     * 
     * @author Frederic
     *
     */
    public class Memento {
    	
    	private final List<Resource> resources;
    	private final Map<Resource, Resource.Memento> resourceMementos;
    	private final Task.Memento taskMemento;
    	
    	/**
    	 * @return The resources stored in this memento.
    	 */
    	private List<Resource> getResources() {
    		return new ArrayList<>(this.resources);
    	}
    	
    	/**
    	 * @return A map linking the resources stored in this planning memento with resource mementos of those resources.
    	 */
    	private Map<Resource, Resource.Memento> getResourceMementos() {
    		return this.resourceMementos;
    	}
    	
    	/**
    	 * @return The task memento stored in this planning memento.
    	 */
    	private Task.Memento getTaskMemento() {
    		return this.taskMemento;
    	}
    	
    	/**
    	 * Creates a new memento which stores the given list of resources, their mementos and a memento of the given task.
    	 * 
    	 * @param resources The resources which need to be associated with the new planning memento.
    	 * @param task The task associated with the new planning memento.
    	 */
    	private Memento(List<Resource> resources, Task task) {
    		this.resources = new ArrayList<>(resources);
    		this.resourceMementos = new HashMap<>();
    		
    		for(Resource r : resources) {
    			this.resourceMementos.put(r, r.createMemento());
    		}
    		
    		this.taskMemento = task.createMemento();
    	}
    }
}
