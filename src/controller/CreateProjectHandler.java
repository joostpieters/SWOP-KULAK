package controller;

import domain.ProjectManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * @throws RuntimeException an error occured in processing the create project request.
     */
    public void createProject(String name, String description, String startTime, String dueTime) throws RuntimeException{
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try{
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            LocalDateTime due = LocalDateTime.parse(dueTime, formatter);
            manager.createProject(name, description, start, due);
        }catch(DateTimeParseException e){
            throw new IllegalArgumentException("The given time is not in the right format.");
        }catch(IllegalArgumentException | IllegalStateException e){
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
        
        
        
    } 
}
