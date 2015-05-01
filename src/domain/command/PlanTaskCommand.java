package domain.command;

import domain.Planning;
import domain.Resource;
import domain.ResourceType;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Timespan;
import exception.ConflictException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    private final Clock clock;
    
    private final Timespan timespan;
    
    private Planning originalTaskPlanning;
    private Planning.Memento originalTaskPlanningMemento;
    
    /**
     * Creates a new plan task command representing the action of planning a task based on the given parameters.
     * 
     * @param timespan The timespan during which the task is planned.
     * @param resources The resources belonging to the task.
     * @param task The task to be planned.
     */
    public PlanTaskCommand(Timespan timespan, List<Resource> resources, Task task, Clock clock) {
        this.task = task;
        this.resources = resources;
        this.timespan = timespan;
        this.clock = clock;
        reservations = new ArrayList<>();
        repleteResources();
        
        for (Resource resource : resources) {
            reservations.add(new CreateReservationCommand(timespan, resource, task));
        }
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
     * The tasks future reservations are cleared and the new reservations are made.
     */
    @Override
    public void execute() throws ConflictException {
    	originalTaskPlanning = task.getPlanning();
    	if(originalTaskPlanning != null)
    	{
    		originalTaskPlanningMemento = originalTaskPlanning.createMemento();
    		originalTaskPlanning.clearFutureReservations(clock.getTime());
    	}
    	
    	Stack<CreateReservationCommand> executedCmds = new Stack<>();
        try {
            for (CreateReservationCommand command : reservations) {
                command.execute();
                executedCmds.push(command);
            }
        } catch (ConflictException ex) {
        	while(!executedCmds.isEmpty())
        		executedCmds.pop().revert();
            throw ex;
        }
        task.setPlanning(new Planning(resources, timespan, task));
        clock.attach(task.getPlanning());
    }
    
    /**
     * Reverts the changes made by the last execution of this command.
     */
    public void revert() {
    	clock.detach(task.getPlanning());
        task.setPlanning(originalTaskPlanning);
        
    	for (CreateReservationCommand command : reservations) {
            command.revert();
        }
    	
        if(originalTaskPlanning != null && originalTaskPlanningMemento != null)
        {
        	originalTaskPlanning.setMemento(originalTaskPlanningMemento);
        }
    }
}
