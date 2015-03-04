package controller;

import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;

import java.util.List;

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
    public List<Task>  getTasksByProject(int Pid){
        return manager.getProject(Pid).getTasks();
    }
    
    /**
     * Create a new task with the given parameters.
     * 
     * @param pId The id of the project to add the task to.
     * @param description The description of the task
     * @param accDev The deviation by which the etimated duration can deviate
     * @param prereq The id's of the task that have to be finished before this task.
     * @param estDuration The estimated duration of the tast
     * @param status The status of the task, FINISHED, FAILED, AVAILABLE, UNAVAILABLE
     */
    public void createTask(int pId, String description, int accDev, int prereq[], int estDuration, String status){       
        Project project = manager.getProject(pId);
        
        // TODO duartion ipv int
        project.createTask(description, estDuration, accDev, prereq, Status.valueOf(description));
    } 
}
