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
    
    private Timespan duration;
    
    private Duration estimatedDuration;
    
    private Task alternativeTask;
    
    private ArrayList<Task> prerequisiteTasks;
    
    private Status status;
    
    private static int nextId=0;
    
    public int getId()
    {
    	return this.id;
    }
    
    public Task(String description, int estDur, float accDev, ArrayList<Task> prereq, Status status){
        this.id = generateId();
    	//this.estimatedDuration = new Duration(estDur);
    	this.id = Task.generateId();
    	this.description = description;
    	this.deviation = accDev;
    	this.alternativeTask = alternativeTask;
    	this.prerequisiteTasks = prereq;
    	this.status = status;
    }
    
    public void update(GregorianCalendar start, GregorianCalendar end, Status status){
        
    }
    
    public boolean isAvailable(){
        return true;
    }
    
    /**
     * Generates an id for a new task.
     * 
     * @return The id to be used for a newly created task.
     */
    private static int generateId()
    {
    	return nextId++;
    }
    
}
