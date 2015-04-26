package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a database in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Database {

    List<ResourceType> resourceTypes;
    List<User> users;
    
    /**
     * Initializes this new database
     */
    public Database(){
        resourceTypes = new ArrayList<>();
        users = new ArrayList<>();
    }
    
    /**
     * 
     * @return The resourcetypes stored in this database
     */
    public List<ResourceType> getResourceTypes(){
        return resourceTypes;
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
     * @return The users stored in this database
     */
    public List<User> getUsers(){
        return users;
    }
    
    /**
     * Adds the given user to this database
     * @param user The user to add
     */
    public void addUser(User user){
        users.add(user);
    }

}
