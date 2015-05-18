package domain;

/**
 * This class represents a manager to contain the projects in the system.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class BranchOffice {
	
	private final ProjectContainer projects;
	private final ResourceContainer resources;
	private final Database database;
    private String location;
	
        /**
         * Initializes this bracnhoffice with the given location
         * 
         * @param location The location where this branch office is situated
         */
        public BranchOffice(String location) {
            this(new Database());
            this.location = location;            
	}
        
	public BranchOffice() {
		this(new Database());
	}
	
	public BranchOffice(Database world) {
		this(new ProjectContainer(), new ResourceContainer(), world);
	}
	
	public BranchOffice(ProjectContainer pc, ResourceContainer rc, Database world) {
		projects = pc;
		resources = rc;
		database = world;
	}

	/**
	 * @return the projects
	 */
	public ProjectContainer getProjects() {
		return projects;
	}

	/**
	 * @return the resources
	 */
	public ResourceContainer getResources() {
		return resources;
	}

	/**
	 * @return the database
	 */
	public Database getWorld() {
		return database;
	}
        /**
         * 
         * @return The location of this branch office 
         */
    public String getLocation() {
        return location;
    }

}
