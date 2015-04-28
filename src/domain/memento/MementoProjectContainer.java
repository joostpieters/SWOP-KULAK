package domain.memento;

import java.util.HashMap;
import java.util.Map;

import domain.Project;

public class MementoProjectContainer {

    private final Map<Integer, Project> projects;
    
    public Map<Integer, Project> getProjects()
    {
    	return new HashMap<Integer, Project>(this.projects);
    }
    
    public MementoProjectContainer(Map<Integer,Project> projects)
    {
    	this.projects = new HashMap<Integer, Project>(projects);
    }
}
