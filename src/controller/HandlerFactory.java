package controller;

import domain.Company;
import domain.time.Clock;
import domain.user.Acl;
import domain.user.Auth;


/**
 * This handler, handles the show project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class HandlerFactory {

    private final Company company;
    private final Acl acl;
    private final Auth auth;
    private final Clock clock;
    
    
    
    /**
     * Initialize a new front controller with the given company, authentication manager, action control list and clock.
     * 
     * @param company The company to use
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param clock The system clock to use in this factory
     */   
    public HandlerFactory(Company company, Auth auth, Acl acl, Clock clock){
        this.acl = acl;
        this.auth = auth;
        this.clock = clock;
        this.company = company;
    }
    
    /** 
     * @return A new show project handler, initialized with this manager.
     */
    public ShowProjectHandler getShowProjectHandler(){
        return new ShowProjectHandler(company, clock);
    }
    
    /** 
     * @return A new create project handler, initialized with this manager.
     */
    public CreateProjectHandler getCreateProjectHandler(){
        return new CreateProjectHandler(auth.getUser().getBranchOffice(), auth, acl);
    }
    
    /** 
     * @return A new create task handler, initialized with this manager.
     */
    public CreateTaskHandler getCreateTaskHandler(){
        return new CreateTaskHandler(auth.getUser().getBranchOffice(), company.getResourceTypes(), auth, acl);
    }
    
    /** 
     * @return A new update task handler, initialized with this manager.
     */
    public UpdateTaskStatusHandler getUpdateTaskHandler(){
        return new UpdateTaskStatusHandler(auth.getUser().getBranchOffice(), clock, auth, acl);
    }
    
     /** 
     * @return A new advance system time handler, initialized with this manager.
     */
    public AdvanceSystemTimeHandler getAdvanceSystemTimeHandler(){
        return new AdvanceSystemTimeHandler(clock);
    }
    
    /** 
     * @return A new plan task handler, initialized with this manager.
     */
    public PlanTaskHandler getPlanTaskHandler(){
        return new PlanTaskHandler(auth.getUser().getBranchOffice(), clock,auth, acl);
    }
    
    /** 
     * @return A new delegate task handler.
     */
    public DelegateTaskHandler getDelegatedTaskHandler(){
        return new DelegateTaskHandler(company, auth.getUser().getBranchOffice(), auth, acl);
    }
    
    /** 
     * @return A new run simulation handler, initialized with this manager.
     */
    public RunSimulationHandler getSimulationHandler(){
        return new RunSimulationHandler(auth.getUser().getBranchOffice(), company.getResourceTypes(), clock, auth, acl);
    }
    
    /** 
     * @return A new login handler.
     */
    public LoginHandler getLoginHandler(){
        return new LoginHandler(company, auth);
    }
    
}
