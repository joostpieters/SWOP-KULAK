package controller;

import domain.ProjectManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This handler, handles the create project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateProjectHandler {
    
    private final ProjectManager manager;
    
    /**
     * Initialize this createprojecthandler with the given projectmanager.
     * 
     * @param manager The projectmanager to use in this handler. 
     */
    public CreateProjectHandler(ProjectManager manager){
        this.manager = manager;
    }
    
    /**
     * Create the project with the given parameters in this project manager.
     * 
     * @param name The name of the new project.
     * @param description The description of the new project.
     * @param startTime The start time of the new project.
     * @param dueTime The due time of the new project.
     */
    public void createProject(String name, String description, String startTime, String dueTime){
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime due = LocalDateTime.parse(dueTime, formatter);
        
        manager.createProject(name, description, start, due);
    } 
}
