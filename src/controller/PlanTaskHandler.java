package controller;

import domain.BranchOffice;
import domain.Resource;
import domain.ResourceType;
import domain.command.SimulatorCommand;
import domain.dto.DetailedResource;
import domain.dto.DetailedResourceType;
import domain.dto.DetailedTask;
import domain.task.Task;
import domain.time.Clock;
import domain.user.Acl;
import domain.user.Auth;
import exception.ConflictException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This handler, handles the update task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class PlanTaskHandler extends Handler {

    protected final BranchOffice manager;
    private final Clock clock;
    
	private SimulatorCommand simulatorCommand;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param clock The clock to use in this handler
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */
    public PlanTaskHandler(BranchOffice manager, Clock clock, Auth auth, Acl acl) {
    	this(manager, clock, auth, acl, new SimulatorCommand());
    }
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param clock The clock to use in this handler
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param simulatorCommand The simulator command to which commands are added.
     */
    public PlanTaskHandler(BranchOffice manager, Clock clock, Auth auth, Acl acl, SimulatorCommand simulatorCommand)
    {
        super(auth, acl);
        this.manager = manager;
        this.clock = clock;
        this.simulatorCommand = simulatorCommand;
    }

    /**
     * Returns a list with all unplanned tasks in this projectContainer
     *
     * @return All unplanned tasks in the projectContainer of this handler.
     */
    public List<DetailedTask> getUnplannedTasks() {
        return new ArrayList<>(manager.getProjectContainer().getUnplannedTasks());
    }

    /**
     * Returns a list of proposed resources for each resourceType
     * of the task with the given id
     * 
     * @param pId The id of the projec the task belongs to
     * @param tId The id of the task
     * @param start The start time at which the resources should be available 
     * @return A list of proposed required resources, such that for every ResourceType the requirements are met.
     * @throws ConflictException if there were not enough resources available.
     */
    public List<DetailedResource> getRequiredResources(int pId, int tId, LocalDateTime start) throws ConflictException {
        Task currentTask = manager.getProjectContainer().getProject(pId).getTask(tId);
        Map<ResourceType, Integer> requirement = currentTask.getRequiredResources();
        requirement.remove(ResourceType.DEVELOPER);
        return manager.getResourceContainer().meetRequirements(currentTask, requirement, currentTask.getSpan(start));
    }
    
    //TODO: commentaar
    public List<DetailedResource> getDevelopers() {
    	return new ArrayList<>(manager.getResourceContainer().getResourcesOfType(ResourceType.DEVELOPER));
    }

    /**
     * Returns the possible start times for the task with the given id in the project
     * with the given project id.
     * 
     * @param pId The id of the project the task belongs to
     * @param tId The id of the task
     * @return A set containing hours at which the task currently being planned
     * could posiible be started
     */
    public Set<LocalDateTime> getPossibleStartTimesCurrentTask(int pId, int tId) {
        return manager.getProjectContainer().getProject(pId).getTask(tId).nextAvailableStartingTimes(manager.getResourceContainer(), clock.getTime(), 3);
    }

    /**
     * Update the start and end time and status of this current task.
     *
     * @param pId The id of the projec the task belongs to
     * @param tId The id of the task
     * @param startTime The start time of the task (yyyy-MM-dd HH:mm)
     * @param resources The resource to make a planning for
     * @throws exception.ConflictException The plainning of this task conflicts with
     * at least one other task
     * @throws RuntimeException An error occured whilte updating the currently
     * selected task.
     */
    public void planTask(int pId, int tId, LocalDateTime startTime, List<Integer> resources) throws ConflictException, RuntimeException {
        ArrayList<Resource> res = new ArrayList<>();
        // id's to resources
        for(int i : resources){
            res.add(manager.getResourceContainer().getResource(i));
        }
        //TODO: bad smell
        simulatorCommand.add(manager.getProjectContainer().getProject(pId).getTask(tId).plan(startTime, res, clock));
    }

    //TODO: goeie oplossing? 
	public List<DetailedResource> getResources(DetailedResourceType type) {
		try {
			return new ArrayList<>(manager.getResourceContainer().getResourcesOfType((ResourceType) type));
		} catch(ClassCastException cce) {
			throw new IllegalArgumentException("This type was not recognized by the system. \nPlease enter a valid type");
		}
	}
    
}
