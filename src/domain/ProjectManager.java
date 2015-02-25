package domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class represents a manager to contain the projects in the system.
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class ProjectManager {
    
    private ArrayList<Project> projects;
    
    public ArrayList<Project> getProjects(){
        return (ArrayList<Project>) projects.clone();
    }
    
    public void createProject(String name, String description, GregorianCalendar startTime, GregorianCalendar dueTime){
        
    }
    
    public void addProject(Project project){
        
    }
    
    public Project getProject(int pId){
        return null;
    }
    
    public ArrayList<Task> getAvailableTasks(){
        return null;
    }
}
