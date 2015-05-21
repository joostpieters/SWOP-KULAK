package domain.command;

import domain.Planning;
import domain.Resource;
import domain.ResourceType;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Timespan;
import exception.ConflictException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    //TODO: PlanTaskCommand lijkt niet verantwoordelijk voor bijhouden Clock.
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
     * @param clock The clock to use with this planning
     */
    public PlanTaskCommand(Timespan timespan, List<Resource> resources, Task task, Clock clock) {
        this.task = task;
        this.resources = resources;
        //TODO: oplossing hiervoor???????????
        if(!requirementsMet())
        	throw new ConflictException("With the given resources, certain requirements are not met", task, null);
        this.timespan = timespan;
        this.clock = clock;
        reservations = new ArrayList<>();
        
        for (Resource resource : resources) {
            reservations.add(new CreateReservationCommand(timespan, resource, task));
        }
    }
    
    //TODO: commentaar
    private boolean requirementsMet() {
		Map<ResourceType, Integer> requirements = task.getRequiredResources();
		for(ResourceType type : requirements.keySet()) {
			int count = 0;
			for(Resource r : resources) {
				if(r.getType().equals(type))
					count++;
			}
			if(count < requirements.get(type))
				return false;
		}
		return true;
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
        //TODO: waarom wordt hier geen task.plan() gebruikt? :'(
        task.setPlanning(new Planning(resources, timespan, task, clock));
    }
    
    /**
     * Reverts the changes made by the last execution of this command.
     */
    @Override
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
