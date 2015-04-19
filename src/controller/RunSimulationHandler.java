package controller;

import domain.Acl;
import domain.Auth;
import domain.ProjectContainer;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the create project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class RunSimulationHandler extends Handler{
    
    private final ProjectContainer manager;
    
    /**
     * Initialize this createprojecthandler with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this handler. 
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */
    public RunSimulationHandler(ProjectContainer manager, Auth auth, Acl acl){
        super(auth, acl);
        this.manager = manager;
    }
    
    /**
     * Create the project with the given parameters in this project container.
     * 
     * @param name The name of the new project.
     * @param description The description of the new project.
     * @param creationTime The creation time of the new project.
     * @param dueTime The due time of the new project.
     * @throws RuntimeException an error occured in processing the create project request.
     */
    public void createProject(String name, String description, LocalDateTime creationTime, LocalDateTime dueTime) throws RuntimeException{
       
        try{
            manager.createProject(name, description, creationTime, dueTime);
        }catch(IllegalArgumentException | IllegalStateException e){
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(RunSimulationHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
        
        
        
    } 
}