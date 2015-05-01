package domain.command;

import domain.Reservation;
import domain.Resource;
import domain.Task;
import domain.time.Timespan;
import exception.ConflictException;

/**
 * This class represents the action of doing a reservation
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class CreateReservationCommand implements ICommand {
    private final Task task;
    private final Resource resource;
    private Resource.Memento resourceMemento;
    private final Timespan timespan;
    private Reservation reservation;
    /**
     * Initializes a new reservation command based on the given time span, resource and task.
     * 
     * @param timespan The time span of the reservation
     * @param resource The resource of the reservation
     * @param task The task of the reservation
     */
    public CreateReservationCommand(Timespan timespan, Resource resource, Task task){
        this.task = task;
        this.resource = resource;
        this.timespan = timespan;
    }
    
    /**
     * Creates a new reservation with the timespan, resource and task
     * belonging to this reservation command.
     * @throws ConflictException 
     */
    public void execute() throws ConflictException{
    	
    	resourceMemento = resource.createMemento();
    	reservation = resource.makeReservation(task, timespan);
    }
    
    public void revert(){
        if(reservation != null && resourceMemento != null)
            resource.setMemento(resourceMemento);
    }
}
