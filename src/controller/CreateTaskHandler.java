package controller;

import domain.DetailedProject;
import domain.DetailedTask;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the create task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskHandler {
    
    private final ProjectManager manager;
    
    /**
     * Initialize a new create task handler with the given projectmanager.
     * 
     * @param manager The projectmanager to use in this handler. 
     */   
    public CreateTaskHandler(ProjectManager manager){
        this.manager = manager;
    }
    
    
    /**
     * Get the list of projects that belongs to the project with the given id.
     * 
     * @param Pid The id of the project to get the tasks from.
     * @return A list containing all the tasks of the project with the given id.
     */   
    public List<DetailedTask>  getTasksByProject(int Pid){
        return new ArrayList<>(manager.getProject(Pid).getTasks());
    }
    
    /**
     * Create a new task with the given parameters.
     * 
     * @param pId The id of the project to add the task to.
     * @param description The description of the task
     * @param accDev The deviation by which the etimated duration can deviate
     * @param prereq The id's of the task that have to be finished before this task.
     * @param estDuration The estimated duration of the tast
     */
    public void createTask(int pId, String description, int accDev, int prereq[], int estDuration){       
        Project project = manager.getProject(pId);
        //TODO right constructor
        try {
            project.createTask(description, estDuration, accDev, prereq, Status.AVAILABLE);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
    } 
    
    /**
     * 
     * @return A list of all projects in this projectmanager.
     */
    public List<DetailedProject> getProjects(){
        return new ArrayList<>(manager.getProjects());
    }
    
    public List<DetailedTask> getAllTasks(){
        return new ArrayList<>(manager.getAllTasks());
    }
}
