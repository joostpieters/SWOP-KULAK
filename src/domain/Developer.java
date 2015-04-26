package domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import domain.time.Timespan;
import domain.time.WorkWeekConfiguration;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Developer extends Resource implements User {
    
    private String name;
    private WorkWeekConfiguration conf;
    private final List<Task> tasks;
    private final String role;
    
    /**
     * Initializes a new developer with the given name and role
     * 
     * @param name The name of the developer
     */
    public Developer(String name) {
        super(name);
        this.name = name;
        this.role = "developer";
        conf = new WorkWeekConfiguration();
        tasks = new ArrayList<>();
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
