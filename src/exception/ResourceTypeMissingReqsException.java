package exception;

import java.util.ArrayList;
import java.util.List;

import domain.ResourceType;

/**
 * TODO
 * @author Frederic
 *
 */
public class ResourceTypeMissingReqsException extends RuntimeException {
	private static final long serialVersionUID = -8774594786044235672L;
	private final List<ResourceType> missingReqs;
	private final ResourceType resourceType;
	public ResourceTypeMissingReqsException(ResourceType resourceType, List<ResourceType> missingReqs)
	{
		super(generateMessage(resourceType, missingReqs));
		this.resourceType = resourceType;
		this.missingReqs = new ArrayList<>(missingReqs);
	}
	
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
