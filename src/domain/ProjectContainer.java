package domain;

import exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a manager to contain the projects in the system.
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class ProjectContainer {
    
    private final Map<Integer, Project> projects;
    
        
    /**
     * Initializes a new project container and its systemClock.
     */
    public ProjectContainer(){
        projects = new HashMap<>();
    }
    
    /**
     * @return the list of projects contained by this projectContainer. 
     */
    public List<Project> getProjects(){
        return new ArrayList<>(projects.values());
    }
    
   
	/**
     * @return the list of unfinished projects contained by this projectContainer. 
     */
    public List<Project> getUnfinishedProjects(){
        List<Project> unfinishedProjects = new ArrayList<>();
        for(Project project : projects.values()){
            if(!project.isFinished()){
                unfinishedProjects.add(project);
            }
        }
        return unfinishedProjects;
    }
    
    /**
     * Creates a new project with the given details and add it to this projectContainer.
     * The new project gets an id that equals the number of projects in this projectContainer.
     * 
     * @param 	name
     * 			The name of the project
     * @param 	description 
     * 			The description of the project
     * @param 	startTime 
     * 			The start time of the project
     * @param 	dueTime 
     * 			The time by which the project should be ended.
     * @return	the project that has been created.
     */
    public Project createProject(String name, String description, LocalDateTime startTime, LocalDateTime dueTime){
        
        Project p = new Project(name,description, startTime, dueTime);
        addProject(p);
        return p;
    }
    
    /**
     * Adds the given project to this project container.
     * 
     * @param project The project to add. 
     */
    private void addProject(Project project){
        projects.put(project.getId(), project);
    }
    
    /**
     * Returns the project with the given id.
     * 
     * @param pId The id of the project to retrieve
     * @return The project instance that belongs to the given id.
     * @throws ObjectNotFoundException The project with the specified id doesn't exist
     * in this project container.
     */
    public Project getProject(int pId) throws ObjectNotFoundException{
        if(!projects.containsKey(pId))
            throw new ObjectNotFoundException("The project with the specified id doesn't exist.", pId);
        return projects.get(pId);      
        
    }
    
    /**
     * 
     * @return The number of projects that are managed by this projectContainer. 
     */
    public int getNbProjects(){
        return projects.size();
    }
    
     /**
     * Returns a map with all available tasks in this projectContainer ascociated 
     * with their project.
     * 
     * @return A map wich has as keys, all available tasks and as value the
     * project the task belongs to.
     */   
    public Map<Task, Project>  getAllAvailableTasks(){
        
        Map<Task, Project> availableTasks = new HashMap<>();
        for (Project project : projects.values()){
            for(Task task : project.getAvailableTasks()){
                availableTasks.put(task, project);
            }           
        }
        return availableTasks;
    }
    
     /**
     * Returns a list with all available tasks in this projectContainer ascociated 
     * with their project.
     * 
     * @return A list with all tasks of all projects in this projectContainer.
     */   
    public List<Task>  getAllTasks(){
        
        List<Task> tasks = new ArrayList<>();
        for (Project project : projects.values()){
            tasks.addAll(project.getTasks());
                      
        }
        return tasks;
    }
}
