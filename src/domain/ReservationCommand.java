package domain;

import domain.time.Timespan;
import exception.ConflictException;

/**
 * This class represents the action of doing a reservation
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class ReservationCommand {
    private final Task task;
    private final Resource resource;
    private final Timespan timespan;
    private Reservation reservation;
    
    public ReservationCommand(Timespan timespan, Resource resource, Task task){
        this.task = task;
        this.resource = resource;
        this.timespan = timespan;
    }
    
    
    public void execute() throws ConflictException{
        reservation = resource.makeReservation(task, timespan);
    }
    
    public void revert(){
        if(reservation != null){
            resource.removeReservation(reservation);
        }        
    }
}
