package domain;

import java.util.HashSet;
import java.util.Set;

import exception.ConflictException;

public class ResourceType {

	private final ResourceType[] requirements;
	private final ResourceType[] conflicts;
	private final Timespan availability;
	private final Set<Resource> resources;
	
	public ResourceType() {
		requirements = new ResourceType[0];
		conflicts = new ResourceType[0];
		availability = null;
		resources = new HashSet<Resource>();
	}

	/**
	 * @return the requirements
	 */
	public ResourceType[] getRequirements() {
		return requirements;
	}

	/**
	 * @return the conflicts
	 */
	public ResourceType[] getConflicts() {
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
	 * @param 	resource
	 *       	The resource to be added.
	 */
	public void addResource(Resource resource) {
		resources.add(resource);
	}
	
	/**
	 * Get the set of available resources of this type.
	 * 
	 * @param 	span
	 *       	The time span the resources should be available in.
	 * @return	all available resources of this type at the given time span.
	 */
	public Set<Resource> getAvailableResources(Timespan span) {
		Set<Resource> result = new HashSet<>();
		for(Resource r : resources)
			if(r.isAvailable(span))
				result.add(r);
		return result;	
	}

	/**
	 * Get the set of tasks that cause conflicts with the given time span.
	 * 
	 * @param 	span
	 * 	     	The time span the tasks conflict with.
	 * @return	all tasks that reserved resources of this type in span.
	 */
	public Set<Task> findConflictingTasks(Timespan span) {
		Set<Task> result = new HashSet<>();
		for(Resource r : resources)
			result.addAll(r.findConflictingTasks(span));
		
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
		if(availableResources.size() < quantity) {
			Set<Task> confl = findConflictingTasks(span);
			confl.add(task);
			throw new ConflictException("There are not enough resources of the right type available.", confl);
		}
		
		Set<Resource> result = new HashSet<>();
		while(result.size() < quantity)
			for(Resource r: availableResources) {
				r.makeReservation(task, span);
				result.add(r);
			}
		return result;
	}
	
}
