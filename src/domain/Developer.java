package domain;

import domain.time.Clock;
import domain.time.WorkWeekConfiguration;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Developer extends Resource implements User {
    
    private String name;
    private WorkWeekConfiguration conf;
    private final String role;
    
    /**
     * Initializes a new developer with the given name and role
     * 
     * @param name The name of the developer
     * @param clock The clock this developer should observe
     */
    public Developer(String name, Clock clock) {
        super(name, clock);
        this.name = name;
        this.role = "developer";
        // TODO wss moven naar resource
        conf = new WorkWeekConfiguration();
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
    
       
    /**
     * 
     * @return The workweek configuration of this user 
     */
    public WorkWeekConfiguration getWorkWeekConfiguration(){
        return conf;
    }
    
    /**
     * Sets the wordkweek configuration of this user to the given configuration.
     * 
     * @param conf The configuration to set
     */
    public void setWorkWeekConfiguration(WorkWeekConfiguration conf){
        this.conf = conf;
    }  
}
