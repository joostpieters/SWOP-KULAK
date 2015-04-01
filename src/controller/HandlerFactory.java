package controller;

import domain.Clock;
import domain.ProjectContainer;


/**
 * This handler, handles the show project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class HandlerFactory {
    
    private final ProjectContainer manager;
    private final Clock clock;
    
    
    
    /**
     * Initialize a new front controller with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this factory. 
     * @param clock The system clock to use in this factory
     */   
    public HandlerFactory(ProjectContainer manager, Clock clock){
        this.manager = manager;
        this.clock = clock;
    }
    
    /** 
     * @return A new show project handler, initialized with this manager.
     */
    public ShowProjectHandler getShowProjectHandler(){
        return new ShowProjectHandler(manager, clock);
    }
    
    /** 
     * @return A new create project handler, initialized with this manager.
     */
    public CreateProjectHandler getCreateProjectHandler(){
        return new CreateProjectHandler(manager);
    }
    
    /** 
     * @return A new create task handler, initialized with this manager.
     */
    public CreateTaskHandler getCreateTaskHandler(){
        return new CreateTaskHandler(manager);
    }
    
    /** 
     * @return A new update task handler, initialized with this manager.
     */
    public UpdateTaskStatusHandler getUpdateTaskHandler(){
        return new UpdateTaskStatusHandler(manager);
    }
    
     /** 
     * @return A new advance system time handler, initialized with this manager.
     */
    public AdvanceSystemTimeHandler getAdvanceSystemTimeHandler(){
        return new AdvanceSystemTimeHandler(manager, clock);
    }
}
