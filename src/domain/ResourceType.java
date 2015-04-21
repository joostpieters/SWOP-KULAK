package domain;

import exception.ConflictException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        return resources;
    }

    /**
     * Add a resource to the list of resources of this type.
     *
     * @param resource The resource to be added.
     */
    public void addResource(Resource resource) {
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
        for (Resource r : resources) {
            if (r.isAvailable(span)) {
                result.add(r);
            }
        }
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
        for (Resource r : resources) {
            result.addAll(r.findConflictingTasks(span));
        }

        return result;
    }

//	public Set<Resource> makeReservation(Resource resource, Task task, Timespan span) throws ConflictException {
//		if(!resources.contains(resource))
//			throw new IllegalArgumentException("The given resource is not of the right type.");
//		resource.makeReservation(task, span);
//		//TODO: wat te doen met de return types???
//		Set<Resource> result = new HashSet<>();
//		result.add(resource);
//		return result;
//	}
    public Set<Resource> makeReservation(Task task, Timespan span, int quantity) throws ConflictException {
        Set<Resource> availableResources = getAvailableResources(span);
        if (availableResources.size() < quantity) {
            Set<Task> confl = findConflictingTasks(span);
            confl.add(task);
            throw new ConflictException("There are not enough resources of the right type available.", confl);
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
    
    //TODO
    public static boolean isValidCombination(Map<ResourceType, Integer> resources) {
        for (ResourceType type : resources.keySet()) {
            if(!type.canHaveAsCombination(resources))
                return false;
        }
        
        return true;
    }
    
    //TODO
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

}
