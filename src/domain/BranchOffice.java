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
	
    private String location;
    private List<User> users;
	
        /**
         * Initializes this bracnhoffice with the given location
         * 
         * @param location The location where this branch office is situated
         */
        public BranchOffice(String location) {
            this();
            this.location = location;            
	}
        
		
	public BranchOffice() {
		this(new ProjectContainer(), new ResourceContainer());
	}
	
	public BranchOffice(ProjectContainer pc, ResourceContainer rc) {
		projects = pc;
		resources = rc;
		
                users = new ArrayList<>();
	}

	/**
	 * @return the project container
	 */
	public ProjectContainer getProjectContainer() {
		return projects;
	}

	/**
	 * @return the resource container
	 */
	public ResourceContainer getResourceContainer() {
		return resources;
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
