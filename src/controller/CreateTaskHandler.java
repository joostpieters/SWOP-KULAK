package controller;

import domain.DetailedProject;
import domain.DetailedTask;
import domain.Duration;
import domain.Project;
import domain.ProjectManager;
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
     * Get the list of tasks that belongs to the project with the given id.
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
     * @param accDev The deviation by which the estimated duration can deviate
     * @param prereq The id's of the task that have to be finished before this task.
     * @param estDurHours The estimated duration of the test in hours.
     * @param estDurMinutes The extra minutes to the estimated duration in hours.
     * @param altfor The id of the task, the task is an alternative for, is smaller
     * than 0, if no alternative. 
     */
    public void createTask(int pId, String description, int accDev, List<Integer> prereq, int estDurHours, int estDurMinutes, int altfor){       
        if(prereq == null){
            prereq = Project.NO_DEPENDENCIES;
        }
        
        if(altfor < 0){
            altfor = Project.NO_ALTERNATIVE;
        }
        try {
            Project project = manager.getProject(pId);
            Duration duration = new Duration(estDurHours, estDurMinutes);
            project.createTask(description, duration, accDev, altfor, prereq);
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
     * @throws IllegalStateException No unfinished projects are available.
     */
    public List<DetailedProject> getUnfinishedProjects() throws IllegalStateException{
        ArrayList<DetailedProject> projects = new ArrayList<>(manager.getUnfinishedProjects());
        if(projects.isEmpty()){
            throw new IllegalStateException("No unfinished projects are available.");
        }
        return projects;
    }
    
    /**
     * 
     * @return A list of all the tasks available in this manager. 
     */
    public List<DetailedTask> getAllTasks(){
        return new ArrayList<>(manager.getAllTasks());
    }
}
