package domain;

import domain.task.Task;
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
public class Planning implements ClockObserver{
    
    private List<Resource> resources;
    private final Timespan timespan;
    private final Task task;
    
    public Planning(List<Resource> resources, Timespan timespan, Task task){
        this.resources = resources;
        this.timespan = timespan;
        this.task = task;
    }
    
    
    public void clearFutureReservations(LocalDateTime currentTime) {
        for (Resource resource : resources) {
            resource.clearFutureReservations(currentTime, task);
        }
    }

    public boolean isBefore(LocalDateTime currentTime) {
        return timespan.startsBefore(currentTime);
    }
    
    /**
     * Restores the state of this planning to the state described in the given memento.
     * 
     * @param memento The memento containing the new state of this planning.
     */
    public void setMemento(Memento memento)
    {
    	this.resources = memento.getResources();
    	for(Entry<Resource, Resource.Memento> entry : memento.getResourceMementos().entrySet())
    	{
    		entry.getKey().setMemento(entry.getValue());
    	}
    	this.task.setMemento(memento.getTaskMemento());
    }
    
    /**
     * Creates a memento of this planning.
     * 
     * @return A memento representing the state of this planning.
     */
    public Memento createMemento()
    {
    	return new Memento(this.resources, this.task);
    }
    
    /**
     * Describes a planning memento which can be used to save or load the state of a planning.
     * 
     * @author Frederic
     *
     */
    public class Memento
    {
    	private final List<Resource> resources;
    	private final Map<Resource, Resource.Memento> resourceMementos;
    	private final Task.Memento taskMemento;
    	
    	/**
    	 * @return The resources stored in this memento.
    	 */
    	private List<Resource> getResources()
    	{
    		return new ArrayList<>(this.resources);
    	}
    	
    	/**
    	 * @return A map linking the resources stored in this planning memento with resource mementos of those resources.
    	 */
    	private Map<Resource, Resource.Memento> getResourceMementos()
    	{
    		return this.resourceMementos;
    	}
    	
    	/**
    	 * @return The task memento stored in this planning memento.
    	 */
    	private Task.Memento getTaskMemento()
    	{
    		return this.taskMemento;
    	}
    	
    	/**
    	 * Creates a new memento which stores the given list of resources, their mementos and a memento of the given task.
    	 * 
    	 * @param resources The resources which need to be associated with the new planning memento.
    	 * @param task The task associated with the new planning memento.
    	 */
    	private Memento(List<Resource> resources, Task task)
    	{
    		this.resources = new ArrayList<Resource>(resources);
    		this.resourceMementos = new HashMap<>();
    		for(Resource r : resources)
    		{
    			this.resourceMementos.put(r, r.createMemento());
    		}
    		this.taskMemento = task.createMemento();
    	}
    }
     
     /**
     * Ends this planning when it is in the past and free all reserved resources
     * 
     * @param currentTime The time to compare to
     */
    @Override
    public void update(LocalDateTime currentTime) {
        if(!timespan.endsAfter(currentTime)){
            task.setPlanning(null);
            for(Resource res :resources){
                res.archiveOldReservations(currentTime);
            }
        }
    }
    
}
