package domain.command;

import domain.ReservationCommand;
import domain.Resource;
import domain.Task;
import domain.Resource.Memento;
import domain.time.Timespan;
import exception.ConflictException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents the action of doing a reservation
 *
 * @author Mathias, Frederic, Pieter-Jan
 */
public class PlanCommand implements Command {

    private final Task task;
    private final List<Resource> resources;
    private final List<ReservationCommand> reservations;
    private final Map<Resource, Resource.Memento> oldReservations;

    public PlanCommand(Timespan timespan, List<Resource> resources, Task task) {
        this.task = task;
        this.resources = resources;
        reservations = new ArrayList<>();
        for (Resource resource : resources) {
            reservations.add(new ReservationCommand(timespan, resource, task));
        }
        oldReservations = new HashMap<>();
    }

    public void execute() {
    	// TODO deze exception werkelijk behandelen
    	try {
    		if (task.isPlanned()) {
            	moveTask();
        	} else {
            	planTask();
        	}
    	} catch (Exception e) {}
    	
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

    public void revert() {
        for (ReservationCommand command : reservations) {
            command.revert();
        }
    }
    
    private void revertMove() {
        for(Entry<Resource, Resource.Memento> entry : oldReservations.entrySet())
            	entry.getKey().setMemento(entry.getValue());
    }
}
