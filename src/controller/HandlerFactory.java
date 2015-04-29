package controller;

import domain.Acl;
import domain.Auth;
import domain.Database;
import domain.ProjectContainer;
import domain.time.Clock;


/**
 * This handler, handles the show project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class HandlerFactory {
    
    private final ProjectContainer manager;
    private final Clock clock;
    private final Acl acl;
    private final Auth auth;
    private final Database db;
    
    
    
    /**
     * Initialize a new front controller with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this factory. 
     * @param clock The system clock to use in this factory
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use
     */   
    public HandlerFactory(ProjectContainer manager, Clock clock, Auth auth, Acl acl, Database db){
        this.acl = acl;
        this.auth = auth;
        this.manager = manager;
        this.clock = clock;
        this.db = db;
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
        return new CreateProjectHandler(manager, auth, acl);
    }
    
    /** 
     * @return A new create task handler, initialized with this manager.
     */
    public CreateTaskHandler getCreateTaskHandler(){
        return new CreateTaskHandler(manager, auth, acl, db);
    }
    
    /** 
     * @return A new update task handler, initialized with this manager.
     */
    public UpdateTaskStatusHandler getUpdateTaskHandler(){
        return new UpdateTaskStatusHandler(manager, clock, auth, acl);
    }
    
     /** 
     * @return A new advance system time handler, initialized with this manager.
     */
    public AdvanceSystemTimeHandler getAdvanceSystemTimeHandler(){
        return new AdvanceSystemTimeHandler(manager, clock);
    }
    
    /** 
     * @return A new plan task handler, initialized with this manager.
     */
    public PlanTaskHandler getPlanTaskHandler(){
        return new PlanTaskHandler(manager, clock,auth, acl);
    }
    
    /** 
     * @return A new run simulation handler, initialized with this manager.
     */
    public RunSimulationHandler getSimulationHandler(){
        return new RunSimulationHandler(manager, clock, auth, acl, db);
    }
}
