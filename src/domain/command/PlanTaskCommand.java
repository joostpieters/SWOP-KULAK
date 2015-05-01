package domain.command;

import domain.Planning;
import domain.Resource;
import domain.ResourceType;
import domain.Task;
import domain.time.Timespan;
import exception.ConflictException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
    private final Timespan timespan;
    private Planning planning;

    
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
        this.timespan = timespan;
        reservations = new ArrayList<>();
        repleteResources();
        
        for (Resource resource : resources) {
            reservations.add(new CreateReservationCommand(timespan, resource, task));
        }
        oldResourceStates = new HashMap<>();
    }
    
    /**
     * Replete the list of resources to the point that all required resources are met
     * @throws IllegalArgumentException 
     */
    private void repleteResources() throws IllegalArgumentException {
        Map<ResourceType, Integer> required = task.getRequiredResources();
        for (ResourceType type : required.keySet()) {
            if (type.numberOfResources(resources) < required.get(type)) {
                // remove the all ready selected resources from the available resources
                Set<Resource> availableResources = type.getAvailableResources(timespan);
                availableResources.removeAll(resources);
                // chech whether the remaining resources can fulfill the still required quantity
                if (availableResources.size() < (required.get(type) - type.numberOfResources(resources))) {
                    throw new IllegalArgumentException("There are not enough resources available at this moment.");
                } else {
                    resources.addAll(availableResources);
                }
            }
        }
    }
    /**
     * TODO commentaar
     */
    @Override
    public void execute() throws ConflictException {
        if (task.isPlanned()) {
            moveTask();
    	} else {
            planTask();
    	}
    }
    
    /**
     * Overwrites the mementos saved in oldResourceStates
     * with the mementos belonging to the current states of the resources.
     */
    private void saveResourceStates()
    {
    	oldResourceStates.clear();
        for(Resource res : resources){
        	oldResourceStates.put(res,  res.createMemento());
        }
    }
    private void moveTask() throws ConflictException {
    	// TODO klopt het dat de bedoeling is de oorspronkelijke reservations horende bij deze taak te verwijderen
    	// TODO en daarna simpelweg nieuwe reservations aanmaken
        // TODO bovenstaande correct implementeren
    	//save the states of the old reservations belonging

        saveResourceStates();
        try {
            for (CreateReservationCommand command : reservations) {
                command.execute();
            }
        } catch (ConflictException ex) {
            revert();
            revertMove();
            
            throw ex;
        }
        
        planning = new Planning(resources, timespan, task);
        
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
    
    public Planning getResult(){
        return planning;
    }
}
