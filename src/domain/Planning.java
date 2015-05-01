package domain;

import domain.task.Task;
import domain.time.Timespan;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents a planning for a task, containing the reservations and
 * the planned timespan
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Planning {
    
    private List<Resource> resources;
    private final Timespan timespan;
    private final Task task;
    
    public Planning(List<Resource> reservations, Timespan timespan, Task task){
        this.resources = reservations;
        this.timespan = timespan;
        this.task = task;
    }
    
    
    public void clearFutureReservations(LocalDateTime currentTime) {
        for (Resource resource : resources) {
            resource.clearFutureReservations(currentTime, task);
        }
    }

    public boolean isBefore(LocalDateTime currentTime) {
        return timespan.startsBefore(currentTime);
    }
    
}
