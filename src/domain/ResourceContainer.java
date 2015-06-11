package domain;

import domain.task.Task;
import domain.time.Timespan;
import exception.ConflictException;
import exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class provides a container to hold resources.
 * It also provides actions to perform on the resources.
 * 
 * @author Mathias
 */
public class ResourceContainer {

    private final Set<Resource> resources;
    
    /**
     * Initializes this resourcecontainer
     */
    public ResourceContainer() {
        resources = new HashSet<>();
    }

    /**
     * @return the resources that are instances of this resource type
     */
    public Set<Resource> getResources() {
        return new HashSet<>(resources);
    }
    
    /**
     * Return a resource based on its id.
     * 
     * @param id The id of the resource looking for.
     * @return the resource with the given id.
     * @throws ObjectNotFoundException if there is no such resource with the given id.
     */
    public Resource getResource(int id) throws ObjectNotFoundException {
    	for(Resource r : getResources())
    		if(r.getId() == id)
    			return r;
    	throw new ObjectNotFoundException("The resource with id " + id + " does not exist in this container");
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
    
    /**
     * Returns all resources in this resourcecontainer of the given type.
     * 
     * @param type The type of the resources to retrieve
     * @return A subset of resources of this resourcecontainer with the given type.
     */
    public Set<Resource> getResourcesOfType(ResourceType type) {
    	Set<Resource> result = new HashSet<>();
    	for(Resource res : getResources()) {
    		if(res.getType().equals(type))
    			result.add(res);
    	}
    	return result;
    }

    /**
     * Get the set of available resources on a given period of time.
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
	 * Return a list of resources meeting the requirements of a given task at a given time span.
	 * The given specific resource-id's defines which resources should be in the resulting list.
	 * 
	 * @param task The task to meet the requirements
	 * @param span The time span during which the requirements need to be met
	 * @param specificIds The list of id's of resources that should be in the resulting set
	 * @return a list of resources containing every resource from {@code specificIds} 
	 * which are available during {@code span} and meet the requirements for {@code task}
	 * @throws ConflictException if there are not enough available resources of a certain type or if one of the specific resources is not available
	 * @throws ObjectNotFoundException if one of the given id's does not represent a resource in this container
	 * @throws IllegalArgumentException if there are more specific resources of a type than required
	 */
	public List<Resource> meetRequirements(Task task, Timespan span, List<Integer> specificIds) 
			throws ConflictException, ObjectNotFoundException, IllegalArgumentException {
		List<Resource> resources = new ArrayList<>();
		Map<ResourceType, Integer> requirement = task.getRequiredResources();
		List<Resource> specific = getAvailableResourcesFromIds(specificIds, span);
		if(specific.size() < specificIds.size())
			throw new ConflictException("One of the resources in the specific resources are not available at the given time span", 
					task, findConflictingTasks(span));
		
		for(ResourceType type : requirement.keySet()) {
			Set<Resource> availableResources = getNrOfAvailableResources(type, requirement.get(type), span, specific);
        	if(availableResources.size() < requirement.get(type))
        		if(getResourcesOfType(type).size() < requirement.get(type))
        			throw new IllegalStateException("The requirements of the task can not be met");
        		else
        			throw new ConflictException("There are not enough resource available at the given time span", 
        					task, findConflictingTasks(span));
        	else
        		resources.addAll(availableResources);
		}
		
        return resources;
	}
    
	/**
	 * Get a list of available resources from a given list of IDs during a given span.
	 * 
	 * @param specificResources The list with resource-id's
	 * @param span The span in which the resources need to be available
	 * @return a list with for every id in {@code specificResources} an actual resource with that id if that resource is available.
	 * @throws ObjectNotFoundException if one of the given id's does not represent a resource in this container
	 */
    private List<Resource> getAvailableResourcesFromIds(List<Integer> specificResources, Timespan span) throws ObjectNotFoundException {
    	List<Resource> result = new ArrayList<>();
		for(int id : specificResources) {
			Resource resource = getResource(id);
			if(resource.isAvailable(span))
				result.add(resource);
		}
		return result;
	}

	/**
     * Returns a set of resources that is available at the given time span of the given type.
     * The given specific resources will be in the resulting set.
     * 
     * @param type The type of the resources
     * @param quantity The necessary quantity
     * @param span The span to check the availability at
     * @param specificResources The resources which should surely be in this set.
     * @return a set with {@code quantity} elements of type {@code type} which are available in {@code span} 
     * containing all resources from {@code specificResources}
     * @throws IllegalArgumentException if the specificResources contains more resources of a type than quantity.
     */
    private Set<Resource> getNrOfAvailableResources(ResourceType type, int quantity, Timespan span, List<Resource> specificResources) throws IllegalArgumentException {
    	Set<Resource> result = new HashSet<>();
    	for(Resource r : specificResources) {
     		if(result.size() >= quantity)
    			throw new IllegalArgumentException("There were too many resources of type " + type.getName() + " in the specific resources list.");
     		if(r.getType().equals(type))
     			result.add(r);
    	}
    	for(Resource r : getAvailableResources(type, span)) {
    		if(result.size() >= quantity)
    			break;
    		result.add(r);
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
  	 * @return all tasks that reserved resources of this type in span.
  	 */
    private Set<Task> findConflictingTasks(Timespan span) {
    	Set<Task> result = new HashSet<>();
    	for (Resource r : getResources()) {
    		result.addAll(r.findConflictingTasks(span));
    	}
    	return result;
    }
}
