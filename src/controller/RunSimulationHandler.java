package controller;

import domain.BranchOffice;
import domain.ResourceType;
import domain.command.SimulatorCommand;
import domain.time.Clock;
import domain.user.Acl;
import domain.user.Auth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler handles the create project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class RunSimulationHandler extends Handler {
	
    private final BranchOffice office;
    private final List<ResourceType> resourceTypes;
    private final Clock clock;
    
    private final SimulatorCommand simulatorCommand;
    
    /**
     * Initialize this handler with the given branch office.
     * 
     * @param office The branch office to use in this handler. 
     * @param resourceTypes All resource types in the company.
     * @param clock The clock to use in this handler
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */
    public RunSimulationHandler(BranchOffice office, List<ResourceType> resourceTypes, Clock clock, Auth auth, Acl acl){
        super(auth, acl);
        this.office = office;
        this.resourceTypes = resourceTypes;
        this.clock = clock;
        this.simulatorCommand = new SimulatorCommand();
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
            office.getProjectContainer().createProject(name, description, creationTime, dueTime);
        }catch(IllegalArgumentException | IllegalStateException e){
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(RunSimulationHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
        
    } 
    
    /**
     * 
     * @return A handler to simulate the creation of a task.
     */
    public CreateTaskHandler getCreateTaskSimulatorHandler() {
    	return new CreateTaskHandler(office, resourceTypes, auth, acl, simulatorCommand);
    } 
    
    /**
     * 
     * @return A handler to simulate the planning of a task.
     */
    public PlanTaskHandler getPlanTaskSimulatorHandler() {
    	return new PlanTaskHandler(office, clock, auth, acl, simulatorCommand);
    } 
    
    /**
     * Cancel the simulation and leave the system unchanged
     */
    public void cancelSimulation(){
    	simulatorCommand.revert();
    }
    
    /**
     * Keeps the changes that were made
     */
    public void carryOutSimulation()
    {
    	
    }
}
