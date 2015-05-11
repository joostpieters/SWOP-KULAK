package exception;

import java.util.ArrayList;
import java.util.List;

import domain.ResourceType;

/**
 * TODO
 * @author Frederic
 *
 */
public class ResourceTypeConflictException extends RuntimeException {
	private static final long serialVersionUID = -8774594786044235672L;
	private final List<ResourceType> conflicts;
	private final ResourceType resourceType;
	public ResourceTypeConflictException(ResourceType resourceType, List<ResourceType> conflicts)
	{
		super(generateMessage(resourceType, conflicts));
		this.resourceType = resourceType;
		this.conflicts = new ArrayList<>(conflicts);
	}
	
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
