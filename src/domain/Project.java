package domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;

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
    
    public Project(int id, String name, String descr, GregorianCalendar creation, GregorianCalendar due){
        
    }
    
    
}
