package domain;

import exception.ConflictException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import time.Timespan;

public class ResourceType {

    private final List<ResourceType> requirements;
    private final List<ResourceType> conflicts;
    private final Timespan availability;
    private final Set<Resource> resources;

    public ResourceType() {
        requirements = new ArrayList<>();
        conflicts = new ArrayList<>();
        availability = null;
        resources = new HashSet<>();
    }

    /**
     * @return the requirements
     */
    public List<ResourceType> getRequirements() {
        return requirements;
    }

    /**
     * @return the conflicts
     */
    public List<ResourceType> getConflicts() {
        return conflicts;
    }

    /**
     * @return the availability
     */
    public Timespan getAvailability() {
        return availability;
    }

    /**
     * @return the resources
     */
    public Set<Resource> getResources() {
        return new HashSet<>(resources);
    }

    public boolean canHaveAsResource(Resource resource) {
		return resource != null;
	}

    /**
     * Add a resource to the list of resources of this type.
     *
     * @param resource The resource to be added.
     */
    protected void addResource(Resource resource) {
    	if(!canHaveAsResource(resource))
    		throw new IllegalArgumentException("The given resource is not of the right type.");
    	
        resources.add(resource);
    }

	/**
     * Get the set of available resources of this type.
     *
     * @param span The time span the resources should be available in.
     * @return	all available resources of this type at the given time span.
     */
    public Set<Resource> getAvailableResources(Timespan span) {
        Set<Resource> result = new HashSet<>();
        
        for (Resource r : getResources())
            if (r.isAvailable(span))
                result.add(r);
        
        return result;
    }

    /**
     * Get the set of tasks that cause conflicts with the given time span.
     *
     * @param span The time span the tasks conflict with.
     * @return	all tasks that reserved resources of this type in span.
     */
    public Set<Task> findConflictingTasks(Timespan span) {
        Set<Task> result = new HashSet<>();
        
        for (Resource r : getResources())
            result.addAll(r.findConflictingTasks(span));

        return result;
    }
    
    public Set<Resource> makeReservation(Task task, Timespan span, int quantity) throws ConflictException {
        Set<Resource> availableResources = getAvailableResources(span);
        if (availableResources.size() < quantity) {
            Set<Task> confl = findConflictingTasks(span);
            throw new ConflictException("There are not enough resources of the right type available.", task, confl);
        }

        Set<Resource> result = new HashSet<>();
        while (result.size() < quantity) {
            for (Resource r : availableResources) {
                r.makeReservation(task, span);
                result.add(r);
            }
        }
        return result;
    }
    
    public SortedSet<Timespan> nextAvailableTimespans(LocalDateTime from) {
    	SortedSet<Timespan> result = new TreeSet<>();
    	
    	for(Resource r : getResources()) {
    		result.addAll(r.nextAvailableTimespans(from));
    	}
    	
    	return result;
    }
    
    /**
     * Check whether the given list of resource types is mutually compatible
     * 
     * @param 	resources 
     *       	The resource to check
     * @return 	{@code true} if and only if all the given resource types don't mutually 
     *        	conflict and for each resource type, its requirements are included in
     *        	the given list. 
     */
    public static boolean isValidCombination(Map<ResourceType, Integer> resources) {
        if(resources == null){
            return true;
        }
        for (ResourceType type : resources.keySet()) {
            if(!type.canHaveAsCombination(resources))
                return false;
        }
        return true;
    }
    
    /**
     * Checks whether the given list of resourcetypes is compatible with this 
     * resourcetype
     * 
     * @param resources The resourcetype to check, including this resourcetype
     * @return True if and only if this resource does not conflict with any of
     * the given resourcetypes and that all the resourcetypes that this resourcetype
     * requires are included in the given list.
     */
    public boolean canHaveAsCombination(Map<ResourceType, Integer> resources) {
        for (ResourceType resource : resources.keySet()) {
            if (conflicts.contains(resource)) {
                if (resource == this && resources.get(resource) > 1) {
                    return false;
                } else if (resource == this && resources.get(resource) == 1) {
                    
                } else {
                    return false;
                }
            }
        }
        for (ResourceType required : requirements) {
            if (!resources.keySet().contains(required)) {
                return false;
            }
        }

        return true;

    }
    //TODO
    public void clearFutureReservations(LocalDateTime currentTime, Task task)
    {
    	for(Resource resource : getResources())
    	{
    		resource.clearFutureReservations(currentTime, task);
    	}
    }

}
