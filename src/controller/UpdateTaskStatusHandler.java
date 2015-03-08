package controller;

import domain.DetailedProject;
import domain.DetailedTask;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This handler, handles the update task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class UpdateTaskStatusHandler {
    
    private final ProjectManager manager;
    
    private Task currentTask;
    
    /**
     * Initialize a new create task handler with the given projectmanager.
     * 
     * @param manager The projectmanager to use in this handler. 
     */   
    public UpdateTaskStatusHandler(ProjectManager manager){
        this.manager = manager;
    }
    
    
    /**
     * Returns a map with all available tasks in this projectmanager ascociated 
     * with their project.
     * 
     * @return All available tasks in the projectmanager of this handler.
     */   
    public Map<DetailedTask, DetailedProject>  getAvailableTasks(){
        
        return new HashMap<>(manager.getAllAvailableTasks());
    }
    
    /**
     * Sets the task with given id in the project with the given id as this 
     * handlers current task.
     * 
     * @param pId The id of the project, this task belongs to.
     * @param tId The id of the task to select.
     */
    public void selectTask(int pId, int tId){
        currentTask = manager.getProject(pId).getTask(tId);
    }
    
    /**
     * Update the start and end time and status of this current task.
     * 
     * @param startTime The start time of the task (yyyy-MM-dd HH:mm)
     * @param endTime The end time of the task (yyyy-MM-dd HH:mm)
     * @param status The status of the task @see domain.Status
     */
    public void updateCurrentTask(String startTime, String endTime, String status){
        if(currentTask  == null){
            throw new IllegalStateException("No task currently selected.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        
        LocalDateTime start;
        LocalDateTime end;
        
        try {
            start = LocalDateTime.parse(startTime, formatter);
            end = LocalDateTime.parse(endTime, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("The provided dates are not in the right format.");
        }
        
        
        try {
            currentTask.update(start, end, Status.valueOf(status));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
        
    }
}
