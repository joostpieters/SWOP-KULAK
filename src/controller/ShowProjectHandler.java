package controller;


import domain.BranchOffice;
import domain.Company;
import domain.Project;
import domain.dto.DetailedProject;
import domain.dto.DetailedTask;
import domain.time.Clock;
import java.util.ArrayList;
import java.util.List;



/**
 * This handler, handles the show project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class ShowProjectHandler {
    
    private final BranchOffice manager;
    
    private Project currentProject;
    private final Clock clock;
    private final Company db;
    
    /**
     * Initialize a new show project handler with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this handler. 
     * @param clock The clock to use in this handler
     * @param database The database to use in this handler.
     */   
    public ShowProjectHandler(BranchOffice manager, Clock clock, Company database){
        this.manager = manager;
        this.clock = clock;
        this.db = database;
    }
    
    /**
     * 
     * @return A list of projects of this projectContainer
     */
    public List<DetailedProject> getProjects(){
        
        return new ArrayList<>(db.getProjects());
    }
    
    /**
     * Select the project with the given id in this projectContainer and set it as
     * this current project to show
     * 
     * @param projectId The id of the project ro retrieve 
     */
    public void selectProject(int projectId){
        currentProject = manager.getProjectContainer().getProject(projectId);
    }
    /**
     * @return The current project of this handler.
     */  
    public DetailedProject  getProject(){
        return currentProject;
    }
    
    /**
     * Returns the task with the given id of the project with the given id.
     * 
     * @param taskId The id of the task to retrieve.
     * @return The task with the given id in the current project of this handler.
     * @throws IllegalStateException The current project is null.
     */
    public DetailedTask getTask(int taskId) throws IllegalStateException{
        if(currentProject == null){
            throw new IllegalStateException("No project is currently selected in this handler.");
        }
        return currentProject.getTask(taskId);
    }
    
    /**
     * 
     * @return The clock used by this handler.
     */
    public Clock getClock(){
        return clock;
    }
}
