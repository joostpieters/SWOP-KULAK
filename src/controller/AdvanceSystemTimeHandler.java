package controller;

import domain.Clock;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This handler, handles the create task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class AdvanceSystemTimeHandler {
    
        
    /**
     * Initialize a new advance system time handler with the given projectmanager.
     * 
     */   
    public AdvanceSystemTimeHandler(){
        
    }
    
    
    /**
     * Advance the system clock to the given timestamp
     * 
     * @param timestamp The timestamp to advance the systemclock to (yyyy-MM-dd HH:mm).
     */   
    public void  advanceTime(String timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(timestamp, formatter);
        
    }    
}
