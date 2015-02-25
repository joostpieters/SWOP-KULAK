package domain;

import java.util.ArrayList;

/**
 * This class represents a project
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Project {
    
    private ArrayList<Task> tasks;
    
    public ArrayList<Task> getTasks(){
        return (ArrayList<Task>) tasks.clone();
    }
    
   
}
