package domain;

import domain.dto.DetailedBranchOffice;
import domain.user.User;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a manager to contain the projects in the system.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class BranchOffice implements DetailedBranchOffice {
	
	private final ProjectContainer projects;
	private final ResourceContainer resources;
	private final Database database;
    private String location;
    private List<User> users;
	
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
                users = new ArrayList<>();
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
    @Override
    public String getLocation() {
        return location;
    }
    
    /**
     * 
     * @return The users working in this branchoffice
     */
    public List<User> getUsers(){
        return new ArrayList<>(users);
    }
    
    /**
     * Adds a user to this branchoffice
     * 
     * @param user The user to add
     */
    public void addUser(User user){
        users.add(user);
    }

}
