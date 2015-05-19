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
    private final List<User> users;
    private final List<Resource> resources;
    private final List<BranchOffice> offices;
    
    /**
     * Initializes this new database
     */
    public Database(){
        resources = new ArrayList<>();
        resourceTypes = new ArrayList<>();
        users = new ArrayList<>();
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
     * @return The resources stored in this database
     */
    public List<Resource> getResources(){
        return new ArrayList<>(resources);
    }
    
    /**
     * Adds the given resource to this database
     * @param resource The resource to add
     */
    public void addResource(Resource resource){
        resources.add(resource);
    }
    
    /**
     * 
     * @return The users stored in this database
     */
    public List<User> getUsers(){
        return new ArrayList<>(users);
    }
    
    /**
     * Adds the given user to this database
     * @param user The user to add
     */
    public void addUser(User user){
        users.add(user);
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
