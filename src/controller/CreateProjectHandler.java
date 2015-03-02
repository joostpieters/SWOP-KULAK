package controller;

import domain.ProjectManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This handler, handles the 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateProjectHandler {
    
    private final ProjectManager manager;
    
    public CreateProjectHandler(ProjectManager manager){
        this.manager = manager;
    }
    
    public void createProject(String name, String description, String startTime, String dueTime){
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:MM");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime due = LocalDateTime.parse(startTime, formatter);
        
        manager.createProject(name, description, start, due);
    } 
}
