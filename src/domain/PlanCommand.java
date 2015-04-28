package domain;

import exception.ConflictException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import domain.time.Timespan;

/**
 * This class represents the action of doing a reservation
 *
 * @author Mathias, Frederic, Pieter-Jan
 */
public class PlanCommand {

    private final Task task;
    private final List<Resource> resources;
    private List<ReservationCommand> reservations;
    private Map<Resource, Resource.Memento> oldReservations;

    public PlanCommand(Timespan timespan, List<Resource> resources, Task task) {
        this.task = task;
        this.resources = resources;
        for (Resource resource : resources) {
            reservations.add(new ReservationCommand(timespan, resource, task));
        }
        oldReservations = new HashMap<Resource, Resource.Memento>();
    }

    public void execute() throws ConflictException {
        if (task.isPlanned()) {
            moveTask();
        } else {
            planTask();
        }
    }

    private void moveTask() throws ConflictException {
    	oldReservations.clear();
        for(Resource res : resources){
        	oldReservations.put(res, res.createMemento());
        }
        
        try {
            for (ReservationCommand command : reservations) {
                command.execute();
            }
        } catch (ConflictException ex) {
            revert();
            revertMove();
            
            throw ex;
        }
        
    }

    private void planTask() throws ConflictException {
        try {
            for (ReservationCommand command : reservations) {
                command.execute();
            }
        } catch (ConflictException ex) {
            revert();
            
            throw ex;
        }
    }

    private void revert() {
        for (ReservationCommand command : reservations) {
            command.revert();
        }
    }
    
    private void revertMove() {
        for(Entry<Resource, Resource.Memento> entry : oldReservations.entrySet())
            	entry.getKey().setMemento(entry.getValue());
    }
}
