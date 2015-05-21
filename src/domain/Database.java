package domain;

import domain.user.User;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a database in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Database {

    private final List<ResourceType> resourceTypes;
    private final List<BranchOffice> offices;
    
    
    /**
     * Initializes this new database
     */
    public Database(){
        resourceTypes = new ArrayList<>();
        offices = new ArrayList<>();
    }
    
    /**
     * 
     * @return The resourcetypes stored in this database
     */
    public List<ResourceType> getResourceTypes(){
        return new ArrayList<>(resourceTypes);
    }
    
    /**
     * Adds the given resourcetype to this database
     * @param type The resourcetype to add
     */
    public void addResourceType(ResourceType type){
        resourceTypes.add(type);
    }
    
    /**
     * 
     * @return The users from all bracnhoffices in this database
     */
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        for(BranchOffice office : offices){
            users.addAll(office.getUsers());
        }
        
        return users;
    }
    
   
     /**
     * 
     * @return The users stored in this database
     */
    public List<BranchOffice> getOffices(){
        return new ArrayList<>(offices);
    }
    
    /**
     * Adds the given user to this database
     * @param office The office to add
     */
    public void addOffice(BranchOffice office){
        offices.add(office);
    }
    
    /**
     * 
     * @return All the projects from all the branchoffices in this database
     */
    public List<Project> getProjects(){
        List<Project> projects = new ArrayList<>();
        for(BranchOffice office : offices){
            projects.addAll(office.getProjectContainer().getProjects());
        }
        
        return projects;
    }

}
