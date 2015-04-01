package controller;

import domain.Clock;
import domain.ProjectManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the create task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class AdvanceSystemTimeHandler {
    
    private final ProjectManager manager;
    private final Clock clock;
        
    /**
     * Initialize a new advance system time handler with the given projectmanager.
     * 
     * @param manager The manager to use in this handler
     * @param clock The clock to use to manipulate
     */   
    public AdvanceSystemTimeHandler(ProjectManager manager, Clock clock){
        this.manager = manager;
        this.clock = clock;
    }
    
    
    /**
     * Advance the system clock to the given timestamp
     * 
     * @param timestamp The timestamp to advance the systemclock to (yyyy-MM-dd HH:mm).
     */   
    public void  advanceTime(String timestamp){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime time = LocalDateTime.parse(timestamp, formatter);
            clock.advanceTime(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("The provided timestamp is in the wrong format.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
        
    } 
    
    /**
     * 
     * @return The current time as indicated by the system clock 
     */
    public LocalDateTime getCurrentTime(){
        return clock.getTime();
    }
}
