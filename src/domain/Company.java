package domain;

import domain.user.User;
import exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a company in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Company {

    private final List<ResourceType> resourceTypes;
    private final List<BranchOffice> offices;
    
    
    /**
     * Initializes this new company
     */
    public Company(){
        resourceTypes = new ArrayList<>();
        offices = new ArrayList<>();
    }
    
    /**
     * 
     * @return The resource types stored in this company
     */
    public List<ResourceType> getResourceTypes(){
        return new ArrayList<>(resourceTypes);
    }
    
    /**
     * Adds the given resource type to this company
     * @param type The resource type to add
     */
    public void addResourceType(ResourceType type){
        resourceTypes.add(type);
    }
    
    /**
     * 
     * @return The users from all bracnh offices in this company
     */
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        
        for(BranchOffice office : offices){
            users.addAll(office.getUsers());
        }
        
        return users;
    }
    
   
    /**
     * @return The users stored in this company
     */
    public List<BranchOffice> getOffices(){
        return new ArrayList<>(offices);
    }
    
    /**
     * Adds the given user to this company
     * @param office The office to add
     */
    public void addOffice(BranchOffice office){
        offices.add(office);
    }
    
    //TODO: toegelaten? (zie onder)
    
    /**
     * @return all the projects from all branch offices in this company
     */
    public List<Project> getProjects(){
        List<Project> projects = new ArrayList<>();
        
        for(BranchOffice office : offices){
            projects.addAll(office.getProjects());
        }
        
        return projects;
    }
    
    /**
     * Get the project with a given id.
     * 
     * @param id The id of the project to look for
     * @return the project with an id equal to the given one or null if it does not exist
     */
    public Project getProject(int id) {
    	Project result = null;
    	for(BranchOffice office : offices) {
    		try {
    			result = office.getProject(id);
    			break;
    		} catch(ObjectNotFoundException onfe) { }
    	}
    	return result;
    }

}
