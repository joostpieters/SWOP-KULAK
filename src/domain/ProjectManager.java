package domain;

import exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class represents a manager to contain the projects in the system.
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class ProjectManager {
    
    private final ArrayList<Project> projects;
    
    /**
     * Initializes a new projectmanager.
     */
    public ProjectManager(){
        projects = new ArrayList<>();
    }
    
    /**
     * 
     * @return The list of projects contained by this projectmanager. 
     */
    public ArrayList<Project> getProjects(){
        return (ArrayList<Project>) projects.clone();
    }
    
    /**
     * Creates a new project with the given details and add it to this projectmanager.
     * 
     * @param name The name of the project
     * @param description The description of the project
     * @param startTime The start time of the project
     * @param dueTime The time by which the project should be ended.
     */
    public void createProject(String name, String description, GregorianCalendar startTime, GregorianCalendar dueTime){
        // TODO ID
        addProject(new Project(0,name, name, dueTime, dueTime));
    }
    
    /**
     * Adds the given project to this project manager.
     * 
     * @param project The project to add. 
     */
    public void addProject(Project project){
        projects.add(project);
    }
    
    /**
     * Returns the project with the given id.
     * 
     * @param pId The id of the project to retrieve
     * @return The project instance that belongs to the given id.
     * @throws ObjectNotFoundException The project with the specified id doesn't exist
     * in this project manager.
     */
    public Project getProject(int pId) throws ObjectNotFoundException{
        try{
            return projects.get(pId);
        }catch(IndexOutOfBoundsException e){
            throw new ObjectNotFoundException("The project with the specified id doesn't exist.", pId);
        }
        
    }
    
    public ArrayList<Task> getAvailableTasks(){
        return null;
    }
}
