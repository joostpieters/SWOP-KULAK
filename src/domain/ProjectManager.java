package domain;

import java.util.ArrayList;

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
}
