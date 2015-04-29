package controller;

import domain.Acl;
import domain.Auth;
import domain.Project;
import domain.ProjectContainer;
import domain.Resource;
import domain.ResourceType;
import domain.Task;
import domain.datainterface.DetailedResource;
import domain.datainterface.DetailedResourceType;
import domain.datainterface.DetailedTask;
import domain.time.Clock;
import domain.time.Timespan;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This handler, handles the update task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class PlanTaskHandler extends Handler {

    private final ProjectContainer manager;

    private Project currentProject;
    private final Clock clock;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param clock The clock to use in this handler
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */
    public PlanTaskHandler(ProjectContainer manager, Clock clock, Auth auth, Acl acl) {
        super(auth, acl);
        this.manager = manager;
        this.clock = clock;
    }

    /**
     * Returns a list with all unplanned tasks in this projectContainer
     *
     * @return All unplanned tasks in the projectContainer of this handler.
     */
    public List<DetailedTask> getUnplannedTasks() {

        return new ArrayList<>(manager.getAllUnplannedTasks());
    }

    /**
     *
     * @param start The start time at which the resources should be available 
     * @return A list of proposed required resources, ascociated with their resourcetype.
     */
    public List<Entry<DetailedResourceType, DetailedResource>> getRequiredResources(int pId, int tId, LocalDateTime start) {
        Task currentTask = manager.getProject(pId).getTask(tId);
        List<Entry<DetailedResourceType, DetailedResource>> resources = new ArrayList<>();
        for (Entry<ResourceType, Integer> entry : currentTask.getRequiredResources().entrySet()) {
            List<Resource> availableResources = new ArrayList<>();
            availableResources.addAll(entry.getKey().getAvailableResources(new Timespan(start, currentTask.getEstimatedDuration())));
            for (int i = 0; i < entry.getValue(); i++) {
                    // if there are not enough resources available add same resource
                // multiple times
                if (i == availableResources.size()) {
                    resources.add(new SimpleEntry<>(entry.getKey(), availableResources.get(availableResources.size() - 1)));
                } else {
                    resources.add(new SimpleEntry<>(entry.getKey(), availableResources.get(i)));
                }
            }

        }
        return resources;
    }

    /**
     *
     * @return A set containing hours at which the task currently being planned
     * could posiible be started
     */
    public Set<LocalDateTime> getPossibleStartTimesCurrentTask(int pId, int tId) {
       
        return manager.getProject(pId).getTask(tId).nextAvailableStartingTimes(clock.getTime());
    }

    /**
     * Update the start and end time and status of this current task.
     *
     * @param startTime The start time of the task (yyyy-MM-dd HH:mm)
     * @param resources
     * @throws exception.ConflictException The plainning of this task conflicts with
     * at least one other task
     * @throws RuntimeException An error occured whilte updating the currently
     * selected task.
     */
    public void planTask(int pId, int tId, LocalDateTime startTime, List<Resource> resources) throws ConflictException, RuntimeException {
        manager.getProject(pId).getTask(tId).plan(startTime, new ArrayList<>());

    }

}
