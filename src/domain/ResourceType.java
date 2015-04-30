package domain;

import domain.dto.DetailedResourceType;
import domain.time.Timespan;
import domain.time.WorkWeekConfiguration;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a common type of resources
 *
 * @author Mathias, Pieter-Jan, Frederic
 */
public class ResourceType implements DetailedResourceType {

    private final String name;
    private final List<ResourceType> requirements;
    private final List<ResourceType> conflicts;
    private final WorkWeekConfiguration availability;
    private final Set<Resource> resources;

    /**
     * Initialize a resource type with given name, required and conflicting
     * types and a daily availability.
     *
     * @param name The name for this type.
     * @param requirements The required types for this type.
     * @param conflicts The conflicting types for this type.
     * @param availability The daily availability for this type.
     */
    public ResourceType(String name, List<ResourceType> requirements, List<ResourceType> conflicts, WorkWeekConfiguration availability) {
        if (!canHaveAsName(name)) {
            throw new IllegalArgumentException("The given name was incorrect.");
        }
        if (!canHaveAsRequirementsAndConflicts(requirements, conflicts)) {
            throw new IllegalArgumentException("The given sets of requirements and conflicts are invalid.");
        }
        if (!canHaveAsAvailability(availability)) {
            throw new IllegalArgumentException("The given availability is invalid.");
        }

        this.name = name;
        this.requirements = requirements;
        this.conflicts = conflicts;
        this.availability = availability;
        resources = new HashSet<>();
    }

    /**
     * Initialize a resource type with given name, requirements and conflicting
     * types.
     *
     * @param name The name for this type.
     * @param requirements The required types for this type.
     * @param conflicts The conflicting types for this type.
     */
    public ResourceType(String name, List<ResourceType> requirements, List<ResourceType> conflicts) {
        this(name, requirements, conflicts, WorkWeekConfiguration.ALWAYS);
    }

    /**
     * Initialize a resource type with given name.
     *
     * @param name The name for this type.
     */
    public ResourceType(String name) {
        this(name, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * **************************************************
     * Getters & Setters *
	 ***************************************************
     */
    /**
     * @return the name of this resourcetype
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Check whether a given name can be the name of this type.
     *
     * @param name The name to be checked.
     * @return	{@code true} if this name consists of at least 1 character.
     */
    public final boolean canHaveAsName(String name) {
        return name != null && name.length() > 0;
    }

    /**
     * @return the requirements of this resourcetype
     */
    public List<ResourceType> getRequirements() {
        return new ArrayList<>(requirements);
    }

    /**
     * Check whether a given set of resources can have the given requirements
     * and conflicts.
     *
     * @param requirements The set of requirements to be checked.
     * @param conflicts The set of conflicts to be checked.
     * @return	{@code true} if both are not null and there is no element in the
     * intersection.
     */
    public final boolean canHaveAsRequirementsAndConflicts(List<ResourceType> requirements, List<ResourceType> conflicts) {
        List<ResourceType> intersection = new ArrayList<>(requirements);
        intersection.retainAll(conflicts);
        return requirements != null && conflicts != null && intersection.isEmpty();
    }

    /**
     * @return the conflicts of this resourcetype
     */
    public List<ResourceType> getConflicts() {
        return new ArrayList<>(conflicts);
    }

    /**
     * @return the availability of this resourcetype
     */
    public WorkWeekConfiguration getAvailability() {
        return availability;
    }

    /**
     * Check whether the availability
     *
     * @param availability The availability to check.
     * @return	{@code true} if the given timespan is finite.
     */
    public final boolean canHaveAsAvailability(WorkWeekConfiguration availability) {
        return availability != null;
    }

    /**
     * @return the resources that are instances of this resourcetype
     */
    @Override
    public Set<Resource> getResources() {
        return new HashSet<>(resources);
    }

    /**
     * Check whether a given resource can be added to this type.
     *
     * @param resource The resource to be checked.
     * @return	{@code true} if not null.
     */
    public boolean canHaveAsResource(Resource resource) {
        return resource != null;
    }

    /**
     * Add a resource to the list of resources of this type.
     *
     * @param resource The resource to be added.
     */
    public void addResource(Resource resource) {
        if (!canHaveAsResource(resource)) {
            throw new IllegalArgumentException("The given resource is not of the right type.");
        }

        resources.add(resource);
    }

    /**
     * **************************************************
     * Others *
	 ***************************************************
     */
    /**
     * Get the set of available resources of this type.
     *
     * @param span The time span the resources should be available in.
     * @return	all available resources of this type at the given time span.
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
     * Checks whether the given quantity of instances are available of this
     * resourcetype at the given timespan.
     *
     * @param span The timespan to check availability on
     * @param quantity The quantity necessary of this resourcetype
     * @return True if and only if the given quantity of instances are available
     * at the given timespan.
     */
    public boolean hasAvailableResources(Timespan span, int quantity) {
        return (quantity < 0) ? false : getAvailableResources(span).size() >= quantity;
    }

    /**
     * Get the set of tasks that cause conflicts with the given time span.
     *
     * @param span The time span the tasks conflict with.
     * @return	all tasks that reserved resources of this type in span.
     */
    public Set<Task> findConflictingTasks(Timespan span) {
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
    public Set<Resource> makeReservation(Task task, Timespan span, int quantity) throws ConflictException {
        if (quantity < 1) {
            throw new IllegalArgumentException("At least 1 resource should be reserved.");
        }
        if (getResources().size() < quantity) {
            throw new IllegalArgumentException("There are less resources of this type than you want to reserve.");
        }
        Set<Resource> availableResources = getAvailableResources(span);
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

    /**
     * Get the time spans the resources of this type are available from a
     * certain point in time.
     *
     * @param from The time stamp to start looking for free time spans.
     * @return	a sorted set of time spans in which the resources of this type
     * are free. This set contains as many infinite time spans as there are
     * resources.
     * @see Resource#nextAvailableTimespans(LocalDateTime)
     */
    public SortedSet<Timespan> nextAvailableTimespans(LocalDateTime from) {
        SortedSet<Timespan> result = new TreeSet<>();

        for (Resource r : getResources()) {
            result.addAll(r.nextAvailableTimespans(from));
        }

        return result;
    }

    /**
     * Checks whether the given list of resourcetypes is compatible with this
     * resourcetype
     *
     * @param resources The resourcetype to check, including this resourcetype
     * @return True if and only if this resource does not conflict with any of
     * the given resourcetypes and that all the resourcetypes that this
     * resourcetype requires are included in the given list.
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

    public void clearFutureReservations(LocalDateTime currentTime, Task task) {
        for (Resource resource : getResources()) {
            resource.clearFutureReservations(currentTime, task);
        }
    }
    
    /**
     * Returns the number of resources of this type the given list contains.
     * 
     * @param resources A list of resources to check
     * @return The number of resources, that are instances of this type 
     * that are in the given list.
     */
    public int numberOfResources(List<Resource> resources) {
        int result = 0;
        for (Resource r : resources) {
            if (getResources().contains(r)) {
                result++;
            }
        }
        return result;
    }

}
