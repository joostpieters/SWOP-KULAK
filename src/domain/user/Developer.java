package domain.user;

import domain.Resource;
import domain.ResourceType;
import domain.time.Clock;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Developer extends Resource implements User {
    
    private String name;
    private final String role;
    
    /**
     * Initializes a new developer with the given name and role
     * 
     * @param name The name of the developer
     * @param clock The clock this developer should observe
     */
    public Developer(String name, Clock clock) {
        super(name, ResourceType.DEVELOPER);
        this.name = name;
        this.role = "developer";
     
    }
    
    /**
     * 
     * @return The name of this user 
     */
     @Override
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this user
     * 
     * @param name The name to set 
     */
     @Override
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return The role of this user 
     */
     @Override
    public String getRole() {
        return role;
    }
}
