package controller;

import domain.user.Acl;
import domain.user.Auth;
import domain.dto.DetailedProject;
import domain.dto.DetailedTask;
import domain.Project;
import domain.ProjectContainer;
import domain.Resource;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Timespan;
import exception.NoAccessException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the update task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class UpdateTaskStatusHandler extends Handler {

    private final ProjectContainer manager;

    private Task currentTask;
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
    public UpdateTaskStatusHandler(ProjectContainer manager, Clock clock, Auth auth, Acl acl) {
        super(auth, acl);
        this.manager = manager;
        this.clock = clock;
    }

    /**
     * Returns a map with all available tasks in this projectContainer
     * ascociated with their project.
     *
     * @return All available tasks in the projectContainer of this handler.
     */
    public Map<DetailedTask, DetailedProject> getAvailableTasks() {

        return new HashMap<>(manager.getAllAvailableTasks());
    }

    /**
     * Sets the task with given id in the project with the given id as this
     * handlers current task.
     *
     * @param pId The id of the project, this task belongs to.
     * @param tId The id of the task to select.
     */
    public void selectTask(int pId, int tId) {
        currentTask = manager.getProject(pId).getTask(tId);
        currentProject = manager.getProject(pId);
    }

    /**
     * Update the start and end time and status of this current task.
     *
     * @param startTime The start time of the task (yyyy-MM-dd HH:mm)
     * @param endTime The end time of the task (yyyy-MM-dd HH:mm)
     * @param status The status of the task @see domain.Status
     * @throws RuntimeException An error occured whilte updating the currently
     * selected task.
     */
    public void updateCurrentTask(LocalDateTime startTime, LocalDateTime endTime, String status) throws RuntimeException {
        if (currentTask == null || currentProject == null) {
            throw new IllegalStateException("No task currently selected.");
        }
        try {
            if (status.equalsIgnoreCase("finished")) {
                currentTask.finish(new Timespan(startTime, endTime), clock.getTime());
            } else if (status.equalsIgnoreCase("failed")) {
                currentTask.fail(new Timespan(startTime, endTime), clock.getTime());
            } else {
                throw new IllegalArgumentException("The given status doesn't exist.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");

        }

    }
    
    /**
     * Execute the currently selected task
     * @throws RuntimeException something went wrong when executing the task
     */
    public void executeCurrentTask() throws RuntimeException{
        
        if (currentTask == null || currentProject == null) {
            throw new IllegalStateException("No task currently selected.");
        }
        try {
            if (((Resource) auth.getUser()).getReservation(currentTask) != null) {
                currentTask.execute(clock.getTime());
            }            
            
        } catch (ClassCastException e) {
            throw new NoAccessException("Sorry you don't have the right role to execute a task.");
        }
    }

}
