package domain.command;

import domain.Resource;
import domain.Task;
import domain.time.Timespan;
import exception.ConflictException;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * This class represents the action of doing a reservation
 *
 * @author Mathias, Frederic, Pieter-Jan
 */
public class PlanTaskCommand implements ICommand {

    private final Task task;
    private final List<Resource> resources;
    private final List<CreateReservationCommand> reservations;
    private final Map<Resource, Resource.Memento> oldResourceStates;

    
    /**
     * Creates a new plan task command representing the action of planning a task based on the given parameters.
     * 
     * @param timespan The timespan during which the task is planned.
     * @param resources The resources belonging to the task.
     * @param task The task to be planned.
     */
    public PlanTaskCommand(Timespan timespan, List<Resource> resources, Task task) {
        this.task = task;
        this.resources = resources;
        reservations = new ArrayList<>();
        for (Resource resource : resources) {
            reservations.add(new CreateReservationCommand(timespan, resource, task));
        }
        oldResourceStates = new HashMap<>();
    }
    /**
     * TODO commentaar
     */
    public void execute() throws ConflictException {
		if (task.isPlanned()) {
        	moveTask();
    	} else {
        	planTask();
    	}
    }

    private void moveTask() throws ConflictException {
    	// TODO klopt het dat de bedoeling is de oorspronkelijke reservations horende bij deze taak te verwijderen
    	// TODO en daarna simpelweg nieuwe reservations aanmaken
    	//save the states of the old reservations belonging
    	oldResourceStates.clear();
        for(Resource res : resources){
        	oldResourceStates.put(res,  res.createMemento());
        }
        
        try {
            for (CreateReservationCommand command : reservations) {
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
            for (CreateReservationCommand command : reservations) {
                command.execute();
            }
        } catch (ConflictException ex) {
            revert();
            throw ex;
        }
    }

    public void revert() {
        for (CreateReservationCommand command : reservations) {
            command.revert();
        }
    }
    
    private void revertMove() {
    	for(Entry<Resource, Resource.Memento> entry : oldResourceStates.entrySet())
    		entry.getKey().setMemento(entry.getValue());
    }
}
