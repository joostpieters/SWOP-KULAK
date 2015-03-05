package controller;

import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import java.util.List;
import java.util.Map;

/**
 * This handler, handles the show project use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class showProjectHandler {
    
    private final ProjectManager manager;
    
    private Project currentProject;
    
    /**
     * Initialize a new show project handler with the given projectmanager.
     * 
     * @param manager The projectmanager to use in this handler. 
     */   
    public showProjectHandler(ProjectManager manager){
        this.manager = manager;
    }
    
    /**
     * 
     * @return A list of projects of this projectmanager
     */
    public List<Project> getProjects(){
        return manager.getProjects();
    }
    
    /**
     * Select the project with the given id in this projectmanager and set it as
     * this current project to show
     * 
     * @param projectId The id of the project ro retrieve 
     */
    public void selectProject(int projectId){
        currentProject = manager.getProject(projectId);
    }
    /**
     * @return The current project of this handler.
     */  
    public Project  getProject(){
        return currentProject;
    }
    
    /**
     * Returns the task with the given id of the project with the given id.
     * 
     * @param taskId The id of the task to retrieve.
     * @return The task with the given id in the current project of this handler.
     * @throws IllegalStateException The current project is null.
     */
    public Task getTask(int taskId) throws IllegalStateException{
        if(currentProject == null){
            throw new IllegalStateException("No project is currently selected in this handler.");
        }
        return currentProject.getTask(taskId);
    }
}
