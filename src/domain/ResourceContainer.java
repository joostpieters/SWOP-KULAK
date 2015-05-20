package domain;

import domain.task.Task;
import domain.time.Timespan;
import java.util.HashSet;
import java.util.Set;

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
     * Return a resource based on its id.
     * 
     * @param id The id of the resource looking for.
     * @return the resource with the given id or null if it is not in this container.
     */
    public Resource getResource(int id) {
    	for(Resource r : getResources())
    		if(r.getId() == id)
    			return r;
    	return null;
    }

    /**
     * Make a resource and add it to the list of resources.
     *
     * @param name The name for the new resource.
     * @param type The type of the new resource.
     * @return The created resource
     */
    public Resource createResource(String name, ResourceType type) {
        Resource res = new Resource(name, type);
        addResource(res);
        return res;
    }
    
    /**
     * Adds the given resource to this container
     * 
     * @param res The resource to add
     */
    public void addResource(Resource res) {
        resources.add(res);
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
     * @param type The resource type to check the availability of
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
}
