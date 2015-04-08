package domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  This class represents a user of the system
 * 
 *  @author Mathias, Frederic, Pieter-Jan
 */
public class User {
    private String name;
    private String role;
    private WorkWeekConfiguration conf;
    private final List<Task> tasks;
    

    /**
     * Initialize a new user with the given name and role and a standard workweek
     * configuration
     * 
     * @param name The name of the user
     * @param role The role of the user
     */    
    public User(String name, String role){
        this.name = name;
        this.role = role;
        conf = new WorkWeekConfiguration();
        tasks = new ArrayList<>();
    }
    
    /**
     * 
     * @return The name of this user 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this user
     * 
     * @param name The name to set 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return The role of this user 
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Sets the role of this user
     * 
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
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
    
    /**
     * Assigns the given task to this user
     * 
     * @param task The task to assign
     */
    public void assignTask(Task task){
        tasks.add(task);
    }
    
    /**
     * Checks whether this user is available at the given time
     * 
     * @param time The time to check on which te user is available
     * @return True if and only if the user is not assigned to a task that is 
     * executing at the given time.
     */
    public boolean isAvailable(LocalTime time){
        for(Task task : tasks){
            //TODO task logic necessary
        }
        return false;
    }
    
    /**
     * Checks whether this user is available during the full given timespan
     * 
     * @param timespan The timespan to check during which te user is available
     * @return True if and only if the user is not assigned to a task that is 
     * executing and false in the given timespan.
     */
    public boolean isAvailable(Timespan timespan){
        for(Task task : tasks){
            //TODO task logic necessary
        }
        return false;
    }
}
