package domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class represents a task
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Task {
    
    private int id;
    
    private String description;
    
    private float deviation;
    
    public Task(int estDur, int accdev, Task alternative, ArrayList<Task> prereq, Status status){
        
    }
    
    public void update(GregorianCalendar start, GregorianCalendar end, Status status){
        
    }
    
    private boolean checkUpdate(GregorianCalendar start, GregorianCalendar end, Status status){
        return true;
    }
    
    public boolean isAvailable(){
        return true;
    }
    
    
    
}
