package domain;

import java.util.HashSet;
import java.util.Set;

import domain.task.Task;
import domain.time.Timespan;
import exception.ConflictException;

public class ResourceContainer {

    private final Set<Resource> resources;
    
    public ResourceContainer() {
        resources = new HashSet<>();
    }

    /**
     * @return the resources that are instances of this resourcetype
     */
    public Set<Resource> getResources() {
        return new HashSet<>(resources);
    }

    /**
     * Make a resource and add it to the list of resources.
     *
     * @param name The name for the new resource.
     * @param type The type of the new resource.
     */
    public Resource createResource(String name, ResourceType type) {
        Resource res = new Resource(name, type);
        resources.add(res);
        return res;
    }
    
    public Set<Resource> getResourcesOfType(ResourceType type) {
    	Set<Resource> result = new HashSet<>();
    	for(Resource res : getResources()) {
    		if(res.getType().equals(type))
    			result.add(res);
    	}
    	return result;
    }

    /**
     * Get the set of available resources of a given type on a given period of time.
     *
     * @param type The type of the resources that should be available.
     * @param span The time span the resources should be available in.
     * @return	all available resources of {@code type} at {@code span}.
     */
    public Set<Resource> getAvailableResources(Timespan span) {
        Set<Resource> result = new HashSet<>();

        for (Resource r : getResources()) {
            if (r.isAvailable(span)) {
                result.add(r);
            }
        }

        return result;
    }

    /**
     * Get the set of available resources of a given type on a given period of time.
     *
     * @param type The type of the resources that should be available.
     * @param span The time span the resources should be available in.
     * @return	all available resources of {@code type} at {@code span}.
     */
    public Set<Resource> getAvailableResources(ResourceType type, Timespan span) {
        Set<Resource> result = new HashSet<>();

        for (Resource r : getResourcesOfType(type)) {
            if (r.isAvailable(span)) {
                result.add(r);
            }
        }

        return result;
    }
    
    /**
     * Checks whether the given quantity of instances are available of this
     * resourcetype at the given timespan.
     *
     * @param span The timespan to check availability on
     * @param quantity The quantity necessary of this resourcetype
     * @return True if and only if the given quantity of instances are available
     * at the given timespan.
     */
    public boolean hasAvailableOfType(ResourceType type, Timespan span, int quantity) {
    	return (quantity < 0) ? false : getAvailableResources(type, span).size() >= quantity;
    }
    
    /**
     * Get the set of tasks that cause conflicts with the given time span.
  	 *
  	 * @param span The time span the tasks conflict with.
  	 * @return	all tasks that reserved resources of this type in span.
  	 */
    private Set<Task> findConflictingTasks(Timespan span) {
    	Set<Task> result = new HashSet<>();
    	for (Resource r : getResources()) {
    		result.addAll(r.findConflictingTasks(span));
    	}
    	return result;
    }

	/**
	 * Reserve a certain amount of resources of this type for a given task
	 * during a given time span.
	 *
	 * @param task The task to make the reservation for.
	 * @param span The time span during which it should be reserved.
	 * @param quantity The number of resources to be reserved.
	 * @return	the set of resources that got reserved.
	 * @throws ConflictException if the reservation conflicted with other tasks.
	 */
	 public Set<Resource> makeReservation(ResourceType type, Task task, Timespan span, int quantity) throws ConflictException {
	     if (quantity < 1) {
	         throw new IllegalArgumentException("At least 1 resource should be reserved.");
	     }
	     if (getResourcesOfType(type).size() < quantity) {
	         throw new IllegalArgumentException("There are less resources of this type than you want to reserve.");
	     }
	     Set<Resource> availableResources = getAvailableResources(type, span);
	     if (availableResources.size() < quantity) {
	         Set<Task> confl = findConflictingTasks(span);
	         throw new ConflictException("There are not enough resources of the right type available.", task, confl);
	     }
		      Set<Resource> result = new HashSet<>();
	     for (Resource r : availableResources) {
	         r.makeReservation(task, span);
	         result.add(r);
	         if(result.size() >= quantity)
	         	break;
	     }
	     return result;
	 }
}
