package exception;

import java.util.ArrayList;
import java.util.List;

import domain.ResourceType;

/**
 * This exception is thrown when there is a conflict with a resource type and other resource types.
 * 
 * @author Frederic, Pieter-Jan, Mathias
 *
 */
public class ResourceTypeConflictException extends RuntimeException {
	private static final long serialVersionUID = -8774594786044235672L;
	private final List<ResourceType> conflicts;
	private final ResourceType resourceType;
	
	/**
	 * Creates a ResourceTypeConflictException which describes a conflict between a resource type and other resource types.
	 * 
	 * @param resourceType The resource type which has conflicts with the list of conflicting resource types.
	 * @param conflicts The list of conflicting resource types.
	 */
	public ResourceTypeConflictException(ResourceType resourceType, List<ResourceType> conflicts)
	{
		super(generateMessage(resourceType, conflicts));
		this.resourceType = resourceType;
		this.conflicts = new ArrayList<>(conflicts);
	}
	
	/**
	 * Returns the conflicting resource types.
	 * 
	 * @return A list of resource types which are in the conflict list of the
	 *         resource type belonging to this ResourceTypeConflictException.
	 */
	public List<ResourceType> getConflictingResourceTypes()
	{
		return new ArrayList<>(this.conflicts);
	}
	
	/**
	 * Returns the resource type which has a conflict with the list of conflicting resource types.
	 * 
	 * @return The resource type of this ResourceTypeConflictException which conflicts with the
	 *         list of conflicting resource types.
	 */
	public ResourceType getResourceType()
	{
		return this.resourceType;
	}
	
	/**
	 * Returns an error message describing the conflict.
	 * 
	 * @param resourceType The resource type which has conflicts with the list of conflicting resource types.
	 * @param conflicts The list of conflicting resource types.
	 * 
	 * @return A string describing the conflict.
	 */
	private static String generateMessage(ResourceType resourceType, List<ResourceType> conflicts)
	{
		String conflictsText = "Resource type '" + resourceType.getName() + "' conflicts with ";
		conflictsText += "['";
		for(int i = 0; i < conflicts.size(); i++)
		{
			conflictsText += conflicts.get(i).getName();
			if(i < conflicts.size() - 1)
				conflictsText += "', ";
		}
		conflictsText += "']";
		return conflictsText;
	}

}
