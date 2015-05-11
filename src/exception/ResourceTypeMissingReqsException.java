package exception;

import java.util.ArrayList;
import java.util.List;

import domain.ResourceType;

/**
 * This exception is thrown when a resource type has missing required resource types.
 * 
 * @author Frederic, Pieter-Jan, Mathias
 *
 */
public class ResourceTypeMissingReqsException extends RuntimeException {
	private static final long serialVersionUID = -8774594786044235672L;
	private final List<ResourceType> missingReqs;
	private final ResourceType resourceType;
	
	/**
	 * Creates a new ResourceTypeMissingReqsException describing the missing requirements of a resource type.
	 * 
	 * @param resourceType The resource type which has missing requirements.
	 * @param missingReqs The list of missing requirements.
	 */
	public ResourceTypeMissingReqsException(ResourceType resourceType, List<ResourceType> missingReqs)
	{
		super(generateMessage(resourceType, missingReqs));
		this.resourceType = resourceType;
		this.missingReqs = new ArrayList<>(missingReqs);
	}
	
	/**
	 * The list of missing requirements of the resource type belonging to this ResourceTypeMissingReqsException
	 * 
	 * @return The list of requirements which are missing.
	 */
	public List<ResourceType> getMissingRequirements()
	{
		return new ArrayList<>(this.missingReqs);
	}
	
	/**
	 * The resource type which has missing requirements.
	 * 
	 * @return The resource type which misses the requirements described in this ResourceTypeMissingReqsException.
	 */
	public ResourceType getResourceType()
	{
		return this.resourceType;
	}
	
	/**
	 * Returns an error message describing the missing requirements.
	 * 
	 * @param resourceType The resource type which has missing requirements.
	 * @param missingReqs The list of missing requirements.
	 * 
	 * @return A string describing the conflict.
	 */
	private static String generateMessage(ResourceType resourceType, List<ResourceType> missingReqs)
	{
		String missingReqsText = "Resource type '" + resourceType.getName() + "' misses requirements ";
		missingReqsText += "['";
		for(int i = 0; i < missingReqs.size(); i++)
		{
			missingReqsText += missingReqs.get(i).getName();
			if(i < missingReqs.size() - 1)
				missingReqsText += "', ";
		}
		missingReqsText += "']";
		return missingReqsText;
	}

}
