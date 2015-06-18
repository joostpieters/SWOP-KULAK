package controller;


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
    
    private final Company company;
    private final Clock clock;
    private Project currentProject;
    
    /**
     * Initialize a new show project handler with the given projectContainer.
     * 
     * @param company The company to use in this handler.
     * @param clock The clock to use in this handler
     */   
    public ShowProjectHandler(Company company, Clock clock) {
        this.company = company;
        this.clock = clock;
    }
    
    /**
     * 
     * @return A list of projects of the company
     */
    public List<DetailedProject> getProjects() {
        return new ArrayList<>(company.getProjects());
    }
    
    /**
     * Select the project with the given id in the company and 
     * set it as the current project to show
     * 
     * @param projectId The id of the project to retrieve 
     */
    public void selectProject(int projectId) {
    	currentProject = company.getProject(projectId);
    }
    
    /**
     * @return The current project of this handler.
     */  
    public DetailedProject  getProject() {
        return currentProject;
    }
    
    /**
     * Returns the task with the given id of the current project of this handler.
     * 
     * @param taskId The id of the task to retrieve.
     * @return The task with the given id in the current project of this handler.
     * @throws IllegalStateException if the current project is null.
     * @see #getProject()
     */
    public DetailedTask getTask(int taskId) throws IllegalStateException {
        if(currentProject == null) {
            throw new IllegalStateException("No project is currently selected in this handler.");
        }
        
        return currentProject.getTask(taskId);
    }
    
    /**
     * 
     * @return The clock used by this handler.
     */
    public Clock getClock() {
        return clock;
    }
}
